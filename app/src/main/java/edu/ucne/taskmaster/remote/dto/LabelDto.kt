package edu.ucne.taskmaster.remote.dto

class LabelDto(

    val Active: Boolean,
    val Description: String,
    val HexColor: String = "#FFFFFF",
    val Id: Int,
    val UserId: Int,
)