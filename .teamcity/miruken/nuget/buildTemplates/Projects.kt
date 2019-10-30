package miruken.nuget.buildTemplates

import jetbrains.buildServer.configs.kotlin.v2018_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_2.Project
import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.finishBuildTrigger

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
        subProject(configureNugetDeployProject(solution, nugetProject, preReleaseBuild, releaseBuild))
    }
}


fun configureNugetDeployProject (
        solution: NugetSolution,
        project: NugetProject,
        preReleaseBuild: BuildType,
        releaseBuild: BuildType) : Project{

    val baseUuid = "${solution.guid}_${project.id}"
    val baseId   = "${solution.id}_${project.id}"

    val deployPreRelease =  deployPreReleaseNuget(BuildType {
        id("${baseId}_DeployPreRelease")
        uuid               = "${baseUuid}_DeployPreRelease"
        name               = "Deploy PreRelease"
        description        = "This will push a NuGet package with a -PreRelease tag for testing from the develop branch. NO CI.   (Note: Non-prerelease nuget packages come from the master branch)"
        buildNumberPattern = "%BuildFormatSpecification%"
        maxRunningBuilds   = 1

        params {
            param("BuildFormatSpecification", "%dep.${solution.preReleaseBuildId}.BuildFormatSpecification%")
            param("PackageVersion",           "%dep.${solution.preReleaseBuildId}.PackageVersion%")
        }

        triggers {
            finishBuildTrigger {
                id = "${baseId}_DeployPreRelease_TRIGGER"
                buildType = solution.preReleaseBuildId
                successfulOnly = true
                branchFilter = "+:*"
            }
        }

        dependencies {
            dependency(preReleaseBuild) {
                snapshot {
                }

                artifacts {
                    id               = "${baseId}_PreRelease_ARTIFACT_DEPENDENCY"
                    cleanDestination = true
                    artifactRules    = "%ArtifactsOut%"
                }
            }
        }
    })

    val deployRelease = deployReleaseNuget(solution.nugetApiKey, BuildType {
        id("${baseId}_DeployRelease")
        uuid         = "${baseUuid}_DeployRelease"
        name         = "Deploy Release"
        description  = "This will push a NuGet package from the MASTER branch. NO CI."

        buildNumberPattern = "%BuildFormatSpecification%"
        maxRunningBuilds   = 1

        params {
            param("BuildFormatSpecification", "%dep.${solution.releaseBuildId}.BuildFormatSpecification%")
            param("PackageVersion",           "%dep.${solution.releaseBuildId}.PackageVersion%")
            param("PrereleaseVersion",        "")
        }

        triggers {
            finishBuildTrigger {
                id             = "${baseId}_Release_TRIGGER"
                buildType      = solution.releaseBuildId
                branchFilter   = "+:master"
            }
        }

        dependencies {
            dependency(releaseBuild){
                snapshot {
                }

                artifacts {
                    id               = "${baseId}_Release_ARTIFACT_DEPENDENCY"
                    cleanDestination = true
                    artifactRules    = "%ArtifactsOut%"
                }
            }
        }
    })

    return Project {
        id(baseId)
        uuid        = baseUuid
        parentId    = AbsoluteId(solution.deploymentProjectId)
        name        = project.packageName
        description = "${project.packageName} nuget package"

        buildType(deployPreRelease)
        buildType(deployRelease)

        params {
            param("NuGetPackSpecFiles", project.nuspecFile)
            param("PackageName",        project.packageName)
        }
    }
}
