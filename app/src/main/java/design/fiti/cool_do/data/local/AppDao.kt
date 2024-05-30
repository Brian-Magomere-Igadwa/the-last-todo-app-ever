package design.fiti.cool_do.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import design.fiti.cool_do.data.local.entity.GoalEntity
import design.fiti.cool_do.data.local.entity.GoalWithTasks
import design.fiti.cool_do.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Transaction
    @Query("SELECT * FROM goals")
    suspend fun getGoalsWithTasks(): List<GoalWithTasks>

    @Update(entity = GoalEntity::class)
    suspend fun updateGoal(goal: GoalEntity)

    //get goals
    @Query("SELECT * FROM goals")
    suspend fun getGoals(): List<GoalEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = GoalEntity::class)
    suspend fun insertGoal(goal: GoalEntity)

    @Delete(entity = GoalEntity::class)
    suspend fun deleteGoal(goal: GoalEntity)

    //delete goal with tasks


    @Transaction
    @Query("SELECT * FROM goals WHERE id = :goalId")
    suspend fun getGoalWithTasks(goalId: Int): GoalWithTasks

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = TaskEntity::class)
    suspend fun insertTask(task: TaskEntity)


    @Update(entity = TaskEntity::class)
    suspend fun updateTask(task: TaskEntity)

    @Delete(entity = TaskEntity::class)
    suspend fun deleteTask(task: TaskEntity)
}