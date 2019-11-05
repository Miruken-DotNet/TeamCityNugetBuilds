package NuGet_Kotlin_MirukenCore.patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.DotnetTestStep
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.dotnetBuild
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.dotnetTest
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.nuGetInstaller
import jetbrains.buildServer.configs.kotlin.v2018_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with uuid = '7e62a1a9-b045-4f9c-be42-cb9a649441e1_CIBuild' (id = 'NuGet_Kotlin_MirukenCore_MirukenAspNet_CIBuild')
accordingly, and delete the patch script.
*/
changeBuildType(uuid("7e62a1a9-b045-4f9c-be42-cb9a649441e1_CIBuild")) {
    expectSteps {
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
    }
    steps {
        update<DotnetTestStep>(2) {
            enabled = false
        }
    }
}
