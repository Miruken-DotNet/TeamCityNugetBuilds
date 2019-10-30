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

    subProject(deploymentProject(solution, preReleaseBuild, releaseBuild))
}

fun deploymentProject(
        solution: NugetSolution,
        preReleaseBuild: BuildType,
        releaseBuild: BuildType
) = Project {
    id(solution.deploymentProjectId)
    uuid     = "${solution.guid}_DeploymentProject"
    parentId = AbsoluteId(solution.id)
    name     = "Deployment"

    params {
        param("SHA", "")
        param("NugetApiKey", solution.nugetApiKey)
    }

    for(nugetProject in solution.nugetProjects){
        subProject(nugetDeployProject(solution, nugetProject, preReleaseBuild, releaseBuild))
    }
}


fun nugetDeployProject (
        solution: NugetSolution,
        project: NugetProject,
        preReleaseBuild: BuildType,
        releaseBuild: BuildType) : Project = Project {

    val baseUuid = "${solution.guid}_${project.id}"
    val baseId   = "${solution.id}_${project.id}"

    id(baseId)
    uuid        = baseUuid
    parentId    = AbsoluteId(solution.deploymentProjectId)
    name        = project.packageName
    description = "${project.packageName} nuget package"

    val deployPreRelease = deployPreReleaseNuget(deployPreRelease(solution, baseId, baseUuid, preReleaseBuild))
    val deployRelease    = deployReleaseNuget(solution.nugetApiKey, deployRelease(solution, baseId, baseUuid, releaseBuild))

    buildType(deployPreRelease)
    buildType(deployRelease)

    params {
        param("NuGetPackSpecFiles", project.nuspecFile)
        param("PackageName",        project.packageName)
    }
}
