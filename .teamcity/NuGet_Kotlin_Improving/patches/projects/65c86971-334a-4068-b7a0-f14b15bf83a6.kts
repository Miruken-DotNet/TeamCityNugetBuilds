package NuGet_Kotlin_Improving.patches.projects

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the project with uuid = '65c86971-334a-4068-b7a0-f14b15bf83a6' (id = 'NuGet_Kotlin_Improving_ImprovingAspNet')
accordingly and delete the patch script.
*/
changeProject("65c86971-334a-4068-b7a0-f14b15bf83a6") {
    params {
        expect {
            param("PatchVersion", "3")
        }
        update {
            param("PatchVersion", "15")
        }
    }
}
