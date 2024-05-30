package design.fiti.cool_do.domain.repository


import design.fiti.cool_do.data.local.entity.GoalWithTasks
import design.fiti.cool_do.data.util.Resource
import design.fiti.cool_do.domain.model.Goal
import design.fiti.cool_do.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TasksRepo {
    suspend fun getGoalsWithTasks(): Flow<Resource<List<GoalWithTasks>>>
    suspend fun getGoals(): Flow<Resource<List<Goal>>>
    suspend fun getGoalWithTasks(goalId: Int): Flow<Resource<GoalWithTasks>>

    suspend fun insertGoal(goal: Goal): Flow<Resource<String>>

    suspend fun deleteGoal(goal: Goal): Flow<Resource<String>>

    suspend fun insertTask(task: Task): Flow<Resource<String>>

    suspend fun deleteTask(task: Task): Flow<Resource<String>>

    suspend fun updateTask(task: Task): Flow<Resource<String>>

    suspend fun updateGoal(goal: Goal): Flow<Resource<String>>

}