package design.fiti.cool_do.domain.model

import design.fiti.cool_do.data.local.entity.TaskEntity
import design.fiti.cool_do.data.local.entity.TaskState

data class Task(
    val id: Int = 0,
    val goalId: Int,
    val title: String,
    val deadline: String,
    val taskState: TaskState = TaskState.TODO,
) {
    fun toTaskEntity(): TaskEntity = TaskEntity(
        goalId = goalId,
        title = title,
        deadline = deadline,
        taskState = taskState
    )
}
