package NuGet_Kotlin_Miruken.patches.vcsRoots

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*
import jetbrains.buildServer.configs.kotlin.v2017_2.vcs.GitVcsRoot

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the vcsRoot with uuid = 'd8420070-ca13-4e71-886c-72d00f96d535CIVcsRoot' (id = 'NuGet_Kotlin_Miruken_MirukenMediateSln_CIVCSRoot')
accordingly and delete the patch script.
*/
changeVcsRoot("d8420070-ca13-4e71-886c-72d00f96d535CIVcsRoot") {
    val expected = GitVcsRoot({
        uuid = "d8420070-ca13-4e71-886c-72d00f96d535CIVcsRoot"
        id = "NuGet_Kotlin_Miruken_MirukenMediateSln_CIVCSRoot"
        name = "git@github.com:Miruken-DotNet/Miruken.Mediate.git_CI"
        url = "git@github.com:Miruken-DotNet/Miruken.Mediate.git"
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