package miruken.nuget.buildTemplates

import jetbrains.buildServer.configs.kotlin.v2018_2.vcs.GitVcsRoot

fun ciVcsRoot(solution: NugetSolution) : GitVcsRoot = GitVcsRoot {
    id(solution.ciVcsRootId)
    uuid             = "${solution.guid}CIVcsRoot"
    name             = "${solution.codeGithubUrl}_CI"
    url              = solution.codeGithubUrl
    branch           = "%DefaultBranch%"
    branchSpec       = "%BranchSpecification%"
    agentCleanPolicy = GitVcsRoot.AgentCleanPolicy.ALWAYS
    authMethod = uploadedKey {
        uploadedKey = "provenstyle"
    }
}

fun preReleaseVcsRoot(solution: NugetSolution) : GitVcsRoot = GitVcsRoot {
    id(solution.preReleaseVcsRootId)
    uuid             = "${solution.guid}PreReleaseVcsRoot"
    name             = "${solution.codeGithubUrl}_PreRelease"
    url              = solution.codeGithubUrl
    branch           = "%DefaultBranch%"
    branchSpec       = "%BranchSpecification%"
    agentCleanPolicy = GitVcsRoot.AgentCleanPolicy.ALWAYS
    authMethod = uploadedKey {
        uploadedKey = "provenstyle"
    }
}

fun releaseVcsRoot(solution: NugetSolution) : GitVcsRoot = GitVcsRoot {
    id(solution.releaseVcsRootId)
    uuid             = "${solution.guid}ReleaseVcsRoot"
    name             = "${solution.codeGithubUrl}_Release"
    url              = solution.codeGithubUrl
    branch           = "%DefaultBranch%"
    branchSpec       = "%BranchSpecification%"
    agentCleanPolicy = GitVcsRoot.AgentCleanPolicy.ALWAYS
    authMethod = uploadedKey {
        uploadedKey = "provenstyle"
    }
}
