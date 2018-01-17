package NuGet_Kotlin_Improving

import Miruken.Nuget.BuildTemplates.NugetProject
import Miruken.Nuget.BuildTemplates.NugetSolution
import Miruken.Nuget.BuildTemplates.configureNugetSolutionProject
import jetbrains.buildServer.configs.kotlin.v2017_2.Project

object Project : Project({
    uuid        = "2380920a-f24b-410f-b86a-ebb59f87ac22"
    id          = "NuGet_Kotlin_Improving"
    parentId    = "NuGet_Kotlin"
    name        = "Improving"
    description = "Improving Nugets"

    subProject(configureNugetSolutionProject(NugetSolution(
            guid              = "65c86971-334a-4068-b7a0-f14b15bf83a6", // stopped here
            parentId          = "NuGet_Kotlin_Improving",
            id                = "NuGet_Kotlin_Improving_ImprovingAspNet",
            name              = "Improving.AspNet Solution",
            codeGithubUrl     = "git@github.com:improving/Improving.AspNet.git",
            nugetApiKey       = "%ImprovingNugetApiKey%",
            solutionFile      = "Improving.AspNet.sln",
            testAssemblies    = "Improving.AspNet.Test\\bin\\Improving.AspNet.dll",
            majorVersion      = "0",
            minorVersion      = "0",
            patchVersion      = "3",
            nugetProjects = listOf(
                    NugetProject(
                            id          = "ImprovingAspNet",
                            packageName = "Improving.AspNet",
                            nuspecFile  = "Improving.AspNet\\Improving.AspNet.nuspec")
            ))))
})
