package miruken.nuget.buildTemplates

class NugetProject(
        val id:          String,
        val packageName: String,
        val nuspecFile:  String? = null
){

    fun baseUuid(solution: NugetSolution): String
            = "${solution.guid}_$id"

    fun baseId(solution: NugetSolution) : String
            = "${solution.id}_$id"
}