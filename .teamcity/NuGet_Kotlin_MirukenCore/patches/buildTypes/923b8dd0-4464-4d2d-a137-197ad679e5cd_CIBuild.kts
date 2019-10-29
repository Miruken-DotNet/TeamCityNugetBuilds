package NuGet_Kotlin_MirukenCore.patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.VisualStudioStep
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.dotnetBuild
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.nuGetInstaller
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.visualStudio
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.vstest
import jetbrains.buildServer.configs.kotlin.v2018_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with uuid = '923b8dd0-4464-4d2d-a137-197ad679e5cd_CIBuild' (id = 'NuGet_Kotlin_MirukenCore_MirukenSln_CIBuild')
accordingly, and delete the patch script.
*/
changeBuildType(uuid("923b8dd0-4464-4d2d-a137-197ad679e5cd_CIBuild")) {
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
        visualStudio {
            name = "CompileStep"
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
            path = "%Solution%"
            version = VisualStudioStep.VisualStudioVersion.vs2017
            runPlatform = VisualStudioStep.Platform.x86
            msBuildVersion = VisualStudioStep.MSBuildVersion.V15_0
            msBuildToolsVersion = VisualStudioStep.MSBuildToolsVersion.V15_0
            targets = "%BuildTargets%"
            configuration = "%BuildConfiguration%"
            platform = "Any CPU"
        }
        vstest {
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
            vstestPath = "%teamcity.dotnet.vstest.14.0%"
            includeTestFileNames = "%TestAssemblies%"
            runSettings = ""
            testCaseFilter = "%TestCaseFilter%"
            coverage = dotcover {
                toolPath = "%teamcity.tool.JetBrains.dotCover.CommandLineTools.bundled%"
            }
        }
    }
    steps {
        insert(3) {
            dotnetBuild {
                name = "Compile"
                projects = "Miruken.sln"
                configuration = "Debug"
                param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
            }
        }
    }
}
