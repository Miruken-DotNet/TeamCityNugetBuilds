package NuGet_Kotlin_Miruken_MirukenSln_MirukenValidate

import NuGet_Kotlin_Miruken_MirukenSln_MirukenValidate.buildTypes.*
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project

object Project : Project({
    uuid = "50fcdffb-6d49-43d4-a0fc-8e6dbc47a532_MirukenValidate"
    id = "NuGet_Kotlin_Miruken_MirukenSln_MirukenValidate"
    parentId = "NuGet_Kotlin_Miruken_MirukenSln_DeploymentProject"
    name = "Miruken.Validate"
    description = "Miruken.Validate nuget package"

    buildType(NuGet_Kotlin_Miruken_MirukenSln_MirukenValidate_DeployPreRelease)
    buildType(NuGet_Kotlin_Miruken_MirukenSln_MirukenValidate_DeployRelease)

    params {
        param("NuGetPackSpecFiles", """Source\Miruken.Validate\Miruken.Validate.nuspec""")
        param("PackageName", "Miruken.Validate")
    }
    buildTypesOrder = arrayListOf(NuGet_Kotlin_Miruken_MirukenSln_MirukenValidate.buildTypes.NuGet_Kotlin_Miruken_MirukenSln_MirukenValidate_DeployPreRelease, NuGet_Kotlin_Miruken_MirukenSln_MirukenValidate.buildTypes.NuGet_Kotlin_Miruken_MirukenSln_MirukenValidate_DeployRelease)
})
