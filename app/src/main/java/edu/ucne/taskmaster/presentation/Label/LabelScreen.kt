package edu.ucne.taskmaster.presentation.Label

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.taskmaster.util.HueBar
import edu.ucne.taskmaster.util.hexToColor
import edu.ucne.taskmaster.util.toHexString

@Composable
fun LabelScreen(
    viewModel: LabelViewModel = hiltViewModel(),
    labelId: Int,
    goBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = labelId) {
        if (labelId != 0) {
            viewModel.onEvent(LabelUiEvent.GetLabel(labelId))
        }
    }

    LabelScreen(
        labelId = labelId,
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goBack = goBack
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
private fun LabelScreen(
    labelId: Int,
    uiState: LabelUiState,
    onEvent: (LabelUiEvent) -> Unit,
    goBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (labelId == 0) "Crear Label" else "Modificar Label") },
                navigationIcon = {
                    IconButton(onClick = goBack) {
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
                        onEvent(LabelUiEvent.Validate)
                        if (uiState.description != "") {
                            onEvent(LabelUiEvent.SaveLabel)
                            goBack()
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        imageVector = if (labelId == 0) Icons.Default.Add else Icons.Default.Done,
                        contentDescription = null
                    )
                    Text(text = if (labelId == 0) "Crear" else "Modificar")
                }
                if (labelId != 0) {
                    Button(
                        onClick = {
                            onEvent(LabelUiEvent.DeleteLabel(labelId))
                            goBack()
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
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                OutlinedTextField(
                    label = { Text("Descripción") },
                    value = uiState.description,
                    onValueChange = { onEvent(LabelUiEvent.DescriptionChange(it)) },
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.descriptionError != null) {
                    Text(
                        text = uiState.descriptionError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clearAndSetSemantics { },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Color de la etiqueta",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .border(1.dp, Color.Black, CircleShape)
                            .background(uiState.hexColor.hexToColor(), CircleShape)
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Seleccionar color:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    HueBar { hue ->
                        val selectedColor =
                            Color(android.graphics.Color.HSVToColor(floatArrayOf(hue, 1f, 1f)))
                        onEvent(LabelUiEvent.ColorChange(selectedColor.toHexString()))
                    }
                }
            }

        }
    }
}


