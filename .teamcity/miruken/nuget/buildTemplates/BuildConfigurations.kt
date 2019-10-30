package miruken.nuget.buildTemplates

import jetbrains.buildServer.configs.kotlin.v2018_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_2.CheckoutMode
import jetbrains.buildServer.configs.kotlin.v2018_2.VcsRoot
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.finishBuildTrigger
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.vcs

fun ciBuild(solution: NugetSolution, ciVcsRoot: VcsRoot) : BuildType =  BuildType {
    id(solution.ciBuildId)
    uuid        = "${solution.guid}_CIBuild"
    name        = "CI Build"
    description = "Watches git repo & creates a build for any change to any branch. Runs tests. Does NOT package/deploy NuGet packages!"

    allowExternalStatus = true

    params {
        param("BranchSpecification", "+:refs/heads/(*)")
        param("MajorVersion",        "0")
        param("MinorVersion",        "0")
        param("PatchVersion",        "0")
        param("PdbFilesForSymbols",  "")
        param("PrereleaseVersion",   "-CI.%build.counter%")
    }

    vcs {
        root(ciVcsRoot)
        cleanCheckout = true
    }

    triggers {
        vcs {
            id                       = "${solution.id}_ci_vcsTrigger"
            quietPeriodMode          = VcsTrigger.QuietPeriodMode.USE_DEFAULT
            perCheckinTriggering     = true
            groupCheckinsByCommitter = true
            enableQueueOptimization  = false
        }
    }
}

fun preReleaseBuild(solution: NugetSolution, preReleaseVcsRoot: VcsRoot) = BuildType {
    id(solution.preReleaseBuildId)
    uuid          = "${solution.guid}_PreReleaseBuild"
    name          = "PreRelease Build"
    description   = "This will push a NuGet package with a -PreRelease tag for testing from the develop branch. NO CI.   (Note: Non-prerelease nuget packages come from the master branch)"
    artifactRules = "%ArtifactsIn%"

    allowExternalStatus = true

    params {
        param("BranchSpecification", """
            +:refs/heads/(develop)
            +:refs/heads/(feature/*)
        """.trimIndent())
        param("BuildConfiguration", "Debug")
    }

    vcs {
        root(preReleaseVcsRoot)
        cleanCheckout = true
    }

    features {
        feature {
            id   = "${solution.id}_symbol-indexer"
            type = "symbol-indexer"
        }
    }
}

fun releaseBuild(solution: NugetSolution, releaseVcsRoot: VcsRoot) = BuildType {
    id(solution.releaseBuildId)
    uuid          = "${solution.guid}_ReleaseBuild"
    name          = "Release Build"
    description   = "This will push a NuGet package from the MASTER branch. NO CI."
    artifactRules = "%ArtifactsIn%"

    allowExternalStatus = true

    params {
        param("BranchSpecification",              "+:refs/heads/(master)")
        param("DefaultBranch",                    "master")
        param("NuGetPackPrereleaseVersionString", "")
        param("PrereleaseVersion",                "")
    }

    vcs {
        root(releaseVcsRoot)
        cleanCheckout = true
        checkoutMode = CheckoutMode.ON_AGENT
    }
}

fun deployPreRelease(solution: NugetSolution, baseId: String, baseUuid: String, preReleaseBuild: BuildType) : BuildType =  BuildType {
    id("${baseId}_DeployPreRelease")
    uuid               = "${baseUuid}_DeployPreRelease"
    name               = "Deploy PreRelease"
    description        = "This will push a NuGet package with a -PreRelease tag for testing from the develop branch. NO CI.   (Note: Non-prerelease nuget packages come from the master branch)"
    buildNumberPattern = "%BuildFormatSpecification%"
    maxRunningBuilds   = 1

    params {
        param("BuildFormatSpecification", "%dep.${solution.preReleaseBuildId}.BuildFormatSpecification%")
        param("PackageVersion",           "%dep.${solution.preReleaseBuildId}.PackageVersion%")
    }

    triggers {
        finishBuildTrigger {
            id = "${baseId}_DeployPreRelease_TRIGGER"
            buildType = solution.preReleaseBuildId
            successfulOnly = true
            branchFilter = "+:*"
        }
    }

    dependencies {
        dependency(preReleaseBuild) {
            snapshot {
            }

            artifacts {
                id               = "${baseId}_PreRelease_ARTIFACT_DEPENDENCY"
                cleanDestination = true
                artifactRules    = "%ArtifactsOut%"
            }
        }
    }
}

fun deployRelease(solution: NugetSolution, baseId: String, baseUuid: String, releaseBuild: BuildType) : BuildType = BuildType {
    id("${baseId}_DeployRelease")
    uuid         = "${baseUuid}_DeployRelease"
    name         = "Deploy Release"
    description  = "This will push a NuGet package from the MASTER branch. NO CI."

    buildNumberPattern = "%BuildFormatSpecification%"
    maxRunningBuilds   = 1

    params {
        param("BuildFormatSpecification", "%dep.${solution.releaseBuildId}.BuildFormatSpecification%")
        param("PackageVersion",           "%dep.${solution.releaseBuildId}.PackageVersion%")
        param("PrereleaseVersion",        "")
    }

    triggers {
        finishBuildTrigger {
            id             = "${baseId}_Release_TRIGGER"
            buildType      = solution.releaseBuildId
            branchFilter   = "+:master"
        }
    }

    dependencies {
        dependency(releaseBuild){
            snapshot {
            }

            artifacts {
                id               = "${baseId}_Release_ARTIFACT_DEPENDENCY"
                cleanDestination = true
                artifactRules    = "%ArtifactsOut%"
            }
        }
    }
}
