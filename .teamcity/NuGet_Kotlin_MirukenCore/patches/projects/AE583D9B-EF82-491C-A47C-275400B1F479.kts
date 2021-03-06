package NuGet_Kotlin_MirukenCore.patches.projects

import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.Project
import jetbrains.buildServer.configs.kotlin.v2018_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the project with uuid = 'AE583D9B-EF82-491C-A47C-275400B1F479' (id = 'NuGet_Kotlin_MirukenCore_MirukenEFSln')
accordingly, and delete the patch script.
*/
changeProject(uuid("AE583D9B-EF82-491C-A47C-275400B1F479")) {
    params {
        expect {
            param("PatchVersion", "1")
        }
        update {
            param("PatchVersion", "8")
        }
    }
}
