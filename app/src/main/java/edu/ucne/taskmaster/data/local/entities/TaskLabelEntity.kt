package edu.ucne.taskmaster.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LabelTasks")
data class TaskLabelEntity(
    @PrimaryKey
    val id: Int? = null,
    val taskId: Int,
    val labelId: Int
)