package edu.ucne.taskmaster.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tasks")
data class TaskEntity(
    @PrimaryKey
    val taskId: Int = 0,
    val createdDate: String = "2021-01-01T00:00:00",
    val description: String?,
    val dueDate: String = "2021-01-01T00:00:00",
    val priority: Int = 0,
    val title: String?,
    val synchronized: Int = 0  // local = 0 , download = 1, upload = 2
)
