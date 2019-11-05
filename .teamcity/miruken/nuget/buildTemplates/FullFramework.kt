package miruken.nuget.buildTemplates

import jetbrains.buildServer.configs.kotlin.v2018_2.BuildStep
import jetbrains.buildServer.configs.kotlin.v2018_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.VisualStudioStep
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.visualStudio
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.vstest
import jetbrains.buildServer.configs.kotlin.v2018_2.Project

class FullFramework {
    companion object {

        private fun compile(buildType: BuildType) : BuildType{
            buildType.steps {
                visualStudio {
                    name                = "CompileStep"
                    id                  = "${buildType.id}_Build"
                    path                = "%Solution%"
                    version             = VisualStudioStep.VisualStudioVersion.vs2017
                    runPlatform         = VisualStudioStep.Platform.x86
                    msBuildVersion      = VisualStudioStep.MSBuildVersion.V15_0
                    msBuildToolsVersion = VisualStudioStep.MSBuildToolsVersion.V15_0
                    targets             = "%BuildTargets%"
                    configuration       = "%BuildConfiguration%"
                    platform            = "Any CPU"
                    executionMode       = BuildStep.ExecutionMode.RUN_ON_SUCCESS
                }
            }
            return buildType
        }

        private fun test(buildType: BuildType) : BuildType{
            buildType.steps {
                vstest {
                    id                   = "${buildType.id}_TestStep"
                    vstestPath           = "%teamcity.dotnet.vstest.14.0%"
                    includeTestFileNames = "%TestAssemblies%"
                    runSettings          = ""
                    testCaseFilter       = "%TestCaseFilter%"
                    executionMode        = BuildStep.ExecutionMode.RUN_ON_SUCCESS
                    coverage = dotcover {
                        toolPath = "%teamcity.tool.JetBrains.dotCover.CommandLineTools.bundled%"
                    }
                }
            }
            return buildType
        }

        private fun fullFrameworkBuild(buildType: BuildType) : BuildType{
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

        private fun deployPreReleaseNuget(buildType: BuildType) : BuildType{
            buildType.steps {
                step {
                    name          = "Prerelease Nuget on TC Feed"
                    id            = "${buildType.id}_PrereleaseNugetStep"
                    type          = "jb.nuget.pack"
                    executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
                    param("toolPathSelector",            "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
                    param("nuget.pack.output.clean",     "true")
                    param("nuget.pack.specFile",         "%NuGetPackSpecFiles%")
                    param("nuget.pack.output.directory", "nupkg")
                    param("nuget.path",                  "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
                    param("nuget.pack.as.artifact",      "true")
                    param("nuget.pack.prefer.project",   "true")
                    param("nuget.pack.version",          "%PackageVersion%")
                }
            }

            return buildType
        }

        private fun deployReleaseNuget(apiKey: String, buildType: BuildType) : BuildType{

            buildType.steps {
                step {
                    name          = "NuGet Pack for NuGet.org"
                    id            = "${buildType.id}_ReleasePackStep"
                    type          = "jb.nuget.pack"
                    executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
                    param("toolPathSelector",            "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
                    param("nuget.pack.output.clean",     "true")
                    param("nuget.pack.specFile",         "%NuGetPackSpecFiles%")
                    param("nuget.pack.include.sources",  "true")
                    param("nuget.pack.output.directory", "nupkg")
                    param("nuget.path",                  "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
                    param("nuget.pack.prefer.project",   "true")
                    param("nuget.pack.version",          "%PackageVersion%")
                }
                step {
                    name          = "Nuget Publish to NuGet.org"
                    id            = "${buildType.id}_ReleasePublishNugetStep"
                    type          = "jb.nuget.publish"
                    executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
                    param("secure:nuget.api.key", apiKey)
                    param("toolPathSelector",     "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
                    param("nuget.path",           "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
                    param("nuget.publish.source", "nuget.org")
                    param("nuget.publish.files",  "nupkg/%NupkgName%")
                }
                step {
                    name          = "Nuget Publish to SymbolSource.org"
                    id            = "${buildType.id}_ReleasePublishSymbolsStep"
                    type          = "jb.nuget.publish"
                    executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
                    param("secure:nuget.api.key", apiKey)
                    param("nuget.path",           "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
                    param("nuget.publish.source", "https://nuget.smbsrc.net/")
                    param("nuget.publish.files",  "nupkg/%NupkgSymbolsName%")
                }
            }

            return buildType
        }

        fun configureNugetSolutionProject(solution: NugetSolution) : Project{

            val ciVcsRoot         = ciVcsRoot(solution)
            val preReleaseVcsRoot = preReleaseVcsRoot(solution)
            val releaseVcsRoot    = releaseVcsRoot(solution)

            val ciBuild           = fullFrameworkBuild(ciBuild(solution, ciVcsRoot))
            val preReleaseBuild   = fullFrameworkBuild(preReleaseBuild(solution, preReleaseVcsRoot))
            val releaseBuild      = versionBuild(tagBuild(fullFrameworkBuild(checkForPreRelease(releaseBuild(solution, releaseVcsRoot)))))

            val solutionProject =  solutionProject(
                    solution,
                    ciVcsRoot,
                    preReleaseVcsRoot,
                    releaseVcsRoot,
                    ciBuild,
                    preReleaseBuild,
                    releaseBuild
            ).also {
                it.params{
                    param("ArtifactsIn", """
                        Source      => Build.zip!/Source
                        packages    => Build.zip!/packages
                        ${solution.solutionFile} => Build.zip!
                    """.trimIndent())
                }
            }

            val deploymentProject = deploymentProject(solution).also {
                it.params {
                    param("ArtifactsOut", """
                        Build.zip!/Source   => Source
                        Build.zip!/packages => packages
                        Build.zip!/${solution.solutionFile}
                    """.trimIndent())
                }
            }

            solutionProject.subProject(deploymentProject)

            for(project in solution.nugetProjects){
                val deployPreReleaseBuild = deployPreReleaseNuget(deployPreRelease(solution, project, preReleaseBuild))
                val deployReleaseBuild    = deployReleaseNuget(solution.nugetApiKey, deployRelease(solution, project, releaseBuild))
                deploymentProject.subProject(nugetDeployProject(solution, project, deployPreReleaseBuild, deployReleaseBuild))
            }

            return solutionProject
        }
    }
}