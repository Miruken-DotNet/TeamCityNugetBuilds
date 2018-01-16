package Miruken.Nuget.BuildTemplates

import jetbrains.buildServer.configs.kotlin.v2017_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.vcs.GitVcsRoot
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.finishBuildTrigger

class NugetSolution(
        val guid:           String,
        val id:             String,
        val parentId:       String,
        val name:           String,
        val solutionFile:   String,
        val testAssemblies: String,
        val codeGithubUrl:  String,
        val nugetApiKey:    String,
        val majorVersion:          String,
        val minorVersion:          String,
        val patchVersion:          String,
        val nugetProjects:  List<NugetProject>){

    val ciVcsRootId: String
        get() = "${id}_CIVCSRoot"

    val releaseVcsRootId: String
        get() = "${id}_ReleaseVCSRoot"

    val teamCityVcsRootId: String
        get() = "${id}_TeamCityVCSRoot"

    val ciBuildId: String
        get() = "${id}_CIBuild"

    val preReleaseBuildId: String
        get() = "${id}_PreReleaseBuild"

    val releaseBuildId: String
        get() = "${id}_ReleaseBuild"

    val deploymentProjectId: String
        get() = "${id}_DeploymentProject"
}

class NugetProject(
        val id:          String,
        val nuspecFile:  String,
        val packageName: String)


