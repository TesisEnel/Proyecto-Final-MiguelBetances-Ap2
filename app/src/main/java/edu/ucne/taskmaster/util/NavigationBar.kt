package edu.ucne.taskmaster.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.sharp.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import edu.ucne.taskmaster.navigation.Screen

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    showBottomBar: Boolean,
    content: @Composable () -> Unit
) {
    val items = listOf(
        NavigationItem(
            title = "Calendar",
            selectedIcon = Icons.Filled.DateRange,
            unselectedIcon = Icons.Outlined.DateRange
        ),
        NavigationItem(
            title = "Tasks",
            selectedIcon = Icons.AutoMirrored.Filled.List,
            unselectedIcon = Icons.AutoMirrored.Outlined.List
        ),
        NavigationItem(
            title = "Labels",
            selectedIcon = Icons.Sharp.MoreVert,
            unselectedIcon = Icons.Sharp.MoreVert
        )
    )
    var selectedItem by remember { mutableStateOf(0) }

    androidx.compose.foundation.layout.Column {
        androidx.compose.foundation.layout.Box(modifier = Modifier.weight(1f)) {
            content()
        }

        // Renderiza la BottomNavigationBar solo si `showBottomBar` es true
        if (showBottomBar) {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (selectedItem == index) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        label = { Text(text = item.title) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            when (item.title) {
                                "Calendar" -> navController.navigate(Screen.Calendar)
                                "Tasks" -> navController.navigate(Screen.TaskList)
                                "Labels" -> navController.navigate(Screen.LabelList)
                            }
                        }
                    )
                }
            }
        }
    }
}

