package edu.ucne.taskmaster.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Label")
data class LabelEntity(
    @PrimaryKey
    val id: Int,
    val active: Boolean,
    val description: String?,
    val hexColor: String?,
)