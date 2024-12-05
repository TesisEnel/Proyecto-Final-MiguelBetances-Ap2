package edu.ucne.taskmaster.presentation.TaskList

import edu.ucne.taskmaster.data.local.entities.LabelEntity
import java.util.Date

sealed interface TaskListUiEvent {
    data class SearchQueryChange(val query: String) : TaskListUiEvent
    data class CreateDateChange(val createdDate: Date) : TaskListUiEvent
    data class DescriptionChange(val description: String) : TaskListUiEvent
    data class DueDateChange(val dueDate: Date) : TaskListUiEvent
    data class PriorityChange(val priority: Int) : TaskListUiEvent
    data class TitleChange(val title: String) : TaskListUiEvent
    data class FilterLabelToggle(val labelsSelected: LabelEntity) : TaskListUiEvent
    data class LabelToggle(val labelsSelected: LabelEntity) : TaskListUiEvent
    data class GetTask(val id: Int) : TaskListUiEvent
    data object SaveTask : TaskListUiEvent
    data object GetLabels : TaskListUiEvent
    data class DeleteTask(val id: Int) : TaskListUiEvent
    data object GetTasks : TaskListUiEvent
    data class OnOrderChange(val order: String) : TaskListUiEvent
    data object GetLabelDescription : TaskListUiEvent
    data object ApplyFilters : TaskListUiEvent
    data object ResetFilters : TaskListUiEvent
    data object Validate : TaskListUiEvent
}

