package NuGet_Kotlin_Miruken_MirukenSln_MirukenValidateCastle

import NuGet_Kotlin_Miruken_MirukenSln_MirukenValidateCastle.buildTypes.*
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project

object Project : Project({
    uuid = "50fcdffb-6d49-43d4-a0fc-8e6dbc47a532_MirukenValidateCastle"
    id = "NuGet_Kotlin_Miruken_MirukenSln_MirukenValidateCastle"
    parentId = "NuGet_Kotlin_Miruken_MirukenSln_DeploymentProject"
    name = "Miruken.Validate.Castle"
    description = "Miruken.Validate.Castle nuget package"

    buildType(NuGet_Kotlin_Miruken_MirukenSln_MirukenValidateCastle_DeployPreRelease)
    buildType(NuGet_Kotlin_Miruken_MirukenSln_MirukenValidateCastle_DeployRelease)

    params {
        param("NuGetPackSpecFiles", """Source\Miruken.Validate.Castle\Miruken.Validate.Castle.nuspec""")
        param("PackageName", "Miruken.Validate.Castle")
    }
    buildTypesOrder = arrayListOf(NuGet_Kotlin_Miruken_MirukenSln_MirukenValidateCastle.buildTypes.NuGet_Kotlin_Miruken_MirukenSln_MirukenValidateCastle_DeployPreRelease, NuGet_Kotlin_Miruken_MirukenSln_MirukenValidateCastle.buildTypes.NuGet_Kotlin_Miruken_MirukenSln_MirukenValidateCastle_DeployRelease)
})
