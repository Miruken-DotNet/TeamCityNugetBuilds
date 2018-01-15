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
})
