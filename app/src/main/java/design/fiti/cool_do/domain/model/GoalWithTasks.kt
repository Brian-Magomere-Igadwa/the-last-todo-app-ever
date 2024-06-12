package design.fiti.cool_do.domain.model

data class GoalWithTasks(
    val goal: Goal,
    val tasks: List<Task>
)
