package edu.ucne.taskmaster.presentation.TaskList

import edu.ucne.taskmaster.data.local.entities.LabelEntity
import edu.ucne.taskmaster.data.local.entities.TaskEntity
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
    val error: String? = null,
    val showModal: Boolean = false,
    val orderBy: List<String> = listOf("Created Date", "Due Date", "Priority"),
    val currentOrder: Int = 0,
    val priorityOptions: List<String> = listOf(
        "Urgente",
        "Alta",
        "Media",
        "Baja",
        "Sin prisa"
    ),
    val labels: List<LabelEntity> = emptyList(),
    val labelsSelected: List<LabelEntity> = emptyList(),
    val labelsDescription: List<String> = emptyList()
)

fun TaskListUIState.toEntity() = TaskEntity(
    createdDate = createdDate,
    description = description,
    dueDate = dueDate,
    priority = priority,
    title = title,
    taskId = if (id == 0) null else id,
)