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
                    projects = "Miruken.sln"
                    configuration = "Debug"
                    param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
                }
            }
            return buildType
        }

        private fun test(buildType: BuildType) : BuildType{
            buildType.steps {
                dotnetTest {
                    name = "Unit Tests"
                    projects = "Test/*/*.csproj"
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