package NuGet_Kotlin_MirukenCore.patches.projects

import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.Project
import jetbrains.buildServer.configs.kotlin.v2018_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the project with uuid = '3e1f42a7-a23b-4190-90c4-1eb972e99dc1' (id = 'NuGet_Kotlin_MirukenCore_MirukenMTSln')
accordingly, and delete the patch script.
*/
changeProject(uuid("3e1f42a7-a23b-4190-90c4-1eb972e99dc1")) {
    params {
        expect {
            param("MajorVersion", "0")
        }
        update {
            param("MajorVersion", "2")
        }
    }
}
