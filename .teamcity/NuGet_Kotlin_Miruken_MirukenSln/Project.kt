package NuGet_Kotlin_Miruken_MirukenSln

import NuGet_Kotlin_Miruken_MirukenSln.buildTypes.*
import NuGet_Kotlin_Miruken_MirukenSln.vcsRoots.*
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project

object Project : Project({
    uuid = "50fcdffb-6d49-43d4-a0fc-8e6dbc47a532"
    id = "NuGet_Kotlin_Miruken_MirukenSln"
    parentId = "NuGet_Kotlin_Miruken"
    name = "Miruken Solution Kotlin"
    description = "CI/CD for Miruken.sln"

    vcsRoot(NuGet_Kotlin_Miruken_MirukenSln_CIVCSRoot)
    vcsRoot(NuGet_Kotlin_Miruken_MirukenSln_TeamCityVCSRoot)
    vcsRoot(NuGet_Kotlin_Miruken_MirukenSln_ReleaseVCSRoot)

    buildType(NuGet_Kotlin_Miruken_MirukenSln_ReleaseBuild)
    buildType(NuGet_Kotlin_Miruken_MirukenSln_CIBuild)
    buildType(NuGet_Kotlin_Miruken_MirukenSln_PreReleaseBuild)

    params {
        param("ArtifactsIn", """
            Source      => Build.zip!/Source
            packages    => Build.zip!/packages
            Miruken.sln => Build.zip!
        """.trimIndent())
        param("ArtifactsOut", """
            Build.zip!/Source   => Source
            Build.zip!/packages => packages
            Build.zip!/Miruken.sln
        """.trimIndent())
        param("MajorVersion", "1")
        param("MinorVersion", "11")
        param("PatchVersion", "18")
        param("PreReleaseProjectId", "NuGet_Kotlin_Miruken_MirukenSln_PreReleaseBuild")
        param("ReleaseProjectId", "NuGet_Kotlin_Miruken_MirukenSln_ReleaseBuild")
        param("Solution", "Miruken.sln")
        param("SolutionProjectId", "NuGet_Kotlin_Miruken_MirukenSln")
        param("TestAssemblies", """**\bin\*Test*.dll""")
    }
    buildTypesOrder = arrayListOf(NuGet_Kotlin_Miruken_MirukenSln.buildTypes.NuGet_Kotlin_Miruken_MirukenSln_CIBuild, NuGet_Kotlin_Miruken_MirukenSln.buildTypes.NuGet_Kotlin_Miruken_MirukenSln_PreReleaseBuild, NuGet_Kotlin_Miruken_MirukenSln.buildTypes.NuGet_Kotlin_Miruken_MirukenSln_ReleaseBuild)
    subProjectsOrder = arrayListOf("NuGet_Kotlin_Miruken_MirukenSln_DeploymentProject")
})
