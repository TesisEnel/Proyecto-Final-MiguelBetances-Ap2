package edu.ucne.taskmaster.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    object Calendar : Screen()

}