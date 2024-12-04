package edu.ucne.taskmaster.util

import android.graphics.Bitmap
import android.graphics.Color.HSVToColor
import android.graphics.Paint
import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toRect
import kotlinx.coroutines.launch

@Composable
fun HueBar(
    setColor: (Float) -> Unit // Pass selected hue (0-360)
) {
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val pressOffset = remember { mutableStateOf(Offset.Zero) }

    Canvas(
        modifier = Modifier
            .height(40.dp)
            .width(300.dp)
            .clip(RoundedCornerShape(50))
            .emitDragGesture(interactionSource)
    ) {
        val drawScopeSize = size
        val huePanel = RectF(0f, 0f, size.width, size.height)

        // Create Hue Gradient
        val hueColors = IntArray(size.width.toInt()) { i ->
            val hue = i * 360f / size.width
            HSVToColor(floatArrayOf(hue, 1f, 1f))
        }

        // Draw Hue Bar
        drawIntoCanvas { canvas ->
            val nativeCanvas = canvas.nativeCanvas
            val bitmap = Bitmap.createBitmap(
                size.width.toInt(),
                size.height.toInt(),
                Bitmap.Config.ARGB_8888
            )
            val hueCanvas = android.graphics.Canvas(bitmap)
            val paint = Paint().apply { strokeWidth = 0f }

            for (i in hueColors.indices) {
                paint.color = hueColors[i]
                hueCanvas.drawLine(i.toFloat(), 0f, i.toFloat(), huePanel.bottom, paint)
            }

            nativeCanvas.drawBitmap(bitmap, null, huePanel.toRect(), null)
        }

        // Handle Point-to-Hue Conversion
        fun pointToHue(pointX: Float): Float {
            val clampedX = pointX.coerceIn(0f, huePanel.width())
            return (clampedX / huePanel.width()) * 360f
        }

        // Capture Press/Drag Gesture
        scope.launch {
            collectForPress(interactionSource) { pressPosition ->
                val x = pressPosition.x.coerceIn(0f..drawScopeSize.width)
                pressOffset.value = Offset(x, 0f)
                val selectedHue = pointToHue(x)
                setColor(selectedHue)
            }
        }

        // Draw Selector Circle
        drawCircle(
            Color.White,
            radius = size.height / 2,
            center = Offset(pressOffset.value.x, size.height / 2),
            style = Stroke(2.dp.toPx())
        )
    }
}

suspend fun collectForPress(
    interactionSource: InteractionSource,
    setOffset: (Offset) -> Unit
) {
    interactionSource.interactions.collect { interaction ->
        (interaction as? PressInteraction.Press)?.pressPosition?.let(setOffset)
    }
}

private fun Modifier.emitDragGesture(
    interactionSource: MutableInteractionSource
): Modifier = composed {
    val scope = rememberCoroutineScope()
    pointerInput(Unit) {
        detectDragGestures { input, _ ->
            scope.launch {
                interactionSource.emit(PressInteraction.Press(input.position))
            }
        }
    }.clickable(interactionSource, null) { }
}
