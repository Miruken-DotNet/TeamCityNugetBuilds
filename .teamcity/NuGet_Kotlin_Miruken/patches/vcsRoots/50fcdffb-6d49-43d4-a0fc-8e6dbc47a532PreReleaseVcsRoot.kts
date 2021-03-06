package NuGet_Kotlin_Miruken.patches.vcsRoots

import jetbrains.buildServer.configs.kotlin.v2018_2.ui.*
import jetbrains.buildServer.configs.kotlin.v2018_2.vcs.GitVcsRoot

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the vcsRoot with uuid = '50fcdffb-6d49-43d4-a0fc-8e6dbc47a532PreReleaseVcsRoot' (id = 'NuGet_Kotlin_Miruken_MirukenSln_PreReleaseVCSRoot')
accordingly and delete the patch script.
*/
changeVcsRoot("50fcdffb-6d49-43d4-a0fc-8e6dbc47a532PreReleaseVcsRoot") {
    val expected = GitVcsRoot({
        id("NuGet_Kotlin_Miruken_MirukenSln_PreReleaseVCSRoot")
        uuid = "50fcdffb-6d49-43d4-a0fc-8e6dbc47a532PreReleaseVcsRoot"
        name = "git@github.com:Miruken-DotNet/Miruken.git_PreRelease"
        url = "git@github.com:Miruken-DotNet/Miruken.git"
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
