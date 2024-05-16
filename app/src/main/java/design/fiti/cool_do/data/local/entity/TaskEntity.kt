package design.fiti.cool_do.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val timeline: String,
    val taskState: TaskState,
)

sealed class TaskState(val state: String) {
    object TODO : TaskState("TODO")
    object INPROGRESS : TaskState("IN PROGRESS")
    object COMPLETED : TaskState("COMPLETED")

}
