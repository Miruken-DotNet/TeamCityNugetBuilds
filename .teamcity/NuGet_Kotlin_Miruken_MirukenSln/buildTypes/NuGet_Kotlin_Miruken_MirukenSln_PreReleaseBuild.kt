package NuGet_Kotlin_Miruken_MirukenSln.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*

object NuGet_Kotlin_Miruken_MirukenSln_PreReleaseBuild : BuildType({
    template = "StandardNuGetBuildTemplate"
    uuid = "50fcdffb-6d49-43d4-a0fc-8e6dbc47a532_PreReleaseBuild"
    id = "NuGet_Kotlin_Miruken_MirukenSln_PreReleaseBuild"
    name = "PreRelease Build"
    description = "This will push a NuGet package with a -PreRelease tag for testing from the develop branch. NO CI.   (Note: Non-prerelease nuget packages come from the master branch)"

    artifactRules = "%ArtifactsIn%"
    buildNumberPattern = "%BuildFormatSpecification%"

    params {
        param("BranchSpecification", """
            +:refs/heads/(develop)
            +:refs/heads/(feature/*)
        """.trimIndent())
        param("BuildConfiguration", "Debug")
    }

    vcs {
        root(NuGet_Kotlin_Miruken_MirukenSln.vcsRoots.NuGet_Kotlin_Miruken_MirukenSln_ReleaseVCSRoot)

        cleanCheckout = true
    }

    features {
        feature {
            id = "NuGet_Kotlin_Miruken_MirukenSln_symbol-indexer"
            type = "symbol-indexer"
        }
    }
    
    disableSettings("RUNNER_21", "RUNNER_22", "RUNNER_4", "RUNNER_5", "RUNNER_6", "RUNNER_8")
})
