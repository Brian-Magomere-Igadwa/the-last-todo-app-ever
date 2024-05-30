package design.fiti.cool_do.presentation.viewmodel

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
                            _uiState.update {
                                _uiState.value.copy(
                                    getGoalsForBoardsScreen_isLoading = true,
                                    getGoalsForBoardsScreen_result = Resource.Loading()
                                )
                            }
                        }

                        is Resource.Success -> {
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
                repo.insertGoal(Goal(id = 0, title = uiState.value.newGoalTitle))
                    .collect { resource ->
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
    val error: String = "",
    val newGoalTitle: String = "",
)

data class GoalsForBoardsScreen(
    val goal: Goal,
    val tasksCount: Int,
    val taskCompletionRate: String,
)

//uiEvent for snackbars

