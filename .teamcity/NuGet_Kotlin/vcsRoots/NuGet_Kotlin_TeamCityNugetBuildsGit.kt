package NuGet_Kotlin.vcsRoots

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.vcs.GitVcsRoot

object NuGet_Kotlin_TeamCityNugetBuildsGit : GitVcsRoot({
    uuid = "7e9ebd23-da96-49d5-b3af-b904c7fb003a"
    id = "NuGet_Kotlin_TeamCityNugetBuildsGit"
    name = "TeamCityNugetBuilds.git"
    url = "git@github.com:Miruken-DotNet/TeamCityNugetBuilds.git"
    authMethod = uploadedKey {
        uploadedKey = "provenstyle"
    }
})
