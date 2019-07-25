package NuGet_Kotlin_Miruken.patches.projects

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the project with uuid = '2847660e-819a-481f-924f-db2309e9d912' (id = 'NuGet_Kotlin_Miruken_MirukenMvcSln')
accordingly and delete the patch script.
*/
changeProject("2847660e-819a-481f-924f-db2309e9d912") {
    params {
        expect {
            param("MajorVersion", "1")
        }
        update {
            param("MajorVersion", "2")
        }
        expect {
            param("MinorVersion", "5")
        }
        update {
            param("MinorVersion", "0")
        }
        expect {
            param("PatchVersion", "2")
        }
        update {
            param("PatchVersion", "4")
        }
    }
}
