package design.fiti.cool_do.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class GoalWithTasks(
    @Embedded val goal: GoalEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "goalId"
    )
    val tasks: List<TaskEntity>
)
