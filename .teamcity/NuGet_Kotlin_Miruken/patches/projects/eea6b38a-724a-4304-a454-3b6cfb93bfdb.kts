package NuGet_Kotlin_Miruken.patches.projects

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the project with uuid = 'eea6b38a-724a-4304-a454-3b6cfb93bfdb' (id = 'NuGet_Kotlin_Miruken')
accordingly and delete the patch script.
*/
changeProject("eea6b38a-724a-4304-a454-3b6cfb93bfdb") {
    params {
        add {
            param("NugetApiKey", "%MirukenNugetApiKey%")
        }
    }
}