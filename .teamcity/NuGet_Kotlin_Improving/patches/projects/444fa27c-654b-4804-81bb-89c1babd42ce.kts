package NuGet_Kotlin_Improving.patches.projects

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the project with uuid = '444fa27c-654b-4804-81bb-89c1babd42ce' (id = 'NuGet_Kotlin_Improving_ImprovingDbUp')
accordingly and delete the patch script.
*/
changeProject("444fa27c-654b-4804-81bb-89c1babd42ce") {
    params {
        expect {
            param("PatchVersion", "4")
        }
        update {
            param("PatchVersion", "7")
        }
    }
}
