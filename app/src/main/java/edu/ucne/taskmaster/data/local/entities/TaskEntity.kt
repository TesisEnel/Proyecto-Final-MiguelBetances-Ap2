package edu.ucne.taskmaster.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Tasks")
data class TaskEntity(
    @PrimaryKey
    val taskId: Int? = null,
    val createdDate: Date,
    val description: String?,
    val dueDate: Date,
    val priority: Int = 0,
    val title: String?,
    val synchronized: Int = 0
)
