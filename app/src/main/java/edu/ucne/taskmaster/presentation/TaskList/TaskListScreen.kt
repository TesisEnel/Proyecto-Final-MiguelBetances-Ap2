package edu.ucne.taskmaster.presentation.TaskList

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.taskmaster.data.local.entities.LabelEntity
import edu.ucne.taskmaster.data.local.entities.TaskEntity
import edu.ucne.taskmaster.util.hexToColor
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel = hiltViewModel(),
    createTask: () -> Unit,
    onTaskClick: (Int) -> Unit,
    onEvent: (TaskListUiEvent) -> Unit = viewModel::onEvent
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var isBottomSheetVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task List") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = { isBottomSheetVisible = !isBottomSheetVisible }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = createTask,
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar tarea")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TaskListBody(
                uiState = uiState,
                onTaskClick = onTaskClick,
                onOrderChange = { order -> onEvent(TaskListUiEvent.OnOrderChange(order)) }
            )

            if (isBottomSheetVisible) {
                TaskFilterModal(
                    sheetState = sheetState,
                    unShowBottomSheet = {
                        coroutineScope.launch {
                            isBottomSheetVisible = false
                            sheetState.hide()
                        }
                    },
                    uiState = uiState,
                    onTextChange = { viewModel.onEvent(TaskListUiEvent.SearchQueryChange(it)) },
                    onToggleLabel = { viewModel.onEvent(TaskListUiEvent.FilterLabelToggle(it)) },
                    onApplyFilters = { viewModel.onEvent(TaskListUiEvent.ApplyFilters) },
                    onResetFilters = { viewModel.onEvent(TaskListUiEvent.ResetFilters) }
                )
            }
        }
    }
}


@Composable
fun TaskListBody(
    uiState: TaskListUIState,
    onTaskClick: (Int) -> Unit,
    onOrderChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()

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
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable { onTaskClick(task.taskId!!) }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = task.title ?: "Untitled Task",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Due Date: " + formatDueDateAndTime(task.dueDate),
            color = dueDateColor,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = task.description ?: "No description available.",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = "Priority: ${getPriorityLabel(task.priority)}",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.bodySmall
        )


        if (labels.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                labels.forEach { label -> LabelTag(label = label) }
            }
        }
    }
}

@Composable
fun LabelTag(label: LabelEntity) {
    val backgroundColor = try {
        Color(android.graphics.Color.parseColor(label.hexColor))
    } catch (e: IllegalArgumentException) {
        MaterialTheme.colorScheme.secondaryContainer
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Row {
            Text(
                text = label.description,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.bodySmall
            )
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(
                        color = label.hexColor.hexToColor(),
                        shape = CircleShape
                    )
                    .border(1.dp, Color.Black, CircleShape)
                    .align(Alignment.CenterVertically)
            )

        }

    }
}


@Composable
fun calculateDueDateColor(dueDate: Date): Color {
    val localDueDate = dueDate.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    val now = java.time.LocalDate.now()
    val daysDifference = java.time.temporal.ChronoUnit.DAYS.between(now, localDueDate)

    return when {
        daysDifference <= 2 -> Color.Red
        daysDifference <= 7 -> Color(186, 142, 35)
        else -> Color(65, 171, 39)
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

fun getPriorityLabel(priority: Int): String {
    val priorityOptions: List<String> = listOf(
        "Urgente",
        "Alta",
        "Media",
        "Baja",
        "Sin prisa"
    )
    return if (priority in priorityOptions.indices) {
        priorityOptions[priority]
    } else {
        "Desconocido"
    }
}


