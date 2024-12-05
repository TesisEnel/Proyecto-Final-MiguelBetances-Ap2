package edu.ucne.taskmaster.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults.filterChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun CustomFilterChip(
    selected: Boolean,
    onSelectedChange: () -> Unit,
    text: String,
    hexColor: String
) {
    FilterChip(selected = selected, onClick = { onSelectedChange() }, label = {
        Row {
            Text(text)
            Spacer(modifier = Modifier.width(2.dp))
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(
                        color = hexColor.hexToColor(),
                        shape = CircleShape
                    )
                    .border(1.dp, Color.Black, CircleShape)
                    .align(Alignment.CenterVertically)
            )

        }
    }, colors = filterChipColors()
    )
}