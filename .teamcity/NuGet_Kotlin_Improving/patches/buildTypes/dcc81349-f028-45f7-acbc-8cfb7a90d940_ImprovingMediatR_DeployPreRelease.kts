package NuGet_Kotlin_Improving.patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with uuid = 'dcc81349-f028-45f7-acbc-8cfb7a90d940_ImprovingMediatR_DeployPreRelease' (id = 'NuGet_Kotlin_Improving_ImprovingMediatR_ImprovingMediatR_DeployPreRelease')
accordingly and delete the patch script.
*/
changeBuildType("dcc81349-f028-45f7-acbc-8cfb7a90d940_ImprovingMediatR_DeployPreRelease") {
    check(paused == false) {
        "Unexpected paused: '$paused'"
    }
    paused = true
}
