package NuGet_Kotlin_MirukenCore.patches.projects

import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.Project
import jetbrains.buildServer.configs.kotlin.v2018_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the project with uuid = '923b8dd0-4464-4d2d-a137-197ad679e5cd' (id = 'NuGet_Kotlin_MirukenCore_MirukenSln')
accordingly, and delete the patch script.
*/
changeProject(uuid("923b8dd0-4464-4d2d-a137-197ad679e5cd")) {
    params {
        expect {
            param("PatchVersion", "1")
        }
        update {
            param("PatchVersion", "11")
        }
    }
}
