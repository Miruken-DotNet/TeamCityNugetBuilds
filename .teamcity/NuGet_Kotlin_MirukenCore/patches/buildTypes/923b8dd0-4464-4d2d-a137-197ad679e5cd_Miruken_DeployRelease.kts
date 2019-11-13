package NuGet_Kotlin_MirukenCore.patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.DotnetNugetPushStep
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.dotnetNugetPush
import jetbrains.buildServer.configs.kotlin.v2018_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with uuid = '923b8dd0-4464-4d2d-a137-197ad679e5cd_Miruken_DeployRelease' (id = 'NuGet_Kotlin_MirukenCore_MirukenSln_Miruken_DeployRelease')
accordingly, and delete the patch script.
*/
changeBuildType(uuid("923b8dd0-4464-4d2d-a137-197ad679e5cd_Miruken_DeployRelease")) {
    expectSteps {
        dotnetNugetPush {
            name = "Push Nuget to Nuget.org"
            packages = "Source/Miruken/bin/Miruken.%PackageVersion%.nupkg"
            serverUrl = "nuget.org"
            apiKey = "%MirukenNugetApiKey%"
            param("configuration", "Debug")
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
            param("outputDir", "nupkg")
            param("teamcity.build.workingDir", "/")
        }
    }
    steps {
        update<DotnetNugetPushStep>(0) {
            apiKey = "credentialsJSON:158e50a6-26fb-4b8d-b8ae-de5d916d8114"
        }
    }

    dependencies {
        expect(AbsoluteId("NuGet_Kotlin_MirukenCore_MirukenSln_ReleaseBuild")) {
            snapshot {
            }

            artifacts {
                cleanDestination = true
                artifactRules = "%ArtifactsOut%"
            }
        }
        update(AbsoluteId("NuGet_Kotlin_MirukenCore_MirukenSln_ReleaseBuild")) {
            snapshot {
                onDependencyFailure = FailureAction.CANCEL
                onDependencyCancel = FailureAction.CANCEL
            }

            artifacts {
                cleanDestination = true
                artifactRules = "%ArtifactsOut%"
            }
        }

    }
}
