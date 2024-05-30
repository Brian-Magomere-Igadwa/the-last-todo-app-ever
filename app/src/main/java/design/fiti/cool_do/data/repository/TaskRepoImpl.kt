package design.fiti.cool_do.data.repository

import design.fiti.cool_do.data.local.AppDao
import design.fiti.cool_do.data.local.entity.GoalWithTasks
import design.fiti.cool_do.data.util.Resource
import design.fiti.cool_do.domain.model.Goal
import design.fiti.cool_do.domain.model.Task
import design.fiti.cool_do.domain.repository.TasksRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TaskRepoImpl(
    val dao: AppDao
) : TasksRepo {
    override suspend fun getGoalsWithTasks(): Flow<Resource<List<GoalWithTasks>>> = flow {
        emit(Resource.Loading())
        try {
            val goals = dao.getGoalsWithTasks()
            emit(Resource.Success(goals))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An error occurred"))
        }
    }

    override suspend fun getGoals(): Flow<Resource<List<Goal>>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(dao.getGoals().map { it.toGoal() }))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An error occurred"))
        }
    }

    override suspend fun getGoalWithTasks(goalId: Int): Flow<Resource<GoalWithTasks>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(dao.getGoalWithTasks(goalId)))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An error occurred"))
        }
    }

    override suspend fun insertGoal(goal: Goal): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            dao.insertGoal(goal.toGoalEntity())
            emit(Resource.Success("Goal added successfully"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An error occurred"))
            e.printStackTrace()
        }
    }

    override suspend fun deleteGoal(goal: Goal): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            dao.deleteGoal(
                goal.toGoalEntity()
            )
            emit(Resource.Success("Goal deleted successfully"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An error occurred"))
        }
    }

    override suspend fun insertTask(task: Task): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            dao.insertTask(
                task.toTaskEntity()
            )
            emit(Resource.Success("Task added successfully"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An error occurred"))
        }
    }

    override suspend fun deleteTask(task: Task): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            dao.deleteTask(task.toTaskEntity())
            emit(Resource.Success("Task deleted successfully"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An error occurred"))
        }
    }

    override suspend fun updateTask(task: Task): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            dao.updateTask(task.toTaskEntity())
            emit(Resource.Success("Task updated successfully"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An error occurred"))
        }
    }

    override suspend fun updateGoal(goal: Goal): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            dao.updateGoal(goal.toGoalEntity())
            emit(Resource.Success("Goal updated successfully"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An error occurred"))
        }
    }

}