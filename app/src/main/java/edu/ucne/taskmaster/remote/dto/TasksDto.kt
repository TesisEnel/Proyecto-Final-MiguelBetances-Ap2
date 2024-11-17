package edu.ucne.taskmaster.remote.dto

import edu.ucne.taskmaster.data.local.entities.TaskEntity

class TasksDto(
    val createdDate: String,
    val description: String,
    val dueDate: String,
    val id: Int,
    val priority: Int,
    val title: String,
)

fun TasksDto.toTaskEntity(): TaskEntity {
    return TaskEntity(
        taskId = this.id,
        createdDate = this.createdDate,
        description = this.description,
        dueDate = this.dueDate,
        priority = this.priority,
        title = this.title,
        synchronized = 0
    )
}