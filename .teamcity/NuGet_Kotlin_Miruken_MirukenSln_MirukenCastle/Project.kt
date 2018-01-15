package NuGet_Kotlin_Miruken_MirukenSln_MirukenCastle

import NuGet_Kotlin_Miruken_MirukenSln_MirukenCastle.buildTypes.*
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project

object Project : Project({
    uuid = "50fcdffb-6d49-43d4-a0fc-8e6dbc47a532_MirukenCastle"
    id = "NuGet_Kotlin_Miruken_MirukenSln_MirukenCastle"
    parentId = "NuGet_Kotlin_Miruken_MirukenSln_DeploymentProject"
    name = "Miruken.Castle"
    description = "Miruken.Castle nuget package"

    buildType(NuGet_Kotlin_Miruken_MirukenSln_MirukenCastle_DeployPreRelease)
    buildType(NuGet_Kotlin_Miruken_MirukenSln_MirukenCastle_DeployRelease)

    params {
        param("NuGetPackSpecFiles", """Source\Miruken.Castle\Miruken.Castle.nuspec""")
        param("PackageName", "Miruken.Castle")
    }
    buildTypesOrder = arrayListOf(NuGet_Kotlin_Miruken_MirukenSln_MirukenCastle.buildTypes.NuGet_Kotlin_Miruken_MirukenSln_MirukenCastle_DeployPreRelease, NuGet_Kotlin_Miruken_MirukenSln_MirukenCastle.buildTypes.NuGet_Kotlin_Miruken_MirukenSln_MirukenCastle_DeployRelease)
})
