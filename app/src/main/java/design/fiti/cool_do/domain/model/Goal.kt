package design.fiti.cool_do.domain.model

import design.fiti.cool_do.data.local.entity.GoalEntity

data class Goal(
    val id: Int,
    val title: String,
    val timeStampForSorting: Long
) {
    fun toGoalEntity(): GoalEntity = GoalEntity(
        title = title,
    )
}
