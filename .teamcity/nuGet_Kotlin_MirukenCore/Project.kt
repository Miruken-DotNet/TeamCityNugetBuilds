package nuGet_Kotlin_MirukenCore

import miruken.nuget.buildTemplates.NugetProject
import miruken.nuget.buildTemplates.NugetSolution
import miruken.nuget.buildTemplates.configureNugetSolutionProject
import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.Project

object Project : Project({
    id("NuGet_Kotlin_MirukenCore")
    uuid        = "a1a862e4-77a8-42e7-9e00-918f59462aef"
    parentId    = AbsoluteId("NuGet_Kotlin")
    name        = "MirukenCore"
    description = "Miruken-DotNet Core NuGet Packages"

    subProject(configureNugetSolutionProject(NugetSolution(
            guid              = "923b8dd0-4464-4d2d-a137-197ad679e5cd",
            parentId          = "NuGet_Kotlin_MirukenCore",
            id                = "NuGet_Kotlin_MirukenCore_MirukenSln",
            name              = "Miruken Solution",
            codeGithubUrl     = "git@github.com:Miruken-DotNet/Miruken.git",
            nugetApiKey       = "%MirukenNugetApiKey%",
            solutionFile      = "Miruken.sln",
            testAssemblies    = "**\\bin\\*Test*.dll",
            majorVersion      = "1",
            minorVersion      = "11",
            patchVersion      = "18",
            nugetProjects = listOf(
                    NugetProject(
                            id          = "Miruken",
                            packageName = "Miruken",
                            nuspecFile  = "Source\\Miruken\\Miruken.nuspec"),
                    NugetProject(
                            id          = "MirukenCastle",
                            packageName = "Miruken.Castle",
                            nuspecFile  = "Source\\Miruken.Castle\\Miruken.Castle.nuspec"),
                    NugetProject(
                            id          = "MirukenValidate",
                            packageName = "Miruken.Validate",
                            nuspecFile  = "Source\\Miruken.Validate\\Miruken.Validate.nuspec"),
                    NugetProject(
                            id          = "MirukenValidateCastle",
                            packageName = "Miruken.Validate.Castle",
                            nuspecFile  = "Source\\Miruken.Validate.Castle\\Miruken.Validate.Castle.nuspec"),
                    NugetProject(
                            id          = "MirukenSecure",
                            packageName = "Miruken.Secure",
                            nuspecFile  = "Source\\Miruken.Secure\\Miruken.Secure.nuspec")
            ))))

    subProject(configureNugetSolutionProject(NugetSolution(
            guid              = "0df6cd1a-93d3-4135-8f13-13b0ccdfdb97",
            parentId          = "NuGet_Kotlin_MirukenCore",
            id                = "NuGet_Kotlin_MirukenCore_MirukenMvcSln",
            name              = "Miruken.Mvc Solution",
            codeGithubUrl     = "git@github.com:Miruken-DotNet/Miruken.Mvc.git",
            nugetApiKey       = "%MirukenNugetApiKey%",
            solutionFile      = "Miruken.Mvc.sln",
            testAssemblies    = "**\\bin\\*Test*.dll",
            majorVersion      = "1",
            minorVersion      = "5",
            patchVersion      = "2",
            nugetProjects = listOf(
                    NugetProject(
                            id          = "MirukenMvc",
                            packageName = "Miruken.Mvc",
                            nuspecFile  = "Source\\Miruken.Mvc\\Miruken.Mvc.nuspec"),
                    NugetProject(
                            id          = "MirukenMvcCastle",
                            packageName = "Miruken.Mvc.Castle",
                            nuspecFile  = "Source\\Miruken.Mvc.Castle\\Miruken.Mvc.Castle.nuspec"),
                    NugetProject(
                            id          = "MirukenMvcConsole",
                            packageName = "Miruken.Mvc.Console",
                            nuspecFile  = "Source\\Miruken.Mvc.Console\\Miruken.Mvc.Console.nuspec"),
                    NugetProject(
                            id          = "MirukenMvcWpf",
                            packageName = "Miruken.Mvc.Wpf",
                            nuspecFile  = "Source\\Miruken.Mvc.Wpf\\Miruken.Mvc.Wpf.nuspec")
            ))))

    subProject(configureNugetSolutionProject(NugetSolution(
            guid              = "815fa438-ba8b-4a82-8363-13ec84f17ad0",
            parentId          = "NuGet_Kotlin_MirukenCore",
            id                = "NuGet_Kotlin_MirukenCore_MirukenMediateSln",
            name              = "Miruken.Mediate Solution",
            codeGithubUrl     = "git@github.com:Miruken-DotNet/Miruken.Mediate.git",
            nugetApiKey       = "%MirukenNugetApiKey%",
            solutionFile      = "Miruken.Mediate.sln",
            testAssemblies    = """
                                    Test\Miruken.Mediate.Tests\bin\Miruken.Mediate.Tests.dll
                                    Test\Miruken.Mediate.Castle.Tests\bin\Miruken.Mediate.Castle.Tests.dll
                                    Test\Miruken.Http.Tests\bin\Miruken.Http.Tests.dll
                                    Test\Miruken.AspNet.Castle.Tests\bin\Miruken.AspNet.Castle.Tests.dll
                                    Test\Example.Tests\bin\Example.Tests.dll
                                """.trimIndent(),
            majorVersion      = "1",
            minorVersion      = "6",
            patchVersion      = "1",
            nugetProjects = listOf(
                    NugetProject(
                            id          = "MirukenHttp",
                            packageName = "Miruken.Http",
                            nuspecFile  = "Source\\Miruken.Http\\Miruken.Http.nuspec"),
                    NugetProject(
                            id          = "MirukenAspNet",
                            packageName = "Miruken.AspNet",
                            nuspecFile  = "Source\\Miruken.AspNet\\Miruken.AspNet.nuspec"),
                    NugetProject(
                            id          = "MirukenAspNetCastle",
                            packageName = "Miruken.AspNet.Castle",
                            nuspecFile  = "Source\\Miruken.AspNet.Castle\\Miruken.AspNet.Castle.nuspec"),
                    NugetProject(
                            id          = "MirukenAspNetSwagger",
                            packageName = "Miruken.AspNet.Swagger",
                            nuspecFile  = "Source\\Miruken.AspNet.Swagger\\Miruken.AspNet.Swagger.nuspec")
            ))))

    subProject(configureNugetSolutionProject(NugetSolution(
            guid              = "2bdb0212-b5e1-4386-9ecc-7bf98eee19b0",
            parentId          = "NuGet_Kotlin_MirukenCore",
            id                = "NuGet_Kotlin_MirukenCore_MirukenMTSln",
            name              = "Miruken.MassTransit Solution",
            codeGithubUrl     = "git@github.com:Miruken-DotNet/Miruken.MassTransit.git",
            nugetApiKey       = "%MirukenNugetApiKey%",
            solutionFile      = "Miruken.MassTransit.sln",
            testAssemblies    = """
                                    Test\IntegrationTests\bin\IntegrationTests.dll
                                """.trimIndent(),
            majorVersion      = "0",
            minorVersion      = "0",
            patchVersion      = "1",
            nugetProjects = listOf(
                    NugetProject(
                            id          = "MirukenMT",
                            packageName = "Miruken.MassTransit",
                            nuspecFile  = "Source\\Miruken.MassTransit\\Miruken.MassTransit.nuspec"),
                    NugetProject(
                            id          = "MirukenMTApi",
                            packageName = "Miruken.MassTransit.Api",
                            nuspecFile  = "Source\\Miruken.MassTransit.Api\\Miruken.MassTransit.Api.nuspec")
            ))))
})
