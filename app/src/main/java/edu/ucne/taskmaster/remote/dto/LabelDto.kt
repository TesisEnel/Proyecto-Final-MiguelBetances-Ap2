package edu.ucne.taskmaster.remote.dto

import edu.ucne.taskmaster.data.local.entities.LabelEntity

data class LabelDto(
    val description: String?,
    val hexColor: String = "#FFFFFF",
    val id: Int? = null,
)


fun LabelDto.toLabelEntity(): LabelEntity {
    return LabelEntity(
        id = this.id,
        description = this.description ?: "N/D",
        hexColor = this.hexColor,
        synchronized = 0
    )
}