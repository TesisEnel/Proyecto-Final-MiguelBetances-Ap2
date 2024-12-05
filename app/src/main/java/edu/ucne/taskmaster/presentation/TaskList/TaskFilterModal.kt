package edu.ucne.taskmaster.presentation.TaskList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.ucne.taskmaster.data.local.entities.LabelEntity
import edu.ucne.taskmaster.util.CustomFilterChip

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
fun TaskFilterModal(
    sheetState: SheetState,
    unShowBottomSheet: () -> Unit,
    uiState: TaskListUIState,
    onTextChange: (String) -> Unit,
    onToggleLabel: (LabelEntity) -> Unit,
    onApplyFilters: () -> Unit,
    onResetFilters: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { unShowBottomSheet() },
        sheetState = sheetState
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = uiState.description,
                onValueChange = onTextChange,
                label = { Text("Buscar tareas (título o descripción)") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Filtrar por etiquetas", style = MaterialTheme.typography.titleMedium)
            FlowRow(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                uiState.labels.forEach { label ->
                    CustomFilterChip(
                        selected = uiState.labelsSelected.contains(label),
                        onSelectedChange = { onToggleLabel(label) },
                        text = label.description,
                        hexColor = label.hexColor
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Button(onClick = { onApplyFilters(); unShowBottomSheet() }) {
                    Text("Aplicar")
                    Icon(Icons.Default.Search, contentDescription = "Aplicar filtros")
                }
                Button(onClick = { onResetFilters(); unShowBottomSheet() }) {
                    Text("Resetear")
                    Icon(Icons.Default.Refresh, contentDescription = "Resetear filtros")
                }
            }
        }
    }
}

