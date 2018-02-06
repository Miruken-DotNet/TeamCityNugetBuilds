package NuGet_Kotlin_Miruken.patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.BuildStep
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with uuid = '50fcdffb-6d49-43d4-a0fc-8e6dbc47a532_Miruken_DeployRelease' (id = 'NuGet_Kotlin_Miruken_MirukenSln_Miruken_DeployRelease')
accordingly and delete the patch script.
*/
changeBuildType("50fcdffb-6d49-43d4-a0fc-8e6dbc47a532_Miruken_DeployRelease") {
    expectSteps {
        step {
            name = "NuGet Pack for NuGet.org"
            type = "jb.nuget.pack"
            param("nuget.pack.include.sources", "true")
            param("nuget.pack.output.clean", "true")
            param("nuget.pack.output.directory", "nupkg")
            param("nuget.pack.prefer.project", "true")
            param("nuget.pack.specFile", "%NuGetPackSpecFiles%")
            param("nuget.pack.version", "%PackageVersion%")
            param("nuget.path", "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
            param("toolPathSelector", "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
        }
        step {
            name = "Nuget Publish to NuGet.org"
            type = "jb.nuget.publish"
            param("nuget.path", "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
            param("nuget.publish.files", "nupkg/%NupkgName%")
            param("nuget.publish.source", "nuget.org")
            param("secure:nuget.api.key", "%MirukenNugetApiKey%")
            param("toolPathSelector", "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
        }
        step {
            name = "Nuget Publish to SymbolSource.org"
            type = "jb.nuget.publish"
            param("nuget.path", "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
            param("nuget.publish.files", "nupkg/%NupkgSymbolsName%")
            param("nuget.publish.source", "https://nuget.smbsrc.net/")
            param("secure:nuget.api.key", "%MirukenNugetApiKey%")
        }
    }
    steps {
        update<BuildStep>(1) {
            param("secure:nuget.api.key", "credentialsJSON:8482549d-f8f4-4ae0-874b-4f562c9acff1")
        }
        update<BuildStep>(2) {
            param("secure:nuget.api.key", "credentialsJSON:8482549d-f8f4-4ae0-874b-4f562c9acff1")
        }
    }
}
