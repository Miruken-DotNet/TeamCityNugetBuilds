package NuGet_Kotlin_MirukenCore.patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.DotnetPackStep
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.dotnetBuild
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.dotnetPack
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.dotnetTest
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.nuGetInstaller
import jetbrains.buildServer.configs.kotlin.v2018_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with uuid = '923b8dd0-4464-4d2d-a137-197ad679e5cd_PreReleaseBuild' (id = 'NuGet_Kotlin_MirukenCore_MirukenSln_PreReleaseBuild')
accordingly, and delete the patch script.
*/
changeBuildType(uuid("923b8dd0-4464-4d2d-a137-197ad679e5cd_PreReleaseBuild")) {
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
            args = "-p:Version=%DotNetAssemblyVersion% -p:DebugType=portable"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        dotnetTest {
            name = "Unit Tests"
            projects = "%Solution%"
            configuration = "%BuildConfiguration%"
            args = "--no-build"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        dotnetPack {
            name = "Pack Miruken"
            projects = "Source/Miruken/Miruken.csproj"
            configuration = "%BuildConfiguration%"
            args = "-p:PackageVersion=%PackageVersion% -p:SymbolPackageFormat=snupkg --include-symbols --no-build"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        dotnetPack {
            name = "Pack Miruken.Castle"
            projects = "Source/Miruken.Castle/Miruken.Castle.csproj"
            configuration = "%BuildConfiguration%"
            args = "-p:PackageVersion=%PackageVersion% -p:SymbolPackageFormat=snupkg --include-symbols --no-build"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        dotnetPack {
            name = "Pack Miruken.Validate"
            projects = "Source/Miruken.Validate/Miruken.Validate.csproj"
            configuration = "%BuildConfiguration%"
            args = "-p:PackageVersion=%PackageVersion% -p:SymbolPackageFormat=snupkg --include-symbols --no-build"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        dotnetPack {
            name = "Pack Miruken.Validate.Castle"
            projects = "Source/Miruken.Validate.Castle/Miruken.Validate.Castle.csproj"
            configuration = "%BuildConfiguration%"
            args = "-p:PackageVersion=%PackageVersion% -p:SymbolPackageFormat=snupkg --include-symbols --no-build"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        dotnetPack {
            name = "Pack Miruken.Secure"
            projects = "Source/Miruken.Secure/Miruken.Secure.csproj"
            configuration = "%BuildConfiguration%"
            args = "-p:PackageVersion=%PackageVersion% -p:SymbolPackageFormat=snupkg --include-symbols --no-build"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        dotnetPack {
            name = "Pack Miruken.Http"
            projects = "Source/Miruken.Http/Miruken.Http.csproj"
            configuration = "%BuildConfiguration%"
            args = "-p:PackageVersion=%PackageVersion% -p:SymbolPackageFormat=snupkg --include-symbols --no-build"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
    }
    steps {
        update<DotnetPackStep>(4) {
            enabled = false
        }
        update<DotnetPackStep>(6) {
            enabled = false
        }
    }
}