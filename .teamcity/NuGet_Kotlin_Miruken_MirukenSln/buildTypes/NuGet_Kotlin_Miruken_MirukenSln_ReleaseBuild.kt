package NuGet_Kotlin_Miruken_MirukenSln.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*

object NuGet_Kotlin_Miruken_MirukenSln_ReleaseBuild : BuildType({
    template = "StandardNuGetBuildTemplate"
    uuid = "50fcdffb-6d49-43d4-a0fc-8e6dbc47a532_ReleaseBuild"
    id = "NuGet_Kotlin_Miruken_MirukenSln_ReleaseBuild"
    name = "Release Build"
    description = "This will push a NuGet package from the MASTER branch. NO CI."

    artifactRules = "%ArtifactsIn%"
    buildNumberPattern = "%BuildFormatSpecification%"

    params {
        param("BranchSpecification", "+:refs/heads/(master)")
        param("DefaultBranch", "master")
        param("NuGetPackPrereleaseVersionString", "")
        param("PrereleaseVersion", "")
    }

    vcs {
        root(NuGet_Kotlin_Miruken_MirukenSln.vcsRoots.NuGet_Kotlin_Miruken_MirukenSln_ReleaseVCSRoot)

        checkoutMode = CheckoutMode.ON_AGENT
        cleanCheckout = true
    }
    
    disableSettings("RUNNER_4", "RUNNER_5", "RUNNER_6", "RUNNER_8")
})
