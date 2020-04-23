package NuGet_Kotlin_MirukenCore

import miruken.nuget.buildTemplates.NugetProject
import miruken.nuget.buildTemplates.NugetSolution
import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.Project
import miruken.nuget.buildTemplates.CoreFramework

object Project : Project({
    id("NuGet_Kotlin_MirukenCore")
    uuid        = "a1a862e4-77a8-42e7-9e00-918f59462aef"
    parentId    = AbsoluteId("NuGet_Kotlin")
    name        = "MirukenCore"
    description = "Miruken-DotNet Core NuGet Packages"

    subProject(CoreFramework.configureNugetSolutionProject(NugetSolution(
            guid              = "923b8dd0-4464-4d2d-a137-197ad679e5cd",
            parentId          = "NuGet_Kotlin_MirukenCore",
            id                = "NuGet_Kotlin_MirukenCore_MirukenSln",
            name              = "Miruken Solution",
            codeGithubUrl     = "git@github.com:Miruken-DotNet/Miruken.git",
            nugetApiKey       = "%MirukenNugetApiKey%",
            solutionFile      = "Miruken.sln",
            majorVersion      = "3",
            minorVersion      = "99",
            patchVersion      = "1",
            nugetProjects = listOf(
                    NugetProject(
                            id          = "Miruken",
                            packageName = "Miruken"),
                    NugetProject(
                            id          = "MirukenCastle",
                            packageName = "Miruken.Castle"),
                    NugetProject(
                            id          = "MirukenValidate",
                            packageName = "Miruken.Validate"),
                    NugetProject(
                            id          = "MirukenValidateCastle",
                            packageName = "Miruken.Validate.Castle"),
                    NugetProject(
                            id          = "MirukenSecure",
                            packageName = "Miruken.Secure"),
                    NugetProject(
                            id          = "MirukenHttp",
                            packageName = "Miruken.Http")
            ))))

    subProject(CoreFramework.configureNugetSolutionProject(NugetSolution(
            guid              = "7e62a1a9-b045-4f9c-be42-cb9a649441e1",
            parentId          = "NuGet_Kotlin_MirukenCore",
            id                = "NuGet_Kotlin_MirukenCore_MirukenAspNet",
            name              = "Miruken.AspNet Solution",
            codeGithubUrl     = "git@github.com:Miruken-DotNet/Miruken.AspNet.git",
            nugetApiKey       = "%MirukenNugetApiKey%",
            solutionFile      = "Miruken.AspNet.sln",
            majorVersion      = "4",
            minorVersion      = "99",
            patchVersion      = "1",
            nugetProjects = listOf(
                    NugetProject(
                            id          = "MirukenAspNet",
                            packageName = "Miruken.AspNet"),
                    NugetProject(
                            id          = "MirukenAspNetCastle",
                            packageName = "Miruken.AspNet.Castle"),
                    NugetProject(
                            id          = "MirukenAspNetSwagger",
                            packageName = "Miruken.AspNet.Swagger")
            ))))

    subProject(CoreFramework.configureNugetSolutionProject(NugetSolution(
            guid              = "b4054a6e-e8bc-4f1e-868d-c01d8c1d669b",
            parentId          = "NuGet_Kotlin_MirukenCore",
            id                = "NuGet_Kotlin_MirukenCore_MirukenAspNetCore",
            name              = "Miruken.AspNetCore Solution",
            codeGithubUrl     = "git@github.com:Miruken-DotNet/Miruken.AspNetCore.git",
            nugetApiKey       = "%MirukenNugetApiKey%",
            solutionFile      = "Miruken.AspNetCore.sln",
            majorVersion      = "0",
            minorVersion      = "0",
            patchVersion      = "1",
            nugetProjects = listOf(
                    NugetProject(
                            id          = "MirukenAspNetCore",
                            packageName = "Miruken.AspNetCore"),
                    NugetProject(
                            id          = "MirukenAspNetCoreSwagger",
                            packageName = "Miruken.AspNetCore.Swagger"),
                    NugetProject(
                            id          = "MirukenAspNCoreSigRApi",
                            packageName = "Miruken.AspNetCore.SignalR.Api")
            ))))

    subProject(CoreFramework.configureNugetSolutionProject(NugetSolution(
            guid              = "3e1f42a7-a23b-4190-90c4-1eb972e99dc1",
            parentId          = "NuGet_Kotlin_MirukenCore",
            id                = "NuGet_Kotlin_MirukenCore_MirukenMTSln",
            name              = "Miruken.MassTransit Solution",
            codeGithubUrl     = "git@github.com:Miruken-DotNet/Miruken.MassTransit.git",
            nugetApiKey       = "%MirukenNugetApiKey%",
            solutionFile      = "Miruken.MassTransit.sln",
            majorVersion      = "0",
            minorVersion      = "0",
            patchVersion      = "1",
            nugetProjects = listOf(
                    NugetProject(
                            id          = "MirukenMT",
                            packageName = "Miruken.MassTransit"),
                    NugetProject(
                            id          = "MirukenMTApi",
                            packageName = "Miruken.MassTransit.Api")
            ))))

    subProject(CoreFramework.configureNugetSolutionProject(NugetSolution(
            guid              = "AE583D9B-EF82-491C-A47C-275400B1F479",
            parentId          = "NuGet_Kotlin_MirukenCore",
            id                = "NuGet_Kotlin_MirukenCore_MirukenEFSln",
            name              = "Miruken.EntityFramework Solution",
            codeGithubUrl     = "git@github.com:Miruken-DotNet/Miruken.EntityFramework.git",
            nugetApiKey       = "%MirukenNugetApiKey%",
            solutionFile      = "Miruken.EntityFramework.sln",
            majorVersion      = "0",
            minorVersion      = "0",
            patchVersion      = "1",
            nugetProjects = listOf(
                    NugetProject(
                            id          = "MirukenET",
                            packageName = "Miruken.EntityFramework")
            ))))

    subProject(CoreFramework.configureNugetSolutionProject(NugetSolution(
            guid              = "94b7f965-6df7-4b65-a2e2-82c47836991f",
            parentId          = "NuGet_Kotlin_MirukenCore",
            id                = "NuGet_Kotlin_MirukenCore_MirukenQuartzSln",
            name              = "Miruken.Quartz Solution",
            codeGithubUrl     = "git@github.com:Miruken-DotNet/Miruken.Quartz.git",
            nugetApiKey       = "%MirukenNugetApiKey%",
            solutionFile      = "Miruken.Quartz.sln",
            majorVersion      = "0",
            minorVersion      = "0",
            patchVersion      = "1",
            nugetProjects = listOf(
                    NugetProject(
                            id          = "MirukenQuartz",
                            packageName = "Miruken.Quartz")
            ))))
})
