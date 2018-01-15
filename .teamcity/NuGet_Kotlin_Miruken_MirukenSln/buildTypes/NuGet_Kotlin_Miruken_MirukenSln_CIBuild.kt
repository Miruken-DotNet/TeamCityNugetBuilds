package NuGet_Kotlin_Miruken_MirukenSln.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.vcs

object NuGet_Kotlin_Miruken_MirukenSln_CIBuild : BuildType({
    template = "StandardNuGetBuildTemplate"
    uuid = "50fcdffb-6d49-43d4-a0fc-8e6dbc47a532_CIBuild"
    id = "NuGet_Kotlin_Miruken_MirukenSln_CIBuild"
    name = "CI Build"
    description = "Watches git repo & creates a build for any change to any branch. Runs tests. Does NOT package/deploy NuGet packages!"

    buildNumberPattern = "%BuildFormatSpecification%"

    params {
        param("BranchSpecification", "+:refs/heads/(*)")
        param("MajorVersion", "0")
        param("MinorVersion", "0")
        param("PatchVersion", "0")
        param("PdbFilesForSymbols", "")
        param("PrereleaseVersion", "-CI.%build.counter%")
    }

    vcs {
        root(NuGet_Kotlin_Miruken_MirukenSln.vcsRoots.NuGet_Kotlin_Miruken_MirukenSln_CIVCSRoot)

        cleanCheckout = true
    }

    triggers {
        vcs {
            id = "NuGet_Kotlin_Miruken_MirukenSln_ci_vcsTrigger"
            quietPeriodMode = VcsTrigger.QuietPeriodMode.USE_DEFAULT
            perCheckinTriggering = true
            groupCheckinsByCommitter = true
            enableQueueOptimization = false
        }
    }

    features {
        feature {
            id = "symbol-indexer"
            type = "symbol-indexer"
            enabled = false
        }
    }
    
    disableSettings("RUNNER_21", "RUNNER_22", "RUNNER_4", "RUNNER_5", "RUNNER_6", "RUNNER_8")
})
