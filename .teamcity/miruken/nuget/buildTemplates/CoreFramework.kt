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
                    param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
                }
            }
            return buildType
        }

        private fun test(buildType: BuildType) : BuildType{
            buildType.steps {
                dotnetTest {
                    name = "Unit Tests"
                    projects = "%TestAssemblies%"
                    param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
                }
            }
            return buildType
        }

        private fun pack(buildType: BuildType) : BuildType{
            buildType.steps {
                dotnetPack {
                    name = ".NET CORE (pack)"
                    projects = "%Solution%"
                    args = "-p:PackageVersion=%PackageVersion% -p:DebugSymbols=true -p:DebugType=pdbonly -p:Version=%DotNetAssemblyVersion% --include-symbols --include-source"
                    param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
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
                    apiKey    = "credentialsJSON:67fc8f44-f16c-456e-8d71-df4fcf9eceb2"
                    param("outputDir", "nupkg")
                    param("teamcity.build.workingDir", "/")
                    param("configuration", "Debug")
                    param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
                }
            }
            return buildType
        }

        private fun pushRelease(buildType: BuildType, project: NugetProject, apiKeyParam: String) : BuildType{
            buildType.steps {
                dotnetNugetPush {
                    name      = "Prerelease Nuget on TC Feed"
                    packages  = "Source/${project.packageName}/bin/${project.packageName}.%PackageVersion%.nupkg"
                    serverUrl = "%teamcity.nuget.feed.guestAuth._Root.default.v2%"
                    apiKey    = "credentialsJSON:67fc8f44-f16c-456e-8d71-df4fcf9eceb2"
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
            val preReleaseBuild   = pack(coreBuild(preReleaseBuild(solution, preReleaseVcsRoot)))
            val releaseBuild      = versionBuild(tagBuild(pack(coreBuild(checkForPreRelease(releaseBuild(solution, releaseVcsRoot))))))

            val solutionProject = solutionProject(
                    solution,
                    ciVcsRoot,
                    preReleaseVcsRoot,
                    releaseVcsRoot,
                    ciBuild,
                    preReleaseBuild,
                    releaseBuild
            )

            val deploymentProject = deploymentProject(solution)
            solutionProject.subProject(deploymentProject)

            for(project in solution.nugetProjects){
                val deployPreReleaseBuild = pushPreRelease(deployPreRelease(solution, project, preReleaseBuild), project)
                val deployReleaseBuild    = pushRelease(deployRelease(solution, project, releaseBuild), project, solution.nugetApiKey)
                deploymentProject.subProject(nugetDeployProject(solution, project, deployPreReleaseBuild, deployReleaseBuild))
            }

            return solutionProject
        }
    }
}