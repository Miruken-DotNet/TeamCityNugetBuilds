package NuGet_Kotlin

import NuGet_Kotlin.vcsRoots.*
import NuGet_Kotlin.vcsRoots.NuGet_Kotlin_TeamCityNugetBuildsGit
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.projectFeatures.VersionedSettings
import jetbrains.buildServer.configs.kotlin.v2017_2.projectFeatures.versionedSettings

object Project : Project({
    uuid = "2326bc66-267d-4f5e-b4db-fb11f69453dd"
    id = "NuGet_Kotlin"
    parentId = "NuGet"
    name = "kotlin"
    description = "Build configured with Kotlin"

    vcsRoot(NuGet_Kotlin_TeamCityNugetBuildsGit)

    features {
        versionedSettings {
            id = "PROJECT_EXT_2"
            mode = VersionedSettings.Mode.ENABLED
            buildSettingsMode = VersionedSettings.BuildSettingsMode.USE_CURRENT_SETTINGS
            rootExtId = NuGet_Kotlin_TeamCityNugetBuildsGit.id
            showChanges = false
            settingsFormat = VersionedSettings.Format.KOTLIN
            storeSecureParamsOutsideOfVcs = true
        }
    }
})
