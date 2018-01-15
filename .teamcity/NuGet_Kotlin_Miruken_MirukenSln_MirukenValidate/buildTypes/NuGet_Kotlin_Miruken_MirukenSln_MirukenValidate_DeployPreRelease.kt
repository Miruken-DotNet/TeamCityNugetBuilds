package NuGet_Kotlin_Miruken_MirukenSln_MirukenValidate.buildTypes

import NuGet_Kotlin_Miruken_MirukenSln.buildTypes.NuGet_Kotlin_Miruken_MirukenSln_PreReleaseBuild
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.finishBuildTrigger

object NuGet_Kotlin_Miruken_MirukenSln_MirukenValidate_DeployPreRelease : BuildType({
    template = "StandardNuGetBuildTemplate"
    uuid = "50fcdffb-6d49-43d4-a0fc-8e6dbc47a532_MirukenValidate_DeployPreRelease"
    id = "NuGet_Kotlin_Miruken_MirukenSln_MirukenValidate_DeployPreRelease"
    name = "Deploy PreRelease"
    description = "This will push a NuGet package with a -PreRelease tag for testing from the develop branch. NO CI.   (Note: Non-prerelease nuget packages come from the master branch)"

    buildNumberPattern = "%BuildFormatSpecification%"

    params {
        param("BuildFormatSpecification", "%dep.NuGet_Kotlin_Miruken_MirukenSln_PreReleaseBuild.BuildFormatSpecification%")
        param("PackageVersion", "%dep.NuGet_Kotlin_Miruken_MirukenSln_PreReleaseBuild.PackageVersion%")
    }

    triggers {
        finishBuildTrigger {
            id = "NuGet_Kotlin_Miruken_MirukenSln_MirukenValidate_DeployPreRelease_TRIGGER"
            buildTypeExtId = NuGet_Kotlin_Miruken_MirukenSln_PreReleaseBuild.id
            successfulOnly = true
            branchFilter = "+:*"
        }
    }

    dependencies {
        dependency(NuGet_Kotlin_Miruken_MirukenSln.buildTypes.NuGet_Kotlin_Miruken_MirukenSln_PreReleaseBuild) {
            snapshot {
            }

            artifacts {
                id = "NuGet_Kotlin_Miruken_MirukenSln_MirukenValidate_PreRelease_ARTIFACT_DEPENDENCY"
                artifactRules = "%ArtifactsOut%"
            }
        }
    }
    
    disableSettings("JetBrains.AssemblyInfo", "RUNNER_1", "RUNNER_2", "RUNNER_21", "RUNNER_22", "RUNNER_3", "RUNNER_5", "RUNNER_6", "RUNNER_8")
})
