package edu.ucne.taskmaster.presentation.TaskList

import edu.ucne.taskmaster.data.local.entities.LabelEntity
import edu.ucne.taskmaster.data.local.entities.TaskEntity

data class Tasks(
    val task: TaskEntity,
    val labels: List<LabelEntity> = emptyList()
)