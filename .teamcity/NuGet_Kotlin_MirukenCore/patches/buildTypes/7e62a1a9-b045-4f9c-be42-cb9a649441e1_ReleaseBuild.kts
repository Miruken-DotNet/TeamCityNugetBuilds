package NuGet_Kotlin_MirukenCore.patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.DotnetTestStep
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.PowerShellStep
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.dotnetBuild
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.dotnetPack
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.dotnetTest
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.nuGetInstaller
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.powerShell
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2018_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with uuid = '7e62a1a9-b045-4f9c-be42-cb9a649441e1_ReleaseBuild' (id = 'NuGet_Kotlin_MirukenCore_MirukenAspNet_ReleaseBuild')
accordingly, and delete the patch script.
*/
changeBuildType(uuid("7e62a1a9-b045-4f9c-be42-cb9a649441e1_ReleaseBuild")) {
    expectSteps {
        powerShell {
            name = "Check For PreRelease Dependency"
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
            formatStderrAsError = true
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
        nuGetInstaller {
            name = "Restore NuGet Packages"
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
            toolPath = "%teamcity.tool.NuGet.CommandLine.DEFAULT%"
            projects = "%Solution%"
            sources = "%PackageSources%"
            param("nuget.updatePackages.mode", "sln")
            param("toolPathSelector", "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
        }
        dotnetBuild {
            name = "Compile"
            projects = "%Solution%"
            configuration = "%BuildConfiguration%"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        dotnetTest {
            name = "Unit Tests"
            projects = "%Solution%"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        dotnetPack {
            name = ".NET CORE (pack)"
            projects = "%Solution%"
            args = "-p:PackageVersion=%PackageVersion% -p:DebugSymbols=true -p:DebugType=pdbonly -p:Version=%DotNetAssemblyVersion% --include-symbols --include-source"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        powerShell {
            name = "Tag Build From Master Branch"
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
            platform = PowerShellStep.Platform.x86
            edition = PowerShellStep.Edition.Desktop
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
        powerShell {
            name = "Increment PatchVersion And Reset Build Counters"
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
            platform = PowerShellStep.Platform.x86
            edition = PowerShellStep.Edition.Desktop
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
    steps {
        update<DotnetTestStep>(3) {
            enabled = false
        }
        insert(4) {
            script {
                name = "temp: run tests with command line"
                scriptContent = "dotnet test %Solution%"
            }
        }
    }
}
