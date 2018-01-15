package NuGet_Kotlin_Miruken_MirukenSln_DeploymentProject

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project

object Project : Project({
    uuid = "50fcdffb-6d49-43d4-a0fc-8e6dbc47a532_DeploymentProject"
    id = "NuGet_Kotlin_Miruken_MirukenSln_DeploymentProject"
    parentId = "NuGet_Kotlin_Miruken_MirukenSln"
    name = "Deployment"

    params {
        param("SHA", "")
    }
})
