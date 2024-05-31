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

@Dao
interface AppDao {
    @Transaction
    @Query("SELECT * FROM goals ORDER BY timeStampForSorting DESC")
    fun getGoalsWithTasks(): List<GoalWithTasks>

    @Update(entity = GoalEntity::class)
    fun updateGoal(goal: GoalEntity)

    //get goals
    @Query("SELECT * FROM goals")
    fun getGoals(): List<GoalEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = GoalEntity::class)
    fun insertGoal(goal: GoalEntity)

    @Query("DELETE FROM goals WHERE id = :goalId")
    fun deleteGoal(goalId: Int)

    //delete goal with tasks


    //sort tasks by time
    @Transaction
    @Query("SELECT * FROM goals WHERE id = :goalId")
    fun getGoalWithTasks(goalId: Int): GoalWithTasks

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = TaskEntity::class)
    fun insertTask(task: TaskEntity)


    @Update(entity = TaskEntity::class)
    fun updateTask(task: TaskEntity)

    @Delete(entity = TaskEntity::class)
    fun deleteTask(task: TaskEntity)
}