package miruken.nuget.buildTemplates

import jetbrains.buildServer.configs.kotlin.v2018_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_2.Project
import jetbrains.buildServer.configs.kotlin.v2018_2.*

fun solutionProject(
        solution: NugetSolution,
        ciVcsRoot: VcsRoot,
        preReleaseVcsRoot: VcsRoot,
        releaseVcsRoot: VcsRoot,
        ciBuild: BuildType,
        preReleaseBuild: BuildType,
        releaseBuild: BuildType
) : Project = Project {
    id(solution.id)
    uuid        = solution.guid
    parentId    = AbsoluteId(solution.parentId)
    name        = solution.name
    description = "CI/CD for ${solution.solutionFile}"

    vcsRoot(ciVcsRoot)
    vcsRoot(preReleaseVcsRoot)
    vcsRoot(releaseVcsRoot)

    buildType(ciBuild)
    buildType(preReleaseBuild)
    buildType(releaseBuild)

    params {
        param("ArtifactsIn", """
            Source      => Build.zip!/Source
            packages    => Build.zip!/packages
            ${solution.solutionFile} => Build.zip!
        """.trimIndent())
        param("ArtifactsOut", """
            Build.zip!/Source   => Source
            Build.zip!/packages => packages
            Build.zip!/${solution.solutionFile}
        """.trimIndent())
        param("MajorVersion",        solution.majorVersion)
        param("MinorVersion",        solution.minorVersion)
        param("PatchVersion",        solution.patchVersion)
        param("PreReleaseProjectId", solution.preReleaseBuildId)
        param("ReleaseProjectId",    solution.releaseBuildId)
        param("Solution",            solution.solutionFile)
        param("SolutionProjectId",   solution.id)
        param("TestAssemblies",      solution.testAssemblies)
    }
}

fun deploymentProject(solution: NugetSolution) = Project {
    id(solution.deploymentProjectId)
    uuid     = "${solution.guid}_DeploymentProject"
    parentId = AbsoluteId(solution.id)
    name     = "Deployment"

    params {
        param("SHA", "")
        param("NugetApiKey", solution.nugetApiKey)
    }
}

fun nugetDeployProject (
        solution: NugetSolution,
        project: NugetProject,
        deployPreReleaseBuild: BuildType,
        deployReleaseBuild: BuildType) : Project = Project {

    id(project.baseId(solution))
    uuid        = project.baseUuid(solution)
    parentId    = AbsoluteId(solution.deploymentProjectId)
    name        = project.packageName
    description = "${project.packageName} nuget package"

    buildType(deployPreReleaseBuild)
    buildType(deployReleaseBuild)

    params {
        if(project.nuspecFile != null)
            param("NuGetPackSpecFiles", project.nuspecFile)

        param("PackageName",        project.packageName)
    }
}
