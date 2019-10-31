package miruken.nuget.buildTemplates

import jetbrains.buildServer.configs.kotlin.v2018_2.BuildStep
import jetbrains.buildServer.configs.kotlin.v2018_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.PowerShellStep
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.powerShell

fun checkForPreRelease(buildType: BuildType) : BuildType{
    buildType.steps {
        powerShell {
            name                = "Check For PreRelease Dependency"
            formatStderrAsError = true
            executionMode       = BuildStep.ExecutionMode.RUN_ON_SUCCESS
            scriptMode = script {
                content = """
                        try{
                            ${'$'}packageConfigs = @(Get-ChildItem -Path .\ -Recurse -Include packages.config)
                            foreach(${'$'}packageConfig in ${'$'}packageConfigs )
                            {
                                ${'$'}text = Get-Content ${'$'}packageConfig -Raw
                                ${'$'}keywords = @("prerelease", "alpha", "beta")
                                foreach(${'$'}keyword in ${'$'}keywords){
                                    if(${'$'}text -like "*${'$'}keyword*") {
                                        throw "Prerelease dependency found in ${'$'}(${'$'}packageConfig.FullName)"
                                    }
                                }
                            }

                            return 0
                        } catch {
                            Write-Error ${'$'}_
                            Write-Host "##teamcity[buildStatus status='FAILURE' text='PreRelease dependency detected']"
                            return 1
                        }
                    """.trimIndent()
            }
        }
    }
    return buildType
}

fun restoreNuget(buildType: BuildType) : BuildType{
    buildType.steps {
        step {
            name          = "Restore NuGet Packages"
            id            = "${buildType.id}_RestoreNugetStep"
            type          = "jb.nuget.installer"
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
            param("toolPathSelector",          "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
            param("nuget.path",                "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
            param("nuget.sources",             "%PackageSources%")
            param("nuget.updatePackages.mode", "sln")
            param("sln.path",                  "%Solution%")
        }
    }
    return buildType
}

fun versionBuild(buildType: BuildType) : BuildType{
    buildType.steps {
        powerShell {
            name          = "Increment PatchVersion And Reset Build Counters"
            id            = "${buildType.id}_VersionStep"
            platform      = PowerShellStep.Platform.x86
            edition       = PowerShellStep.Edition.Desktop
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
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
            name          = "Tag Build From Master Branch"
            id            = "${buildType.id}_TagStep"
            platform      = PowerShellStep.Platform.x86
            edition       = PowerShellStep.Edition.Desktop
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
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

