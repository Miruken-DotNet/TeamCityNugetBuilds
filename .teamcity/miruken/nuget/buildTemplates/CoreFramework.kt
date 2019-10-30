package miruken.nuget.buildTemplates

import jetbrains.buildServer.configs.kotlin.v2018_2.BuildStep
import jetbrains.buildServer.configs.kotlin.v2018_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.VisualStudioStep
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.visualStudio
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.vstest
import jetbrains.buildServer.configs.kotlin.v2018_2.Project

class CoreFramework {
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

        fun configureNugetSolutionProject(solution: NugetSolution) : Project{

            val ciVcsRoot         = ciVcsRoot(solution)
            val preReleaseVcsRoot = preReleaseVcsRoot(solution)
            val releaseVcsRoot    = releaseVcsRoot(solution)

            val ciBuild           = coreBuild(ciBuild(solution, ciVcsRoot))
            val preReleaseBuild   = coreBuild(preReleaseBuild(solution, preReleaseVcsRoot))
            val releaseBuild      = versionBuild(tagBuild(coreBuild(checkForPreRelease(releaseBuild(solution, releaseVcsRoot)))))

            return solutionProject(
                    solution,
                    ciVcsRoot,
                    preReleaseVcsRoot,
                    releaseVcsRoot,
                    ciBuild,
                    preReleaseBuild,
                    releaseBuild
            )
        }
    }
}