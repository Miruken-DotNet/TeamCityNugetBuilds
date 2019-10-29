package nuGet_Kotlin.vcsRoots

import jetbrains.buildServer.configs.kotlin.v2018_2.vcs.GitVcsRoot

object NuGet_Kotlin_TeamCityNugetBuildsGit : GitVcsRoot({
    id("NuGet_Kotlin_TeamCityNugetBuildsGit")
    uuid = "7e9ebd23-da96-49d5-b3af-b904c7fb003a"
    name = "TeamCityNugetBuilds.git"
    url  = "git@github.com:Miruken-DotNet/TeamCityNugetBuilds.git"
    authMethod = uploadedKey {
        uploadedKey = "provenstyle"
    }
})
