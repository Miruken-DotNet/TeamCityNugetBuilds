package NuGet_Kotlin_Improving.patches.projects

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the project with uuid = 'dcc81349-f028-45f7-acbc-8cfb7a90d940' (id = 'NuGet_Kotlin_Improving_ImprovingMediatR')
accordingly and delete the patch script.
*/
changeProject("dcc81349-f028-45f7-acbc-8cfb7a90d940") {
    params {
        expect {
            param("PatchVersion", "3")
        }
        update {
            param("PatchVersion", "4")
        }
    }
}
