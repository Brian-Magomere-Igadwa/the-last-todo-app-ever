package design.fiti.cool_do.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import design.fiti.cool_do.data.local.entity.GoalWithTasks
import design.fiti.cool_do.data.local.entity.TaskState
import design.fiti.cool_do.data.util.Resource
import design.fiti.cool_do.domain.model.Goal
import design.fiti.cool_do.domain.model.Task
import design.fiti.cool_do.domain.repository.TasksRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val repo: TasksRepo
) : ViewModel() {
    private var _uiState = MutableStateFlow(GoalsUiState())
    val uiState = _uiState.asStateFlow()

    fun getGoals() {
        _uiState.update {
            _uiState.value.copy(getGoals_isLoading = true, getGoals_result = Resource.Loading())
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.getGoals().collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _uiState.update {
                                _uiState.value.copy(
                                    getGoals_isLoading = true,
                                    getGoals_result = Resource.Loading()
                                )
                            }
                        }

                        is Resource.Success -> {
                            _uiState.update {
                                _uiState.value.copy(
                                    goals = resource.data ?: emptyList(),
                                    getGoals_isLoading = false,
                                    getGoals_result = Resource.Success(
                                        "Operation successful"
                                    )
                                )
                            }
                        }

                        is Resource.Error -> {
                            _uiState.update {
                                _uiState.value.copy(
                                    error = resource.message ?: "An error occurred fetching goals.",
                                    getGoals_isLoading = false,
                                    getGoals_result = Resource.Error(
                                        message = resource.message
                                            ?: "An error occurred fetching goals."
                                    )
                                )
                            }
                        }


                    }
                }
            }
        }
    }

    fun deleteGoal(goal: Goal) {
        _uiState.update {
            _uiState.value.copy(
                deleteGoals_isLoading = true,
                deleteGoals_result = Resource.Loading()
            )
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.deleteGoal(goal).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            Log.d("Error checks", "Loading")
                            _uiState.update {
                                _uiState.value.copy(
                                    deleteGoals_isLoading = true,
                                    deleteGoals_result = Resource.Loading()
                                )
                            }
                        }

                        is Resource.Success -> {
                            _uiState.update {
                                Log.d("Error checks", "Successs")
                                _uiState.value.copy(
                                    deleteGoals_isLoading = false,
                                    deleteGoals_result = Resource.Success(
                                        "Operation successful"
                                    )
                                )
                            }
                            delay(200)
                            _uiState.update {

                                _uiState.value.copy(
                                    deleteGoals_isLoading = false,
                                    deleteGoals_result = null
                                )
                            }
                        }

                        is Resource.Error -> {
                            _uiState.update {
                                _uiState.value.copy(
                                    error = resource.message ?: "An error occurred fetching goals.",
                                    deleteGoals_isLoading = false,
                                    deleteGoals_result = Resource.Error(
                                        message = resource.message
                                            ?: "An error occurred fetching goals."
                                    )
                                )
                            }
                            Log.d("Error checks", "Errored...")
                        }


                    }
                }
            }
        }
    }


    fun getGoalWithTasks(goalId: Int) {
        if (uiState.value.goalwithtasks.isEmpty()) getGoalsForBoardsScreen()
        try {
            _uiState.update {
                _uiState.value.copy(
                    filteredGoalWithTasks = _uiState.value.goalwithtasks.find {
                        it.goal.id == goalId
                    }
                )
            }
        } catch (e: Exception) {
            Log.d("Error checks", "Error: ${e.localizedMessage}")
        }
    }

    fun getGoalsForBoardsScreen() {
        _uiState.update {
            _uiState.value.copy(
                getGoalsForBoardsScreen_isLoading = true,
                getGoalsForBoardsScreen_result = Resource.Loading()
            )
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.getGoalsWithTasks().collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            Log.d("Error Checks", "A Loading bumped in .....")
                            _uiState.update {
                                _uiState.value.copy(
                                    getGoalsForBoardsScreen_isLoading = true,
                                    getGoalsForBoardsScreen_result = Resource.Loading()
                                )
                            }
                        }

                        is Resource.Success -> {
                            Log.d("Error Checks", "A Success bumped in .....")
                            _uiState.update {
                                _uiState.value.copy(
                                    goalsForBoardsScreen = resource.data?.map { goalWithTasks ->
                                        GoalsForBoardsScreen(
                                            goal = goalWithTasks.goal.toGoal(),
                                            tasksCount = goalWithTasks.tasks.size,
                                            taskCompletionRate = (if (goalWithTasks.tasks.isNotEmpty()) (goalWithTasks.tasks.filter { it.taskState is TaskState.COMPLETED }.size / goalWithTasks.tasks.size.toFloat() * 100)
                                            else 0).toString() + "%"
                                        )
                                    } ?: emptyList(),
                                    getGoalsForBoardsScreen_isLoading = false,
                                    getGoalsForBoardsScreen_result = Resource.Success("Operation successful"),
                                    goalwithtasks = resource.data ?: emptyList(),
                                )
                            }
                        }

                        is Resource.Error -> {
                            Log.d("Error Checks", "An Error bumped in ..... ${resource.message}")
                            _uiState.update {
                                _uiState.value.copy(
                                    error = resource.message ?: "An error occurred fetching goals.",
                                    getGoalsForBoardsScreen_isLoading = false,
                                    getGoalsForBoardsScreen_result = Resource.Error(
                                        message = resource.message
                                            ?: "An error occurred fetching goals."
                                    )
                                )
                            }
                        }


                    }
                }
            }
        }
    }

    fun parsePercentage(input: String): Int {
        val number = input.removeSuffix("%").toIntOrNull()
        if (number != null) {
            return number
        } else {
            return 1
        }
    }

    fun insertGoal() {
        if (uiState.value.newGoalTitle.isEmpty()) return _uiState.update { uiState.value.copy(error = "We cannot insert a goal that is empty.") }

        _uiState.update {
            _uiState.value.copy(insertGoal_isLoading = true, insertGoal_result = Resource.Loading())
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.insertGoal(
                    Goal(
                        id = 0,
                        title = uiState.value.newGoalTitle,
                        timeStampForSorting = System.currentTimeMillis()
                    )
                ).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _uiState.update {
                                _uiState.value.copy(
                                    insertGoal_isLoading = true,
                                    insertGoal_result = Resource.Loading()
                                )
                            }
                        }

                        is Resource.Success -> {
                            _uiState.update {
                                _uiState.value.copy(
                                    insertGoal_isLoading = false,
                                    newGoalTitle = "",
                                    insertGoal_result = Resource.Success("Operation successful")
                                )
                            }
                            delay(100)
                            _uiState.update {
                                _uiState.value.copy(
                                    insertGoal_isLoading = false,
                                    insertGoal_result = null
                                )
                            }
                        }

                        is Resource.Error -> {
                            _uiState.update {
                                _uiState.value.copy(
                                    error = resource.message
                                        ?: "An error occurred fetching goals.",
                                    insertGoal_isLoading = false,
                                    insertGoal_result = Resource.Error(
                                        message = resource.message
                                            ?: "An error occurred fetching goals."
                                    )
                                )
                            }
                        }


                    }
                }
            }
        }
    }

    fun updateNewGoalTitle(text: String) {
        _uiState.update {
            _uiState.value.copy(newGoalTitle = text)
        }
    }

    fun updateNewTaskTitle(text: String) {
        _uiState.update {
            _uiState.value.copy(newTaskTitle = text)
        }
    }

    fun insertTask(dateMillis: Long?, timeHour: Int, timeMins: Int, goalId: Int) {
        if (uiState.value.newTaskTitle.isEmpty()) return _uiState.update { uiState.value.copy(error = "We cannot insert a goal that is empty.") }

        _uiState.update {
            _uiState.value.copy(insertTask_isLoading = true, insertTask_result = Resource.Loading())
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.insertTask(
                    Task(
                        title = uiState.value.newTaskTitle,
                        goalId = goalId,
                        deadline = "$dateMillis $timeHour:$timeMins"
                    )
                ).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _uiState.update {
                                _uiState.value.copy(
                                    insertTask_isLoading = true,
                                    insertTask_result = Resource.Loading()
                                )
                            }
                        }

                        is Resource.Success -> {
                            _uiState.update {
                                _uiState.value.copy(
                                    insertTask_isLoading = false,
                                    newTaskTitle = "",
                                    insertTask_result = Resource.Success("Operation successful")
                                )
                            }
                            delay(100)
                            _uiState.update {
                                _uiState.value.copy(
                                    insertTask_isLoading = false,
                                    insertTask_result = null
                                )
                            }
                        }

                        is Resource.Error -> {
                            _uiState.update {
                                _uiState.value.copy(
                                    error = resource.message
                                        ?: "An error occurred fetching goals.",
                                    insertTask_isLoading = false,
                                    insertTask_result = Resource.Error(
                                        message = resource.message
                                            ?: "An error occurred fetching goals."
                                    )
                                )
                            }
                        }


                    }
                }
            }
        }
    }

    init {
        getGoalsForBoardsScreen()
    }
}

data class GoalsUiState(
    val goals: List<Goal> = emptyList(),
    val goalwithtasks: List<GoalWithTasks> = emptyList(),
    val goalsForBoardsScreen: List<GoalsForBoardsScreen> = emptyList(),
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val getGoalsForBoardsScreen_isLoading: Boolean = false,
    val getGoalsForBoardsScreen_result: Resource<String>? = null,
    val getGoals_isLoading: Boolean = false,
    val getGoals_result: Resource<String>? = null,
    val getGoalsWithTask_isLoading: Boolean = false,
    val getGoalsWithTask_result: Resource<String>? = null,
    val insertGoal_isLoading: Boolean = false,
    val insertGoal_result: Resource<String>? = null,
    val insertTask_isLoading: Boolean = false,
    val insertTask_result: Resource<String>? = null,
    val deleteGoals_isLoading: Boolean = false,
    val deleteGoals_result: Resource<String>? = null,
    val error: String = "",
    val newGoalTitle: String = "",
    val newTaskTitle: String = "",
    val filteredGoalWithTasks: GoalWithTasks? = null
) {

}

data class GoalsForBoardsScreen(
    val goal: Goal,
    val tasksCount: Int,
    val taskCompletionRate: String,
)

//uiEvent for snackbars

