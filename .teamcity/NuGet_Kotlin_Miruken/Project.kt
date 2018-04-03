package NuGet_Kotlin_Miruken

import Miruken.Nuget.BuildTemplates.NugetProject
import Miruken.Nuget.BuildTemplates.NugetSolution
import Miruken.Nuget.BuildTemplates.configureNugetSolutionProject
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project

object Project : Project({
    uuid        = "eea6b38a-724a-4304-a454-3b6cfb93bfdb"
    id          = "NuGet_Kotlin_Miruken"
    parentId    = "NuGet_Kotlin"
    name        = "Miruken"
    description = "Miruken-DotNet NuGet Pakages"

    subProject(configureNugetSolutionProject(NugetSolution(
            guid              = "50fcdffb-6d49-43d4-a0fc-8e6dbc47a532",
            parentId          = "NuGet_Kotlin_Miruken",
            id                = "NuGet_Kotlin_Miruken_MirukenSln",
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
                            nuspecFile  = "Source\\Miruken.Validate.Castle\\Miruken.Validate.Castle.nuspec")
            ))))

    subProject(configureNugetSolutionProject(NugetSolution(
            guid              = "2847660e-819a-481f-924f-db2309e9d912",
            parentId          = "NuGet_Kotlin_Miruken",
            id                = "NuGet_Kotlin_Miruken_MirukenMvcSln",
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
            guid              = "d8420070-ca13-4e71-886c-72d00f96d535",
            parentId          = "NuGet_Kotlin_Miruken",
            id                = "NuGet_Kotlin_Miruken_MirukenMediateSln",
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
                            id          = "MirukenMediate",
                            packageName = "Miruken.Mediate",
                            nuspecFile  = "Source\\Miruken.Mediate\\Miruken.Mediate.nuspec"),
                    NugetProject(
                            id          = "MirukenMediateCastle",
                            packageName = "Miruken.Mediate.Castle",
                            nuspecFile  = "Source\\Miruken.Mediate.Castle\\Miruken.Mediate.Castle.nuspec"),
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
                            nuspecFile  = "Source\\Miruken.AspNet.Swagger\\Miruken.AspNet.Swagger.nuspec"),
                    NugetProject(
                            id          = "MirukenMediateApi",
                            packageName = "Miruken.Mediate.Api",
                            nuspecFile  = "Source\\Miruken.Mediate.Api\\Miruken.Mediate.Api.nuspec")
            ))))
})
