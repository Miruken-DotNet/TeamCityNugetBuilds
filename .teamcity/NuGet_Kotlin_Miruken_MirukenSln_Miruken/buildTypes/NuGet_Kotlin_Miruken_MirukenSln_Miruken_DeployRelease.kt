package NuGet_Kotlin_Miruken_MirukenSln_Miruken.buildTypes

import NuGet_Kotlin_Miruken_MirukenSln.buildTypes.NuGet_Kotlin_Miruken_MirukenSln_ReleaseBuild
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.finishBuildTrigger

object NuGet_Kotlin_Miruken_MirukenSln_Miruken_DeployRelease : BuildType({
    template = "StandardNuGetBuildTemplate"
    uuid = "50fcdffb-6d49-43d4-a0fc-8e6dbc47a532_Miruken_DeployRelease"
    id = "NuGet_Kotlin_Miruken_MirukenSln_Miruken_DeployRelease"
    name = "Deploy Release"
    description = "This will push a NuGet package from the MASTER branch. NO CI."

    buildNumberPattern = "%BuildFormatSpecification%"

    params {
        param("BuildFormatSpecification", "%dep.NuGet_Kotlin_Miruken_MirukenSln_ReleaseBuild.BuildFormatSpecification%")
        param("PackageVersion", "%dep.NuGet_Kotlin_Miruken_MirukenSln_ReleaseBuild.PackageVersion%")
        param("PrereleaseVersion", "")
    }

    triggers {
        finishBuildTrigger {
            id = "NuGet_Kotlin_Miruken_MirukenSln_Miruken_Release_TRIGGER"
            buildTypeExtId = NuGet_Kotlin_Miruken_MirukenSln_ReleaseBuild.id
            branchFilter = "+:master"
        }
    }

    dependencies {
        dependency(NuGet_Kotlin_Miruken_MirukenSln.buildTypes.NuGet_Kotlin_Miruken_MirukenSln_ReleaseBuild) {
            snapshot {
            }

            artifacts {
                id = "NuGet_Kotlin_Miruken_MirukenSln_Miruken_Release_ARTIFACT_DEPENDENCY"
                artifactRules = "%ArtifactsOut%"
            }
        }
    }
    
    disableSettings("JetBrains.AssemblyInfo", "RUNNER_1", "RUNNER_2", "RUNNER_21", "RUNNER_22", "RUNNER_3", "RUNNER_4", "RUNNER_5", "RUNNER_8")
})
