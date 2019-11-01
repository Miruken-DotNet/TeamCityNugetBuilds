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
            testAssemblies    = "**\\bin\\*Test*.dll",
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
            majorVersion      = "1",
            minorVersion      = "99",
            patchVersion      = "1",
            nugetProjects = listOf(
                    NugetProject(
                            id          = "MirukenMT",
                            packageName = "Miruken.MassTransit"),
                    NugetProject(
                            id          = "MirukenMTApi",
                            packageName = "Miruken.MassTransit.Api")
            ))))
})
