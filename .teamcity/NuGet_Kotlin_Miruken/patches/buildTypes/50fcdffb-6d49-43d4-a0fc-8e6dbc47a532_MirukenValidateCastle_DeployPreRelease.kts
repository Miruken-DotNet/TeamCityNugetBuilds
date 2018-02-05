package NuGet_Kotlin_Miruken.patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with uuid = '50fcdffb-6d49-43d4-a0fc-8e6dbc47a532_MirukenValidateCastle_DeployPreRelease' (id = 'NuGet_Kotlin_Miruken_MirukenSln_MirukenValidateCastle_DeployPreRelease')
accordingly and delete the patch script.
*/
changeBuildType("50fcdffb-6d49-43d4-a0fc-8e6dbc47a532_MirukenValidateCastle_DeployPreRelease") {
    check(paused == false) {
        "Unexpected paused: '$paused'"
    }
    paused = true
}
