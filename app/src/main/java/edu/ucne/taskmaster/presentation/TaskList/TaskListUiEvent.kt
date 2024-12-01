package edu.ucne.taskmaster.presentation.TaskList

import java.util.Date

sealed interface TaskListUiEvent {
    data class CreateDateChange(val createdDate: Date) : TaskListUiEvent
    data class DescriptionChange(val description: String) : TaskListUiEvent
    data class DueDateChange(val dueDate: Date) : TaskListUiEvent
    data class PriorityChange(val priority: Int) : TaskListUiEvent
    data class TitleChange(val title: String) : TaskListUiEvent
    data class GetTask(val id: Int) : TaskListUiEvent
    data object SaveTask : TaskListUiEvent
    data class DeleteTask(val id: Int) : TaskListUiEvent
    data object GetTasks : TaskListUiEvent
}