package nuGet_Kotlin

import nuGet_Kotlin.vcsRoots.NuGet_Kotlin_TeamCityNugetBuildsGit
import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.Project
import jetbrains.buildServer.configs.kotlin.v2018_2.projectFeatures.VersionedSettings
import jetbrains.buildServer.configs.kotlin.v2018_2.projectFeatures.versionedSettings

object Project : Project({
    id("NuGet_Kotlin")
    uuid        = "2326bc66-267d-4f5e-b4db-fb11f69453dd"
    parentId    = AbsoluteId("NuGet")
    name        = "Configured with Kotlin DSL"
    description = ""

    vcsRoot(NuGet_Kotlin_TeamCityNugetBuildsGit)

    features {
        versionedSettings {
            id = "PROJECT_EXT_2"
            mode = VersionedSettings.Mode.ENABLED
            buildSettingsMode = VersionedSettings.BuildSettingsMode.USE_CURRENT_SETTINGS
            rootExtId = NuGet_Kotlin_TeamCityNugetBuildsGit.id?.value
            showChanges = false
            settingsFormat = VersionedSettings.Format.KOTLIN
            storeSecureParamsOutsideOfVcs = true
        }
    }
})
