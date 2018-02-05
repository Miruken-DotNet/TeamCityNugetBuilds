package NuGet_Kotlin_Improving.patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with uuid = '65c86971-334a-4068-b7a0-f14b15bf83a6_PreReleaseBuild' (id = 'NuGet_Kotlin_Improving_ImprovingAspNet_PreReleaseBuild')
accordingly and delete the patch script.
*/
changeBuildType("65c86971-334a-4068-b7a0-f14b15bf83a6_PreReleaseBuild") {
    check(paused == false) {
        "Unexpected paused: '$paused'"
    }
    paused = true
}