fun configureNugetSolutionProject(solution: NugetSolution) : Project{

    val ciVcsRoot = GitVcsRoot({
        uuid             = "${solution.guid}CIVcsRoot"
        id               = solution.ciVcsRootId
        name             = "${solution.codeGithubUrl}_CI"
        url              = solution.codeGithubUrl
        branch           = "%DefaultBranch%"
        branchSpec       = "%BranchSpecification%"
        agentCleanPolicy = GitVcsRoot.AgentCleanPolicy.ALWAYS
        authMethod = uploadedKey {
            uploadedKey = "provenstyle"
        }
    })

    val releaseVcsRoot = GitVcsRoot({
        uuid             = "${solution.guid}ReleaseVcsRoot"
        id               = solution.releaseVcsRootId
        name             = "${solution.codeGithubUrl}_Release"
        url              = solution.codeGithubUrl
        branch           = "%DefaultBranch%"
        branchSpec       = "%BranchSpecification%"
        agentCleanPolicy = GitVcsRoot.AgentCleanPolicy.ALWAYS
        authMethod = uploadedKey {
            uploadedKey = "provenstyle"
        }
    })

    val ciBuild =  BuildType({
        template    = "StandardNuGetBuildTemplate"
        uuid        = "${solution.guid}_CIBuild"
        id          = solution.ciBuildId
        name        = "CI Build"
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
            root(ciVcsRoot)
            cleanCheckout = true
        }

        triggers {
            vcs {
                id = "${solution.id}_ci_vcsTrigger"
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

    val preReleaseBuild =  BuildType({
        template    = "StandardNuGetBuildTemplate"
        uuid        = "${solution.guid}_PreReleaseBuild"
        id          = solution.preReleaseBuildId
        name        = "PreRelease Build"
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
            root(releaseVcsRoot)
            cleanCheckout = true
        }

        features {
            feature {
                id = "${solution.id}_symbol-indexer"
                type = "symbol-indexer"
            }
        }

        disableSettings("RUNNER_21", "RUNNER_22", "RUNNER_4", "RUNNER_5", "RUNNER_6", "RUNNER_8")
    })


    val releaseBuild = BuildType({
        template    = "StandardNuGetBuildTemplate"
        uuid        = "${solution.guid}_ReleaseBuild"
        id          = solution.releaseBuildId
        name        = "Release Build"
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
            root(releaseVcsRoot)
            cleanCheckout = true
            checkoutMode = CheckoutMode.ON_AGENT
        }

        disableSettings("RUNNER_4", "RUNNER_5", "RUNNER_6", "RUNNER_8")
    })

    val deploymentProject = Project({
        uuid     = "${solution.guid}_DeploymentProject"
        id       = solution.deploymentProjectId
        parentId = solution.id
        name = "Deployment"

        params {
            param("SHA", "")
            param("NugetApiKey", solution.nugetApiKey)
        }

        for(nugetProject in solution.nugetProjects){
            subProject(configureNugetDeployProject(solution, nugetProject, preReleaseBuild, releaseBuild))
        }

        subProjectsOrder = arrayListOf("NuGet_MirukenDotNet_MirukenSolutionKotlin_Deployment_Miruken", "NuGet_MirukenDotNet_MirukenSolutionKotlin_Deployment_MirukenCastle", "NuGet_MirukenDotNet_MirukenSolutionKotlin_Deployment_MirukenValidate", "NuGet_MirukenDotNet_MirukenSolutionKotlin_Deployment_MirukenValidateCastle")
    })

    return Project({
        uuid        = solution.guid
        id          = solution.id
        parentId    = solution.parentId
        name        = solution.name
        description = "CI/CD for ${solution.solutionFile}"

        vcsRoot(ciVcsRoot)
        vcsRoot(releaseVcsRoot)

        buildType(ciBuild)
        buildType(preReleaseBuild)
        buildType(releaseBuild)

        params {
            param("ArtifactsIn", """
            Source      => Build.zip!/Source
            packages    => Build.zip!/packages
            ${solution.solutionFile} => Build.zip!
        """.trimIndent())
            param("ArtifactsOut", """
            Build.zip!/Source   => Source
            Build.zip!/packages => packages
            Build.zip!/${solution.solutionFile}
        """.trimIndent())
            param("MajorVersion",        solution.majorVersion)
            param("MinorVersion",        solution.minorVersion)
            param("PatchVersion",        solution.patchVersion)
            param("PreReleaseProjectId", solution.preReleaseBuildId)
            param("ReleaseProjectId",    solution.releaseBuildId)
            param("Solution",            solution.solutionFile)
            param("SolutionProjectId",   solution.id)
            param("TestAssemblies",      solution.testAssemblies)
        }

        buildTypesOrder = arrayListOf(ciBuild, preReleaseBuild, releaseBuild)
        subProjectsOrder = arrayListOf(deploymentProject.id)

        subProject(deploymentProject)
    })
}

fun configureNugetDeployProject (
        solution: NugetSolution,
        project: NugetProject,
        preReleaseBuild: BuildType,
        releaseBuild: BuildType) : Project{

    val baseUuid = "${solution.guid}_${project.id}"
    val baseId   = "${solution.id}_${project.id}"

    val deployPreRelease =  BuildType({
        uuid               = "${baseUuid}_DeployPreRelease"
        id                 = "${baseId}_DeployPreRelease"
        name               = "Deploy PreRelease"
        description        = "This will push a NuGet package with a -PreRelease tag for testing from the develop branch. NO CI.   (Note: Non-prerelease nuget packages come from the master branch)"
        buildNumberPattern = "%BuildFormatSpecification%"

        params {
            param("BuildFormatSpecification", "%dep.${solution.preReleaseBuildId}.BuildFormatSpecification%")
            param("PackageVersion",           "%dep.${solution.preReleaseBuildId}.PackageVersion%")
        }

        steps {
            step {
                name = "Prerelease Nuget on TC Feed"
                id = "${baseId}_PrereleaseNugetStep"
                type = "jb.nuget.pack"
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

        triggers {
            finishBuildTrigger {
                id = "${baseId}_DeployPreRelease_TRIGGER"
                buildTypeExtId = solution.preReleaseBuildId
                successfulOnly = true
                branchFilter = "+:*"
            }
        }

        dependencies {
            dependency(preReleaseBuild) {
                snapshot {
                }

                artifacts {
                    id = "${baseId}_PreRelease_ARTIFACT_DEPENDENCY"
                    artifactRules = "%ArtifactsOut%"
                }
            }
        }
    })

    val deployRelease = BuildType({
        uuid         = "${baseUuid}_DeployRelease"
        id           = "${baseId}_DeployRelease"
        name         = "Deploy Release"
        description  = "This will push a NuGet package from the MASTER branch. NO CI."

        buildNumberPattern = "%BuildFormatSpecification%"

        params {
            param("BuildFormatSpecification", "%dep.${solution.releaseBuildId}.BuildFormatSpecification%")
            param("PackageVersion",           "%dep.${solution.releaseBuildId}.PackageVersion%")
            param("PrereleaseVersion",        "")
        }

        steps {
            step {
                name = "NuGet Pack for NuGet.org"
                id   = "${baseId}_ReleasePackStep"
                type = "jb.nuget.pack"
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
                name = "Nuget Publish to NuGet.org"
                id   = "${baseId}_ReleasePublishStep"
                type = "jb.nuget.publish"
                param("toolPathSelector",     "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
                param("secure:nuget.api.key", "zxx2e3163a9797ccbf3894c3a62d6d95c15")
                param("nuget.path",           "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
                param("nuget.publish.source", "nuget.org")
                param("nuget.publish.files",  "nupkg/%NupkgName%")
            }
        }

        triggers {
            finishBuildTrigger {
                id = "${baseId}_Release_TRIGGER"
                buildTypeExtId = solution.releaseBuildId
                branchFilter = "+:master"
            }
        }

        dependencies {
            dependency(releaseBuild){
                snapshot {
                }

                artifacts {
                    id = "${baseId}_Release_ARTIFACT_DEPENDENCY"
                    artifactRules = "%ArtifactsOut%"
                }
            }
        }
    })

    return Project({
        uuid        = baseUuid
        id          = baseId
        parentId    = solution.deploymentProjectId
        name        = project.packageName
        description = "${project.packageName} nuget package"

        buildType(deployPreRelease)
        buildType(deployRelease)

        params {
            param("NuGetPackSpecFiles", project.nuspecFile)
            param("PackageName", project.packageName)
        }
        buildTypesOrder = arrayListOf(deployPreRelease, deployRelease)
    })
}
