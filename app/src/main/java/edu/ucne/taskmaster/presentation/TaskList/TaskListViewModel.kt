package edu.ucne.taskmaster.presentation.TaskList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.taskmaster.remote.dto.TasksDto
import edu.ucne.taskmaster.repository.TaskRepository
import edu.ucne.taskmaster.repository.TasksRepository
import edu.ucne.taskmaster.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val tasksRepository: TasksRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskListUIState())
    val uiState = _uiState.asStateFlow()

    init {
        onEvent(TaskListUiEvent.GetTasks)
    }

    fun onEvent(event: TaskListUiEvent) {
        when (event) {
            is TaskListUiEvent.CreateDateChange -> {
                _uiState.update { it.copy(createdDate = event.createdDate) }
            }

            is TaskListUiEvent.DescriptionChange -> {
                _uiState.update { it.copy(description = event.description) }
            }

            is TaskListUiEvent.DueDateChange -> {
                _uiState.update { it.copy(dueDate = event.dueDate) }
            }

            is TaskListUiEvent.PriorityChange -> {
                _uiState.update { it.copy(priority = event.priority) }
            }

            is TaskListUiEvent.TitleChange -> {
                _uiState.update { it.copy(title = event.title) }
            }

            is TaskListUiEvent.GetTask -> {
                getTask(event.id)
            }

            is TaskListUiEvent.SaveTask -> {
                saveTask()
            }

            is TaskListUiEvent.DeleteTask -> {
                deleteTask(event.id)
            }

            is TaskListUiEvent.GetTasks -> {
                getTasks()
            }
        }
    }

    private fun getTask(id: Int) {
        viewModelScope.launch {
            taskRepository.getTask(id).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                //createdDate = result.data?.createdDate ?: "",
                                description = result.data?.description ?: "",
                                //dueDate = result.data?.dueDate ?: "",
                                id = result.data?.taskId ?: 0,
                                priority = result.data?.priority ?: 0,
                                title = result.data?.title ?: ""
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "Error al obtener la tarea"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun saveTask() {
        viewModelScope.launch {
            taskRepository.saveTask(uiState.value.toEntity()).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resource.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                        onEvent(TaskListUiEvent.GetTasks)
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "Error al guardar la tarea"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun deleteTask(id: Int) {
        viewModelScope.launch {
            taskRepository.deleteTask(id).let { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resource.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                        onEvent(TaskListUiEvent.GetTasks)
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "Error al eliminar la tarea"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getTasks() {
        viewModelScope.launch {
            tasksRepository.getTasksWithRealLabels().let { tasks ->
                if (tasks.isNotEmpty()) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            tasks = tasks,
                            error = null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "No se encontraron tareas"
                        )
                    }
                }
            }
        }
    }
}

fun TaskListUIState.toEntity() = TasksDto(
    createdDate = createdDate,
    description = description,
    dueDate = dueDate,
    id = id,
    priority = priority,
    title = title
)
