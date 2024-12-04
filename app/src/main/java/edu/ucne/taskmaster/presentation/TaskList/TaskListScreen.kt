package edu.ucne.taskmaster.presentation.TaskList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.taskmaster.data.local.entities.LabelEntity
import edu.ucne.taskmaster.data.local.entities.TaskEntity
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel = hiltViewModel(),
    createTask: () -> Unit,
    onTaskClick: (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Task List", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(34, 40, 49),
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { /* Add search logic */ }) {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = createTask,
                containerColor = Color(0xFF007ACC)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.White)
            }
        }
    ) { innerPadding ->
        TaskListBody(
            innerPadding = innerPadding,
            uiState = uiState,
            onTaskClick = { onTaskClick(it) },
            onOrderChange = { order -> viewModel.onEvent(TaskListUiEvent.OnOrderChange(order)) }
        )
    }
}


@Composable
fun TaskListBody(
    innerPadding: PaddingValues,
    uiState: TaskListUIState,
    onTaskClick: (Int) -> Unit,
    onOrderChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(34, 40, 49),
                        Color(57, 62, 70)
                    )
                )
            )
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        } else if (uiState.error?.isNotEmpty() == true) {
            Text(
                text = uiState.error,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        } else {
            Column {
                // Botones de ordenación
                OrderButtons(
                    orderBy = uiState.orderBy,
                    currentOrder = uiState.currentOrder,
                    onOrderChange = onOrderChange
                )

                // Lista de tareas
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(16.dp)

                ) {
                    items(uiState.tasks.size) { index ->
                        val tasks = uiState.tasks[index]
                        TaskItem(tasks.task, tasks.labels, onTaskClick)
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: TaskEntity, labels: List<LabelEntity>, onTaskClick: (Int) -> Unit) {
    val dueDateColor = task.dueDate.let {
        calculateDueDateColor(it)
    }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(44, 62, 80))
            .clickable { onTaskClick(task.taskId!!) }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Título de la tarea (resaltado)
        Text(
            text = task.title ?: "Untitled Task",
            color = Color(0xFF007ACC), // Azul destacado
            style = MaterialTheme.typography.titleMedium
        )

        // Fecha de vencimiento con colores dinámicos
        Text(
            text = "Due Date: " + formatDueDateAndTime(task.dueDate),
            color = dueDateColor,
            style = MaterialTheme.typography.bodyMedium
        )

        // Descripción de la tarea
        Text(
            text = task.description ?: "No description available.",
            color = Color.LightGray,
            style = MaterialTheme.typography.bodySmall
        )

        // Etiquetas (si las hay)
        if (labels.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                labels.forEach { label ->
                    LabelTag(label = label)
                }
            }
        }
    }
}

@Composable
fun LabelTag(label: LabelEntity) {
    val backgroundColor = try {
        Color(android.graphics.Color.parseColor(label.hexColor))
    } catch (e: IllegalArgumentException) {
        Color.Gray
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = label.description,
            color = Color.White,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

fun calculateDueDateColor(dueDate: Date): Color {
    val localDueDate = dueDate.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    val now = java.time.LocalDate.now()
    val daysDifference = java.time.temporal.ChronoUnit.DAYS.between(now, localDueDate)

    return when {
        daysDifference <= 2 -> Color.Red
        daysDifference <= 7 -> Color.Yellow
        else -> Color.Green
    }
}

@Composable
fun OrderButtons(
    orderBy: List<String>,
    currentOrder: Int = 0,
    onOrderChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        orderBy.forEachIndexed { index, order ->
            Button(
                onClick = { onOrderChange(order) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentOrder == index) Color.Gray else Color.DarkGray
                )
            ) {
                Text(order)
            }
        }
    }
}

fun formatDueDateAndTime(dueDate: Date): String {
    val localDateTime = dueDate.toInstant().atZone(ZoneId.systemDefault())
        .toLocalDateTime()
    val formatter =
        DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a")
    return localDateTime.format(formatter)
}


