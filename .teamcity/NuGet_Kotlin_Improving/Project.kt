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
            guid              = "65c86971-334a-4068-b7a0-f14b15bf83a6",
            parentId          = "NuGet_Kotlin_Improving",
            id                = "NuGet_Kotlin_Improving_ImprovingAspNet",
            name              = "Improving.AspNet Solution",
            codeGithubUrl     = "git@github.com:improving/Improving.AspNet.git",
            nugetApiKey       = "%ImprovingNugetApiKey%",
            solutionFile      = "Improving.AspNet.sln",
            testAssemblies    = "Test\\Improving.AspNet.Test\\bin\\Improving.AspNet.Tests.dll",
            majorVersion      = "0",
            minorVersion      = "0",
            patchVersion      = "3",
            nugetProjects = listOf(
                    NugetProject(
                            id          = "ImprovingAspNet",
                            packageName = "Improving.AspNet",
                            nuspecFile  = "Source\\Improving.AspNet\\Improving.AspNet.nuspec")
            ))))

    subProject(configureNugetSolutionProject(NugetSolution(
            guid              = "444fa27c-654b-4804-81bb-89c1babd42ce",
            parentId          = "NuGet_Kotlin_Improving",
            id                = "NuGet_Kotlin_Improving_ImprovingDbUp",
            name              = "Improving.DbUp Solution",
            codeGithubUrl     = "git@github.com:improving/Improving.DbUp.git",
            nugetApiKey       = "%ImprovingNugetApiKey%",
            solutionFile      = "Improving.DbUp.sln",
            testAssemblies    = "Test\\Improving.DbUp.Tests\\bin\\Improving.DbUp.Tests.dll",
            majorVersion      = "1",
            minorVersion      = "0",
            patchVersion      = "4",
            nugetProjects = listOf(
                    NugetProject(
                            id          = "ImprovingDbUp",
                            packageName = "Improving.DbUp",
                            nuspecFile  = "Source\\Improving.DbUp\\Improving.DbUp.nuspec")
            ))))
})
