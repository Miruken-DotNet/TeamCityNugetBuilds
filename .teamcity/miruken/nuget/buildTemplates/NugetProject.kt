package miruken.nuget.buildTemplates

class NugetProject(
        val id:          String,
        val nuspecFile:  String,
        val packageName: String){

    fun baseUuid(solution: NugetSolution): String
            = "${solution.guid}_$id"

    fun baseId(solution: NugetSolution) : String
            = "${solution.id}_$id"
}