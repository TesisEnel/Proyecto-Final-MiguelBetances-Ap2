package edu.ucne.taskmaster.presentation.TaskList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TaskScreen(
    taskId: Int,
    viewModel: TaskListViewModel = hiltViewModel(),
    goBackToTaskList: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TaskBodyScreen(
        taskId = taskId,
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goBackToTaskList = goBackToTaskList
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TaskBodyScreen(
    taskId: Int,
    uiState: TaskListUIState,
    onEvent: (TaskListUiEvent) -> Unit,
    goBackToTaskList: () -> Unit
) {
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    LaunchedEffect(key1 = true) {
        if (taskId != 0) {
            onEvent(TaskListUiEvent.GetTask(taskId)) // Cargar tarea para edición
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (taskId == 0) "Crear Tarea" else "Modificar Tarea",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = goBackToTaskList) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Ir a lista de tareas"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(15.dp)
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Fecha de Creación (solo lectura)
                    OutlinedTextField(
                        label = { Text("Fecha de Creación") },
                        value = dateFormat.format(uiState.createdDate),
                        onValueChange = {},
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .focusRequester(focusRequester),
                        shape = RoundedCornerShape(10.dp),
                        readOnly = true
                    )

                    // Título de la tarea
                    OutlinedTextField(
                        label = { Text("Título") },
                        value = uiState.title,
                        onValueChange = { onEvent(TaskListUiEvent.TitleChange(it)) },
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp)),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Next) }
                        )
                    )
//                    uiState.errorTitle?.let {
//                        Text(text = it, color = Color.Red)
//                    }

                    // Descripción de la tarea
                    OutlinedTextField(
                        label = { Text("Descripción") },
                        value = uiState.description,
                        onValueChange = { onEvent(TaskListUiEvent.DescriptionChange(it)) },
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp)),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Next) }
                        )
                    )
//                    uiState.errorDescription?.let {
//                        Text(text = it, color = Color.Red)
//                    }

                    // Fecha de vencimiento
                    OutlinedTextField(
                        label = { Text("Fecha de Vencimiento") },
                        value = uiState.dueDate.toString(),
                        onValueChange = { },
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp)),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Next) }
                        )
                    )
//                    uiState.errorDueDate?.let {
//                        Text(text = it, color = Color.Red)
//                    }

                    // Prioridad
                    OutlinedTextField(
                        label = { Text("Prioridad") },
                        value = uiState.priority.toString(),
                        onValueChange = {
                            onEvent(
                                TaskListUiEvent.PriorityChange(
                                    it.toIntOrNull() ?: 0
                                )
                            )
                        },
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp)),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                onEvent(TaskListUiEvent.SaveTask)
                            }
                        )
                    )
//                    uiState.errorPriority?.let {
//                        Text(text = it, color = Color.Red)
//                    }

                    // Botones de acción
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedButton(
                            onClick = { onEvent(TaskListUiEvent.SaveTask) }
                        ) {
                            Icon(
                                imageVector = if (taskId == 0) Icons.Default.Add else Icons.Default.Done,
                                contentDescription = "Guardar Tarea"
                            )
                            Text(
                                text = if (taskId == 0) "Crear Tarea" else "Modificar Tarea"
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
