package edu.ucne.taskmaster.util

import androidx.compose.ui.graphics.Color

fun Color.toHexString(): String {
    return String.format(
        "#%02X%02X%02X",
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt()
    )
}

