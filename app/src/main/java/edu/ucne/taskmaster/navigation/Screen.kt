package edu.ucne.taskmaster.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    object Calendar : Screen()

    @Serializable
    data class Task(val id: Int) : Screen()

    @Serializable
    data object TaskList : Screen()

    @Serializable
    object LabelList : Screen()

    @Serializable
    data class Label(val id: Int) : Screen()

    @Serializable
    data object LoginScreen : Screen()


}