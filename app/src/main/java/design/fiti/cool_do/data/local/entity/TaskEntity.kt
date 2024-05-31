package design.fiti.cool_do.data.local.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = GoalEntity::class,
            parentColumns = ["id"],
            childColumns = ["goalId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val goalId: Int,
    val title: String,
    val deadline: String,
    val taskState: TaskState,
)

sealed class TaskState(val state: String) {
    data object TODO : TaskState("TODO")
    data object INPROGRESS : TaskState("IN PROGRESS")
    data object COMPLETED : TaskState("COMPLETED")

}
