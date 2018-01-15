package NuGet_Kotlin_Miruken_MirukenSln.vcsRoots

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.vcs.GitVcsRoot

object NuGet_Kotlin_Miruken_MirukenSln_TeamCityVCSRoot : GitVcsRoot({
    uuid = "50fcdffb-6d49-43d4-a0fc-8e6dbc47a532TeamCityVcsRoot"
    id = "NuGet_Kotlin_Miruken_MirukenSln_TeamCityVCSRoot"
    name = "git@github.com:Miruken-DotNet/MirukenKotlinBuild.git_TeamCity"
    url = "git@github.com:Miruken-DotNet/MirukenKotlinBuild.git"
    authMethod = uploadedKey {
        uploadedKey = "provenstyle"
    }
})
