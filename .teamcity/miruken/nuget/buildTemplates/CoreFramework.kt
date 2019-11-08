package miruken.nuget.buildTemplates

import jetbrains.buildServer.configs.kotlin.v2018_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_2.Project
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.*

class CoreFramework {
    companion object {

        private fun compile(buildType: BuildType) : BuildType{
            buildType.steps {
                dotnetBuild {
                    name = "Compile"
                    projects = "%Solution%"
                    configuration = "%BuildConfiguration%"
                    args = "-p:Version=%DotNetAssemblyVersion%"
                    param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
                }
            }
            return buildType
        }

        private fun test(buildType: BuildType) : BuildType{
            buildType.steps {
                dotnetTest {
                    name = "Unit Tests"
                    projects = "%Solution%"
                    configuration = "%BuildConfiguration%"
                    args = "--no-build"
                    param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
                }
            }
            return buildType
        }

        private fun pack(buildType: BuildType, solution: NugetSolution) : BuildType{
            //Having to do these individually instead of solution because it try's to
            //build a nuget for the web test project and fails
            for(project in solution.nugetProjects){
                buildType.steps {
                    dotnetPack {
                        name = "Pack ${project.packageName}"
                        projects = "Source/${project.packageName}/${project.packageName}.csproj"
                        configuration = "%BuildConfiguration%"
                        args = "-p:PackageVersion=%PackageVersion% -p:DebugSymbols=true -p:DebugType=pdbonly -p:Version=%DotNetAssemblyVersion% --include-symbols --include-source --no-build"
                        param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
                    }
                }
            }
            return buildType
        }

        private fun coreBuild(buildType: BuildType) : BuildType{
            test(compile(restoreNuget(buildType)))

            buildType.maxRunningBuilds   = 1
            buildType.buildNumberPattern = "%BuildFormatSpecification%"

            buildType.features {
                feature {
                    type = "JetBrains.AssemblyInfo"
                    param("file-format",     "%DotNetAssemblyVersion%")
                    param("assembly-format", "%DotNetAssemblyVersion%")
                    param("info-format",     "%BuildFormatSpecification%")
                }
            }

            return buildType
        }

        private fun pushPreRelease(buildType: BuildType, project: NugetProject) : BuildType{
            buildType.steps {
                dotnetNugetPush {
                    name      = "Prerelease Nuget on TC Feed"
                    packages  = "Source/${project.packageName}/bin/${project.packageName}.%PackageVersion%.nupkg"
                    serverUrl = "%teamcity.nuget.feed.guestAuth._Root.default.v2%"
                    apiKey    = "%teamcity.nuget.feed.api.key%"
                    param("outputDir", "nupkg")
                    param("teamcity.build.workingDir", "/")
                    param("configuration", "Debug")
                    param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
                }
            }
            return buildType
        }

        private fun pushRelease(buildType: BuildType, solution: NugetSolution, project: NugetProject) : BuildType{
            buildType.steps {
                dotnetNugetPush {
                    name      = "Prerelease Nuget on TC Feed"
                    packages  = "Source/${project.packageName}/bin/${project.packageName}.%PackageVersion%.nupkg"
                    serverUrl = "nuget.org"
                    apiKey    = solution.nugetApiKey
                    param("outputDir", "nupkg")
                    param("teamcity.build.workingDir", "/")
                    param("configuration", "Debug")
                    param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
                }
            }
            return buildType
        }

        fun configureNugetSolutionProject(solution: NugetSolution) : Project{

            val ciVcsRoot         = ciVcsRoot(solution)
            val preReleaseVcsRoot = preReleaseVcsRoot(solution)
            val releaseVcsRoot    = releaseVcsRoot(solution)

            val ciBuild           = coreBuild(ciBuild(solution, ciVcsRoot))
            val preReleaseBuild   = pack(coreBuild(preReleaseBuild(solution, preReleaseVcsRoot)), solution)
            val releaseBuild      = versionBuild(tagBuild(pack(coreBuild(checkForPreRelease(releaseBuild(solution, releaseVcsRoot))), solution)))

            val solutionProject = solutionProject(
                    solution,
                    ciVcsRoot,
                    preReleaseVcsRoot,
                    releaseVcsRoot,
                    ciBuild,
                    preReleaseBuild,
                    releaseBuild
            ).also {
                it.params {
                    param("ArtifactsIn", """
                        Source      => Build.zip!/Source
                        ${solution.solutionFile} => Build.zip!
                    """.trimIndent())
                }
            }

            val deploymentProject = deploymentProject(solution).also {
                it.params {
                    param("ArtifactsOut", """
                        Build.zip!/Source   => Source
                        Build.zip!/${solution.solutionFile}
                    """.trimIndent())
                }
            }

            solutionProject.subProject(deploymentProject)

            for(project in solution.nugetProjects){
                val deployPreReleaseBuild = pushPreRelease(deployPreRelease(solution, project, preReleaseBuild), project)
                val deployReleaseBuild    = pushRelease(deployRelease(solution, project, releaseBuild), solution, project)
                deploymentProject.subProject(nugetDeployProject(solution, project, deployPreReleaseBuild, deployReleaseBuild))
            }

            return solutionProject
        }
    }
}