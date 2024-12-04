package edu.ucne.taskmaster.util

import androidx.compose.ui.graphics.Color

fun String.hexToColor(): Color {
    if (this.length != 6 && this.length != 8) {
        return Color.Gray
    }

    return try {
        val offset = if (this.length == 8) 2 else 0
        val red = this.substring(offset, offset + 2).toInt(16)
        val green = this.substring(offset + 2, offset + 4).toInt(16)
        val blue = this.substring(offset + 4, offset + 6).toInt(16)
        Color(red, green, blue)
    } catch (e: Exception) {
        Color.Gray
    }
}



