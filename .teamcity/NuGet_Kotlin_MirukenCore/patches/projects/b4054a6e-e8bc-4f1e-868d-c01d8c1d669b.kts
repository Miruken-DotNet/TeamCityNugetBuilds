package NuGet_Kotlin_MirukenCore.patches.projects

import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.Project
import jetbrains.buildServer.configs.kotlin.v2018_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the project with uuid = 'b4054a6e-e8bc-4f1e-868d-c01d8c1d669b' (id = 'NuGet_Kotlin_MirukenCore_MirukenAspNetCore')
accordingly, and delete the patch script.
*/
changeProject(uuid("b4054a6e-e8bc-4f1e-868d-c01d8c1d669b")) {
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
            param("PatchVersion", "3")
        }
    }
}
