package NuGet_Kotlin_Miruken.patches.projects

import jetbrains.buildServer.configs.kotlin.v2018_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the project with uuid = 'd8420070-ca13-4e71-886c-72d00f96d535' (id = 'NuGet_Kotlin_Miruken_MirukenMediateSln')
accordingly and delete the patch script.
*/
changeProject("d8420070-ca13-4e71-886c-72d00f96d535") {
    params {
        expect {
            param("MajorVersion", "1")
        }
        update {
            param("MajorVersion", "4")
        }
        expect {
            param("MinorVersion", "6")
        }
        update {
            param("MinorVersion", "0")
        }
        expect {
            param("PatchVersion", "1")
        }
        update {
            param("PatchVersion", "9")
        }
    }
}
