package NuGet_Kotlin_Miruken.patches.projects

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the project with uuid = '18499CBF-CC08-47D3-92D5-E4B9C09BD7B1' (id = 'NuGet_Kotlin_Miruken_MirukenMTSln')
accordingly and delete the patch script.
*/
changeProject("18499CBF-CC08-47D3-92D5-E4B9C09BD7B1") {
    params {
        expect {
            param("MajorVersion", "0")
        }
        update {
            param("MajorVersion", "1")
        }
        expect {
            param("PatchVersion", "1")
        }
        update {
            param("PatchVersion", "2")
        }
    }
}