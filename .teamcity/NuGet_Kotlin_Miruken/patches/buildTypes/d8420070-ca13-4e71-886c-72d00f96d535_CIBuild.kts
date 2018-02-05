package NuGet_Kotlin_Miruken.patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with uuid = 'd8420070-ca13-4e71-886c-72d00f96d535_CIBuild' (id = 'NuGet_Kotlin_Miruken_MirukenMediateSln_CIBuild')
accordingly and delete the patch script.
*/
changeBuildType("d8420070-ca13-4e71-886c-72d00f96d535_CIBuild") {
    check(paused == false) {
        "Unexpected paused: '$paused'"
    }
    paused = true
}
