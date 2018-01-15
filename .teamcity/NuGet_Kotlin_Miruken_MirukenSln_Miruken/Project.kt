package NuGet_Kotlin_Miruken_MirukenSln_Miruken

import NuGet_Kotlin_Miruken_MirukenSln_Miruken.buildTypes.*
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project

object Project : Project({
    uuid = "50fcdffb-6d49-43d4-a0fc-8e6dbc47a532_Miruken"
    id = "NuGet_Kotlin_Miruken_MirukenSln_Miruken"
    parentId = "NuGet_Kotlin_Miruken_MirukenSln_DeploymentProject"
    name = "Miruken"
    description = "Miruken nuget package"

    buildType(NuGet_Kotlin_Miruken_MirukenSln_Miruken_DeployPreRelease)
    buildType(NuGet_Kotlin_Miruken_MirukenSln_Miruken_DeployRelease)

    params {
        param("NuGetPackSpecFiles", """Source\Miruken\Miruken.nuspec""")
        param("PackageName", "Miruken")
    }
    buildTypesOrder = arrayListOf(NuGet_Kotlin_Miruken_MirukenSln_Miruken.buildTypes.NuGet_Kotlin_Miruken_MirukenSln_Miruken_DeployPreRelease, NuGet_Kotlin_Miruken_MirukenSln_Miruken.buildTypes.NuGet_Kotlin_Miruken_MirukenSln_Miruken_DeployRelease)
})
