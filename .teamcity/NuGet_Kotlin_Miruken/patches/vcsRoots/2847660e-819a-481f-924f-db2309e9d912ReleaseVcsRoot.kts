package NuGet_Kotlin_Miruken.patches.vcsRoots

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*
import jetbrains.buildServer.configs.kotlin.v2017_2.vcs.GitVcsRoot

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the vcsRoot with uuid = '2847660e-819a-481f-924f-db2309e9d912ReleaseVcsRoot' (id = 'NuGet_Kotlin_Miruken_MirukenMvcSln_ReleaseVCSRoot')
accordingly and delete the patch script.
*/
changeVcsRoot("2847660e-819a-481f-924f-db2309e9d912ReleaseVcsRoot") {
    val expected = GitVcsRoot({
        uuid = "2847660e-819a-481f-924f-db2309e9d912ReleaseVcsRoot"
        id = "NuGet_Kotlin_Miruken_MirukenMvcSln_ReleaseVCSRoot"
        name = "git@github.com:Miruken-DotNet/Miruken.Mvc.git_Release"
        url = "git@github.com:Miruken-DotNet/Miruken.Mvc.git"
        branch = "%DefaultBranch%"
        branchSpec = "%BranchSpecification%"
        agentCleanPolicy = GitVcsRoot.AgentCleanPolicy.ALWAYS
        authMethod = uploadedKey {
            uploadedKey = "provenstyle"
        }
    })

    check(this == expected) {
        "Unexpected VCS root settings"
    }

    (this as GitVcsRoot).apply {
        authMethod = uploadedKey {
            userName = ""
            uploadedKey = "provenstyle"
            passphrase = ""
        }
    }

}