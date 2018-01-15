package NuGet_Kotlin_Miruken

import Miruken.Nuget.BuildTemplates.NugetProject
import Miruken.Nuget.BuildTemplates.NugetSolution
import Miruken.Nuget.BuildTemplates.configureNugetSolutionProject
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project

object Project : Project({
    uuid = "eea6b38a-724a-4304-a454-3b6cfb93bfdb"
    id = "NuGet_Kotlin_Miruken"
    parentId = "NuGet_Kotlin"
    name = "Miruken"
    description = "Miruken-DotNet NuGet Pakages"

    subProject(configureNugetSolutionProject(NugetSolution(
            guid = "2f710b99-d945-4072-b5dd-463fac99b976",
            parentId = "NuGet_MirukenDotNet",
            id = "NuGet_MirukenDotNet_MirukenSolutionKotlin",
            name = "Miruken Solution Kotlin",
            codeGithubUrl = "git@github.com:Miruken-DotNet/Miruken.git",
            teamCityGithubUrl = "git@github.com:Miruken-DotNet/MirukenKotlinBuild.git",
            solutionFile = "Miruken.sln",
            testAssemblies = "**\\bin\\*Test*.dll",
            nugetProjects = listOf(
                    NugetProject(
                            id = "Miruken",
                            packageName = "Miruken",
                            nuspecFile = "Source\\Miruken\\Miruken.nuspec"),
                    NugetProject(
                            id = "MirukenCastle",
                            packageName = "Miruken.Castle",
                            nuspecFile = "Source\\Miruken.Castle\\Miruken.Castle.nuspec"),
                    NugetProject(
                            id = "MirukenValidate",
                            packageName = "Miruken.Validate",
                            nuspecFile = "Source\\Miruken.Validate\\Miruken.Validate.nuspec"),
                    NugetProject(
                            id = "MirukenValidateCastle",
                            packageName = "Miruken.Validate.Castle",
                            nuspecFile = "Source\\Miruken.Validate.Castle\\Miruken.Validate.Castle.nuspec")
            ))))
})
