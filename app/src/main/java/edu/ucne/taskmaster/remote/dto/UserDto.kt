package edu.ucne.taskmaster.remote.dto

import edu.ucne.taskmaster.data.local.entities.UserEntity

data class UserDto(
    val email: String,
    val name: String,
    val userId: Int
)

fun UserDto.toUserEntity(): UserEntity {
    return UserEntity(
        userId = this.userId,
        name = this.name,
        email = this.email
    )
}