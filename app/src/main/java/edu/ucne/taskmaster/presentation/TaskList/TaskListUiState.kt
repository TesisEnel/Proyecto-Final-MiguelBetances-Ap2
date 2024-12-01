package edu.ucne.taskmaster.presentation.TaskList

import java.util.Date

data class TaskListUIState(
    val isLoading: Boolean = false,
    val createdDate: Date = Date(),
    val description: String = "",
    val dueDate: Date = Date(),
    val id: Int = 0,
    val priority: Int = 0,
    val title: String = "",
    val tasks: List<Tasks> = emptyList(),
    val error: String? = null
)