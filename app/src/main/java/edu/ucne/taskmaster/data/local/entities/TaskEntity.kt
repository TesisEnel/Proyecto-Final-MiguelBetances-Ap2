package edu.ucne.taskmaster.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tasks")
data class TaskEntity(
    @PrimaryKey
    val taskId: Int,
    val createdDate: String,
    val description: String?,
    val dueDate: String,
    val priority: Int,
    val title: String?,
    val synchronized: Int // local = 0 , download = 1, upload = 2
)
