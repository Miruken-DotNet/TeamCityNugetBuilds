package Miruken.Nuget.BuildTemplates

import jetbrains.buildServer.configs.kotlin.v2017_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.vcs.GitVcsRoot
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.finishBuildTrigger
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.VisualStudioStep
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.visualStudio
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.vstest
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.powerShell
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.PowerShellStep


class NugetSolution(
        val guid:           String,
        val id:             String,
        val parentId:       String,
        val name:           String,
        val solutionFile:   String,
        val testAssemblies: String,
        val codeGithubUrl:  String,
        val nugetApiKey:    String,
        val majorVersion:   String,
        val minorVersion:   String,
        val patchVersion:   String,
        val nugetProjects:  List<NugetProject>){

    val ciVcsRootId: String
        get() = "${id}_CIVCSRoot"

    val releaseVcsRootId: String
        get() = "${id}_ReleaseVCSRoot"

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

    fun restoreNuget(buildType: BuildType) : BuildType{
        buildType.steps {
            step {
                name = "Restore NuGet Packages"
                id   = "${buildType.id}_RestoreNugetStep"
                type = "jb.nuget.installer"
                param("toolPathSelector",          "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
                param("nuget.path",                "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
                param("nuget.sources",             "%PackageSources%")
                param("nuget.updatePackages.mode", "sln")
                param("sln.path",                  "%Solution%")
            }
        }
        return buildType
    }
    fun compile(buildType: BuildType) : BuildType{
        buildType.steps {
            visualStudio {
                name                = "CompileStep"
                id                  = "${buildType.id}_Build"
                path                = "%Solution%"
                version             = VisualStudioStep.VisualStudioVersion.vs2017
                runPlatform         = VisualStudioStep.Platform.x86
                msBuildVersion      = VisualStudioStep.MSBuildVersion.V15_0
                msBuildToolsVersion = VisualStudioStep.MSBuildToolsVersion.V15_0
                targets             = "%BuildTargets%"
                configuration       = "%BuildConfiguration%"
                platform            = "Any CPU"
            }
        }
        return buildType
    }
    fun test(buildType: BuildType) : BuildType{
        buildType.steps {
            vstest {
                id                   = "${buildType.id}_TestStep"
                vstestPath           = "%teamcity.dotnet.vstest.14.0%"
                includeTestFileNames = "%TestAssemblies%"
                runSettings          = ""
                testCaseFilter       = "%TestCaseFilter%"
                coverage = dotcover {
                    toolPath = "%teamcity.tool.JetBrains.dotCover.CommandLineTools.bundled%"
                }
            }
        }
        return buildType
    }

    fun versionBuild(buildType: BuildType) : BuildType{
        buildType.steps {
            powerShell {
                name     = "Increment PatchVersion And Reset Build Counters"
                id       = "${buildType.id}_VersionStep"
                platform = PowerShellStep.Platform.x86
                edition  = PowerShellStep.Edition.Desktop
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
                noProfile = false
            }
        }
        return buildType
    }

    fun tagBuild(buildType: BuildType) : BuildType{
        buildType.steps {
            powerShell {
                name       = "Tag Build From Master Branch"
                id         = "${buildType.id}_TagStep"
                platform   = PowerShellStep.Platform.x86
                edition    = PowerShellStep.Edition.Desktop
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
                noProfile = false
            }
        }

        return buildType
    }

    fun dotNetBuild(buildType: BuildType) : BuildType{
        test(compile(restoreNuget(buildType)))

        buildType.buildNumberPattern = "%BuildFormatSpecification%"

        return buildType
    }

    val ciBuild =  dotNetBuild(BuildType({
        uuid        = "${solution.guid}_CIBuild"
        id          = solution.ciBuildId
        name        = "CI Build"
        description = "Watches git repo & creates a build for any change to any branch. Runs tests. Does NOT package/deploy NuGet packages!"

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
                id      = "symbol-indexer"
                type    = "symbol-indexer"
                enabled = false
            }
        }
    }))

    val preReleaseBuild =  dotNetBuild(BuildType({
        uuid          = "${solution.guid}_PreReleaseBuild"
        id            = solution.preReleaseBuildId
        name          = "PreRelease Build"
        description   = "This will push a NuGet package with a -PreRelease tag for testing from the develop branch. NO CI.   (Note: Non-prerelease nuget packages come from the master branch)"
        artifactRules = "%ArtifactsIn%"

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
                id   = "${solution.id}_symbol-indexer"
                type = "symbol-indexer"
            }
        }
    }))


    val releaseBuild = versionBuild(tagBuild(dotNetBuild(BuildType({
        uuid          = "${solution.guid}_ReleaseBuild"
        id            = solution.releaseBuildId
        name          = "Release Build"
        description   = "This will push a NuGet package from the MASTER branch. NO CI."
        artifactRules = "%ArtifactsIn%"

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

        features {
            feature {
                id   = "${solution.id}_symbol-indexer"
                type = "symbol-indexer"
            }
        }
    }))))

    val deploymentProject = Project({
        uuid     = "${solution.guid}_DeploymentProject"
        id       = solution.deploymentProjectId
        parentId = solution.id
        name     = "Deployment"

        params {
            param("SHA", "")
            param("NugetApiKey", solution.nugetApiKey)
        }

        for(nugetProject in solution.nugetProjects){
            subProject(configureNugetDeployProject(solution, nugetProject, preReleaseBuild, releaseBuild))
        }
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

    fun deployPreReleaseNuget(buildType: BuildType) : BuildType{

        buildType.steps {
            step {
                name = "Prerelease Nuget on TC Feed"
                id = "${buildType.id}_PrereleaseNugetStep"
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

        return buildType
    }

    fun deployReleaseNuget(apiKey: String, buildType: BuildType) : BuildType{

        buildType.steps {
            step {
                name = "NuGet Pack for NuGet.org"
                id   = "${buildType.id}_ReleasePackStep"
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
                id   = "${buildType.id}_ReleasePublishNugetStep"
                type = "jb.nuget.publish"
                param("secure:nuget.api.key", apiKey)
                param("toolPathSelector",     "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
                param("nuget.path",           "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
                param("nuget.publish.source", "nuget.org")
                param("nuget.publish.files",  "nupkg/%NupkgName%")
            }
            step {
                name = "Nuget Publish to SymbolSource.org"
                id   = "${buildType.id}_ReleasePublishSymbolsStep"
                type = "jb.nuget.publish"
                param("secure:nuget.api.key", apiKey)
                param("nuget.path",           "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
                param("nuget.publish.source", "https://nuget.smbsrc.net/")
                param("nuget.publish.files",  "nupkg/%NupkgSymbolsName%")
            }
        }

        return buildType
    }

    val deployPreRelease =  deployPreReleaseNuget(BuildType({
        uuid               = "${baseUuid}_DeployPreRelease"
        id                 = "${baseId}_DeployPreRelease"
        name               = "Deploy PreRelease"
        description        = "This will push a NuGet package with a -PreRelease tag for testing from the develop branch. NO CI.   (Note: Non-prerelease nuget packages come from the master branch)"
        buildNumberPattern = "%BuildFormatSpecification%"

        params {
            param("BuildFormatSpecification", "%dep.${solution.preReleaseBuildId}.BuildFormatSpecification%")
            param("PackageVersion",           "%dep.${solution.preReleaseBuildId}.PackageVersion%")
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
                    id            = "${baseId}_PreRelease_ARTIFACT_DEPENDENCY"
                    artifactRules = "%ArtifactsOut%"
                }
            }
        }
    }))

    val deployRelease = deployReleaseNuget(solution.nugetApiKey, BuildType({
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

        triggers {
            finishBuildTrigger {
                id             = "${baseId}_Release_TRIGGER"
                buildTypeExtId = solution.releaseBuildId
                branchFilter   = "+:master"
            }
        }

        dependencies {
            dependency(releaseBuild){
                snapshot {
                }

                artifacts {
                    id            = "${baseId}_Release_ARTIFACT_DEPENDENCY"
                    artifactRules = "%ArtifactsOut%"
                }
            }
        }
    }))

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
            param("PackageName",        project.packageName)
        }
    })
}
