package design.fiti.cool_do.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import design.fiti.cool_do.domain.model.Goal

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
) {
    fun toGoal(): Goal = Goal(id, title)
}
