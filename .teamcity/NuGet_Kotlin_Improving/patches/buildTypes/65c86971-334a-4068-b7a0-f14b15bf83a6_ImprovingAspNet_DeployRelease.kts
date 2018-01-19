package NuGet_Kotlin_Improving.patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.BuildStep
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with uuid = '65c86971-334a-4068-b7a0-f14b15bf83a6_ImprovingAspNet_DeployRelease' (id = 'NuGet_Kotlin_Improving_ImprovingAspNet_ImprovingAspNet_DeployRelease')
accordingly and delete the patch script.
*/
changeBuildType("65c86971-334a-4068-b7a0-f14b15bf83a6_ImprovingAspNet_DeployRelease") {
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
            param("secure:nuget.api.key", "zxx2e3163a9797ccbf3894c3a62d6d95c15")
            param("toolPathSelector", "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
        }
    }
    steps {
        update<BuildStep>(1) {
            param("secure:nuget.api.key", "credentialsJSON:c17afa04-30a4-410b-ac55-f2d0fc02578d")
        }
        insert(2) {
            step {
                name = "Nuget Publish to NuGet.org (1)"
                type = "jb.nuget.publish"
                param("secure:nuget.api.key", "credentialsJSON:c17afa04-30a4-410b-ac55-f2d0fc02578d")
                param("nuget.path", "%teamcity.tool.NuGet.CommandLine.DEFAULT%")
                param("nuget.publish.source", "https://nuget.smbsrc.net/")
                param("nuget.push.commandline", "-source")
                param("nuget.publish.files", "nupkg/%NupkgSymbolsName%")
            }
        }
    }
}
