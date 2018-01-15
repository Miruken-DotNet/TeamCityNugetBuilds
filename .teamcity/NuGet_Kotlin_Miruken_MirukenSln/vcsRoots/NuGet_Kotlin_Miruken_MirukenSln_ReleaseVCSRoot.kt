package NuGet_Kotlin_Miruken_MirukenSln.vcsRoots

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.vcs.GitVcsRoot

object NuGet_Kotlin_Miruken_MirukenSln_ReleaseVCSRoot : GitVcsRoot({
    uuid = "50fcdffb-6d49-43d4-a0fc-8e6dbc47a532ReleaseVcsRoot"
    id = "NuGet_Kotlin_Miruken_MirukenSln_ReleaseVCSRoot"
    name = "git@github.com:Miruken-DotNet/Miruken.git_Release"
    url = "git@github.com:Miruken-DotNet/Miruken.git"
    branch = "%DefaultBranch%"
    branchSpec = "%BranchSpecification%"
    agentCleanPolicy = GitVcsRoot.AgentCleanPolicy.ALWAYS
    authMethod = uploadedKey {
        uploadedKey = "provenstyle"
    }
})
