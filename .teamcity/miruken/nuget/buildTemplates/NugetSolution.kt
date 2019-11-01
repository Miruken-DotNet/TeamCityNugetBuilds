package miruken.nuget.buildTemplates

class NugetSolution(
        val guid:           String,
        val id:             String,
        val parentId:       String,
        val name:           String,
        val solutionFile:   String,
        val codeGithubUrl:  String,
        val nugetApiKey:    String,
        val majorVersion:   String,
        val minorVersion:   String,
        val patchVersion:   String,
        val nugetProjects:  List<NugetProject>,
        val testAssemblies: String? = null
){

    val ciVcsRootId: String
        get() = "${id}_CIVCSRoot"

    val preReleaseVcsRootId: String
        get() = "${id}_PreReleaseVCSRoot"

    val releaseVcsRootId: String
        get() = "${id}_ReleaseVCSRoot"

    val ciBuildId: String
        get() = "${id}_CIBuild"

    val preReleaseBuildId: String
        get() = "${id}_PreReleaseBuild"

    val releaseBuildId: String
        get() = "${id}_ReleaseBuild"

    val deploymentProjectId: String
        get() = "${id}_DeploymentProject"
}