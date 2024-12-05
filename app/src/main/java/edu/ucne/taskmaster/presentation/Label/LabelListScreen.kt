package edu.ucne.taskmaster.presentation.Label

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.taskmaster.data.local.entities.LabelEntity
import edu.ucne.taskmaster.util.hexToColor

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LabelListScreen(
    viewModel: LabelViewModel = hiltViewModel(),
    onAddLabel: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var labelToDelete by remember { mutableStateOf<LabelEntity?>(null) } // State for selected label to delete

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Labels") },
                actions = {
                    IconButton(onClick = { onAddLabel() }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Label")
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    if (uiState.error != null) {
                        Text(text = "Error: ${uiState.error}", color = Color.Red)
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(uiState.labels) { label ->
                            LabelItem(
                                label = label,
                                onDelete = { labelToDelete = label }
                            )
                        }
                    }
                }
            }
        }
    )

    // Confirmation Dialog
    if (labelToDelete != null) {
        AlertDialog(
            onDismissRequest = { labelToDelete = null },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete the label '${labelToDelete?.description}'? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteLabel(labelToDelete!!.id!!)
                    labelToDelete = null // Reset the selected label
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { labelToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun LabelItem(
    label: LabelEntity,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = label.description,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(
                                color = label.hexColor.hexToColor(),
                                shape = CircleShape
                            )
                            .border(1.dp, Color.Black, CircleShape)
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
