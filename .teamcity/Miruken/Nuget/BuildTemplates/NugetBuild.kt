package Miruken.Nuget.BuildTemplates

import jetbrains.buildServer.configs.kotlin.v2017_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.vcs.GitVcsRoot
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.*
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

    val stepRestoreNuGets = BuildStep {
        name = "Install NuGet Packages"
        id = "RUNNER_1"
        type = "jb.nuget.installer"
        param("toolPathSelector",          "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
        param("nuget.path",                "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
        param("nuget.sources",             "%PackageSources%")
        param("nuget.updatePackages.mode", "sln")
        param("sln.path",                  "%Solution%")
    }

    val stepCompile = VisualStudioStep {
        name                 = "Compile"
        id                   = "RUNNER_2"
        path                 = "%Solution%"
        version              = VisualStudioStep.VisualStudioVersion.vs2017
        runPlatform          = VisualStudioStep.Platform.x86
        msBuildVersion       = VisualStudioStep.MSBuildVersion.V15_0
        msBuildToolsVersion  = VisualStudioStep.MSBuildToolsVersion.V15_0
        targets              = "%BuildTargets%"
        configuration        = "%BuildConfiguration%"
        platform             = "Any CPU"
    }

    val stepTest = VSTestStep {
        id                   = "RUNNER_3"
        vstestPath           = "%teamcity.dotnet.vstest.14.0%"
        includeTestFileNames = "%TestAssemblies%"
        runSettings          = "%VSTestRunSettings%"
        testCaseFilter       = "%TestCaseFilter%"
        coverage             = dotcover {
            toolPath = "%teamcity.tool.JetBrains.dotCover.CommandLineTools.bundled%"
        }
    }

    val stepIncrementVerison = PowerShellStep {
        name      = "Increment PatchVersion And Reset Build Counters"
        id        = "RUNNER_21"
        platform  = PowerShellStep.Platform.x86
        edition   = PowerShellStep.Edition.Desktop
        noProfile = false
        scriptMode = script {
            content = """
                    ${'$'}baseUri           = "localhost"
                    ${'$'}projectId         = "%SolutionProjectId%"
                    ${'$'}preReleaseBuildId = "%PreReleaseProjectId%"
                    ${'$'}releaseBuildId    = "%ReleaseProjectId%"
                    ${'$'}branch            = "%teamcity.build.branch%"
                    ${'$'}username          = "%teamcityApiUserName%"
                    ${'$'}password          = "%teamcityApiPassword%"
                    ${'$'}base64AuthInfo    = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes(("{0}:{1}" -f ${'$'}username,${'$'}password)))

                    Write-Host "temp ${'$'}username ${'$'}password"

                    if(${'$'}branch -ne "master") {return 0};

                    function Increment-ProjectPatchVersion (${'$'}projectId) {
                        #get PatchVersion
                        ${'$'}paramUri    ="${'$'}baseUri/httpAuth/app/rest/projects/id:${'$'}projectId/parameters/PatchVersion"
                        Write-Host ${'$'}paramUri
                        ${'$'}paramResult = Invoke-RestMethod -Headers @{Authorization=("Basic {0}" -f ${'$'}base64AuthInfo)} -Method Get -Uri ${'$'}paramUri

                        #increment PatchVersion
                        ${'$'}newPatchVersion = ([int]${'$'}paramResult.property.value) + 1
                        ${'$'}updateResult    = Invoke-RestMethod -Headers @{Authorization=("Basic {0}" -f ${'$'}base64AuthInfo);"Content-Type"="text/plain"} -Method Put -Uri ${'$'}paramUri -Body ${'$'}newPatchVersion
                        Write-Host "Project ${'$'}projectId PatchVersion parameter incremented to ${'$'}newPatchVersion"
                    }

                    function Reset-BuildCounter(${'$'}buildId) {
                        ${'$'}buildCounterUri = "${'$'}baseUri/httpAuth/app/rest/buildTypes/id:${'$'}buildId/settings/buildNumberCounter"
                        Write-Host ${'$'}buildCounterUri
                        ${'$'}updateResult    = Invoke-RestMethod -Headers @{Authorization=("Basic {0}" -f ${'$'}base64AuthInfo);"Content-Type"="text/plain"} -Method Put -Uri ${'$'}buildCounterUri -Body 0
                        Write-Host "Reset build counter for ${'$'}(${'$'}_.name)"
                    }

                    Increment-ProjectPatchVersion ${'$'}projectId
                    Reset-BuildCounter            ${'$'}preReleaseBuildId
                    Reset-BuildCounter            ${'$'}releaseBuildId
                """.trimIndent()
        }
    }

    val stepTagBuild = PowerShellStep {
        name      = "Tag Build From Master Branch"
        id        = "RUNNER_22"
        platform  = PowerShellStep.Platform.x86
        edition   = PowerShellStep.Edition.Desktop
        noProfile = false
        scriptMode = script {
            content = """
                    ${'$'}branch = "%teamcity.build.branch%"

                    if(${'$'}branch -ne "master") { return 0 }

                    ${'$'}tag = "%SemanticVersion%"
                    Write-Host "Taging build ${'$'}tag"

                    git tag ${'$'}tag
                    git push origin ${'$'}tag
                """.trimIndent()
        }
    }

    val ciBuild =  BuildType({
        uuid        = "${solution.guid}_CIBuild"
        id          = solution.ciBuildId
        name        = "CI Build"
        description = "Watches git repo & creates a build for any change to any branch. Runs tests. Does NOT package/deploy NuGet packages!"

        buildNumberPattern = "%BuildFormatSpecification%"

        steps {
            stepRestoreNuGets
            stepCompile
            stepTest
        }

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

        features {
            feature {
                id = "symbol-indexer"
                type = "symbol-indexer"
                enabled = false
            }
        }
    })

    val preReleaseBuild =  BuildType({
        uuid        = "${solution.guid}_PreReleaseBuild"
        id          = solution.preReleaseBuildId
        name        = "PreRelease Build"
        description = "This will push a NuGet package with a -PreRelease tag for testing from the develop branch. NO CI.   (Note: Non-prerelease nuget packages come from the master branch)"

        artifactRules = "%ArtifactsIn%"
        buildNumberPattern = "%BuildFormatSpecification%"

        steps {
            stepRestoreNuGets
            stepCompile
            stepTest
        }

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
    })


    val releaseBuild = BuildType({
        uuid        = "${solution.guid}_ReleaseBuild"
        id          = solution.releaseBuildId
        name        = "Release Build"
        description = "This will push a NuGet package from the MASTER branch. NO CI."

        artifactRules = "%ArtifactsIn%"
        buildNumberPattern = "%BuildFormatSpecification%"

        steps {
            stepRestoreNuGets
            stepCompile
            stepTest
            stepIncrementVerison
            stepTagBuild
        }

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
        template           = "StandardNuGetBuildTemplate"
        uuid               = "${baseUuid}_DeployPreRelease"
        id                 = "${baseId}_DeployPreRelease"
        name               = "Deploy PreRelease"
        description        = "This will push a NuGet package with a -PreRelease tag for testing from the develop branch. NO CI.   (Note: Non-prerelease nuget packages come from the master branch)"
        buildNumberPattern = "%BuildFormatSpecification%"

        params {
            param("BuildFormatSpecification", "%dep.${solution.preReleaseBuildId}.BuildFormatSpecification%")
            param("PackageVersion", "%dep.${solution.preReleaseBuildId}.PackageVersion%")
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

        disableSettings("JetBrains.AssemblyInfo", "RUNNER_1", "RUNNER_2", "RUNNER_21", "RUNNER_22", "RUNNER_3", "RUNNER_5", "RUNNER_6", "RUNNER_8")
    })

    val deployRelease = BuildType({
        template     = "StandardNuGetBuildTemplate"
        uuid         = "${baseUuid}_DeployRelease"
        id           = "${baseId}_DeployRelease"
        name         = "Deploy Release"
        description  = "This will push a NuGet package from the MASTER branch. NO CI."

        buildNumberPattern = "%BuildFormatSpecification%"

        params {
            param("BuildFormatSpecification", "%dep.${solution.releaseBuildId}.BuildFormatSpecification%")
            param("PackageVersion", "%dep.${solution.releaseBuildId}.PackageVersion%")
            param("PrereleaseVersion", "")
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

        disableSettings("JetBrains.AssemblyInfo", "RUNNER_1", "RUNNER_2", "RUNNER_21", "RUNNER_22", "RUNNER_3", "RUNNER_4", "RUNNER_5")
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
