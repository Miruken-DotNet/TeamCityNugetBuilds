package NuGet_Kotlin_Miruken.patches.projects

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the project with uuid = '50fcdffb-6d49-43d4-a0fc-8e6dbc47a532' (id = 'NuGet_Kotlin_Miruken_MirukenSln')
accordingly and delete the patch script.
*/
changeProject("50fcdffb-6d49-43d4-a0fc-8e6dbc47a532") {
    params {
        expect {
            param("MajorVersion", "1")
        }
        update {
            param("MajorVersion", "2")
        }
        expect {
            param("MinorVersion", "11")
        }
        update {
            param("MinorVersion", "1")
        }
        expect {
            param("PatchVersion", "18")
        }
        update {
            param("PatchVersion", "4")
        }
    }
}
