package edu.ucne.taskmaster.remote.dto

import edu.ucne.taskmaster.data.local.entities.TaskLabelEntity

data class TaskLabelDto(
    val id: Int,
    val taskId: Int,
    val labelId: Int
)

fun TaskLabelDto.toTaskLabelEntity(): TaskLabelEntity {
    return TaskLabelEntity(
        id = this.id,
        taskId = this.taskId,
        labelId = this.labelId
    )
}