package edu.ucne.taskmaster.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class UserEntity(
    @PrimaryKey
    val userId: Int,
    val name: String,
    val email: String,
)