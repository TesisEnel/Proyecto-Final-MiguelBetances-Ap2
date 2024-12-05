package edu.ucne.taskmaster.presentation.TaskList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.taskmaster.presentation.Calendar.DatePickerDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TaskScreen(
    taskId: Int,
    viewModel: TaskListViewModel = hiltViewModel(),
    goBackToTaskList: () -> Unit,
    onDateClick: () -> Unit = viewModel::onDateClick,
    onDateSelected: (Date) -> Unit = viewModel::changeSelectedDate,
    onEvent: (TaskListUiEvent) -> Unit = viewModel::onEvent
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = taskId) {
        if (taskId != 0)
            onEvent(TaskListUiEvent.GetTask(taskId))
    }

    TaskBodyScreen(
        taskId = taskId,
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goBackToTaskList = goBackToTaskList,
        onDateClick = onDateClick,
        onDateSelected = onDateSelected,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
private fun TaskBodyScreen(
    taskId: Int,
    uiState: TaskListUIState,
    onEvent: (TaskListUiEvent) -> Unit,
    goBackToTaskList: () -> Unit,
    onDateClick: () -> Unit,
    onDateSelected: (Date) -> Unit
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (taskId == 0) "Crear Tarea" else "Modificar Tarea") },
                navigationIcon = {
                    IconButton(onClick = goBackToTaskList) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            Column {
                Button(
                    onClick = {
                        onEvent(TaskListUiEvent.Validate)

                        if (uiState.title != "" && uiState.description != ""
                        ) {
                            onEvent(TaskListUiEvent.SaveTask)
                            goBackToTaskList()
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        imageVector = if (taskId == 0) Icons.Default.Add else Icons.Default.Done,
                        contentDescription = null
                    )
                    Text(text = if (taskId == 0) "Crear" else "Modificar")
                }
                if (taskId != 0) {
                    Button(
                        onClick = {
                            onEvent(TaskListUiEvent.DeleteTask(taskId))
                            goBackToTaskList()
                        },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = null
                        )
                        Text(text = "Eliminar")
                    }
                }
            }
        }
    ) { innerPadding ->
        if (uiState.showModal) {
            DatePickerDialog(
                onDateSelected = { date ->
                    onDateSelected(date)
                },
                onDismissRequest = { onDateClick() }
            )
        }
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Title Field
            item {
                OutlinedTextField(
                    label = { Text("Título") },
                    value = uiState.title,
                    onValueChange = { onEvent(TaskListUiEvent.TitleChange(it)) },
                    isError = uiState.titleError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.titleError != null) {
                    Text(
                        uiState.titleError,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Description Field
            item {
                OutlinedTextField(
                    label = { Text("Descripción") },
                    value = uiState.description,
                    onValueChange = { onEvent(TaskListUiEvent.DescriptionChange(it)) },
                    isError = uiState.descriptionError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.descriptionError != null) {
                    Text(
                        uiState.descriptionError,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Due Date
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDateClick() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Fecha de Vencimiento:", style = MaterialTheme.typography.bodyMedium)
                    TextButton(onClick = onDateClick) {
                        Text(dateFormat.format(uiState.dueDate))
                    }
                }
            }

            // Priority Selection
            item {
                Text("Prioridad", style = MaterialTheme.typography.titleMedium)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    uiState.priorityOptions.forEachIndexed { index, priority ->
                        AssistChip(
                            onClick = { onEvent(TaskListUiEvent.PriorityChange(index)) },
                            label = { Text(priority) },
                            colors = if (uiState.priority == index) {
                                AssistChipDefaults.assistChipColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    labelColor = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                AssistChipDefaults.assistChipColors(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    labelColor = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        )
                    }
                }
            }

            // Labels
            item {
                Column {
                    // Selected Labels
                    Text("Etiquetas seleccionadas", style = MaterialTheme.typography.titleMedium)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        uiState.labelsSelected.forEach { label ->
                            FilterChip(
                                selected = true,
                                onClick = { onEvent(TaskListUiEvent.LabelToggle(label)) },
                                label = { Text(label.description) },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    labelColor = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }

                    // Unselected Labels
                    Text("Etiquetas no seleccionadas", style = MaterialTheme.typography.titleMedium)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        uiState.labels.forEach { label ->
                            FilterChip(
                                selected = false,
                                onClick = { onEvent(TaskListUiEvent.LabelToggle(label)) },
                                label = { Text(label.description) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun TaskScreenPreview() {
    TaskScreen(
        taskId = 0,
        goBackToTaskList = {}
    )
}
