package edu.ucne.taskmaster.remote.dto

import java.util.Date

class TasksDto(

    val createdDate: Date,
    val description: String,
    val dueDate: Date,
    val id: Int,
    val priority: Int,
    val title: String,
    val userId: Int
)