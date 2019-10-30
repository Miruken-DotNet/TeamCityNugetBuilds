package NuGet_Kotlin_Improving

import miruken.nuget.buildTemplates.NugetProject
import miruken.nuget.buildTemplates.NugetSolution
import jetbrains.buildServer.configs.kotlin.v2018_2.AbsoluteId
import jetbrains.buildServer.configs.kotlin.v2018_2.Project
import miruken.nuget.buildTemplates.FullFramework

object Project : Project({
    id("NuGet_Kotlin_Improving")
    uuid        = "2380920a-f24b-410f-b86a-ebb59f87ac22"
    parentId    = AbsoluteId("NuGet_Kotlin")
    name        = "Improving"
    description = "Improving Nugets"

    subProject(FullFramework.configureNugetSolutionProject(NugetSolution(
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

    subProject(FullFramework.configureNugetSolutionProject(NugetSolution(
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
                            nuspecFile  = "Source\\Improving.DbUp\\Improving.DbUp.nuspec"),
                    NugetProject(
                            id          = "ImprovingDbUpQuickStart",
                            packageName = "Improving.DbUp.QuickStart",
                            nuspecFile  = "Source\\Improving.DbUp.QuickStart\\Improving.DbUp.QuickStart.nuspec")
            ))))

    subProject(FullFramework.configureNugetSolutionProject(NugetSolution(
            guid              = "1852e351-879e-426d-9bb7-299060589c6f",
            parentId          = "NuGet_Kotlin_Improving",
            id                = "NuGet_Kotlin_Improving_ImpHghwyDtScp",
            name              = "Improving.Highway.Data.Scope Solution",
            codeGithubUrl     = "git@github.com:improving/Improving.Highway.Data.Scope.git",
            nugetApiKey       = "%ImprovingNugetApiKey%",
            solutionFile      = "Improving.Highway.Data.Scope.sln",
            testAssemblies    = "Test\\Improving.Highway.Data.Scope.Test\\bin\\Improving.Highway.Data.Scope.Test.dll",
            majorVersion      = "2",
            minorVersion      = "0",
            patchVersion      = "0",
            nugetProjects = listOf(
                    NugetProject(
                            id          = "ImprovingHighwayDataScope",
                            packageName = "Improving.Highway.Data.Scope",
                            nuspecFile  = "Source\\Improving.Highway.Data.Scope\\Improving.Highway.Data.Scope.nuspec")
            ))))

    subProject(FullFramework.configureNugetSolutionProject(NugetSolution(
            guid              = "dcc81349-f028-45f7-acbc-8cfb7a90d940",
            parentId          = "NuGet_Kotlin_Improving",
            id                = "NuGet_Kotlin_Improving_ImprovingMediatR",
            name              = "Improving.MediatR Solution",
            codeGithubUrl     = "git@github.com:improving/Improving.MediatR.git",
            nugetApiKey       = "%ImprovingNugetApiKey%",
            solutionFile      = "Improving.MediatR.sln",
            testAssemblies    = "Test\\Improving.MediatR.Test\\bin\\Improving.MediatR.Tests.dll",
            majorVersion      = "3",
            minorVersion      = "0",
            patchVersion      = "3",
            nugetProjects = listOf(
                    NugetProject(
                            id          = "ImprovingMediatR",
                            packageName = "Improving.MediatR",
                            nuspecFile  = "Source\\Improving.Mediatr\\Improving.MediatR.nuspec")
            ))))
})
