package edu.ucne.taskmaster.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.DateRange
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
    content: @Composable () -> Unit
) {
    val items = listOf(
        NavigationItem(
            title = "Calendar",
            selectedIcon = Icons.Filled.DateRange,
            unselectedIcon = Icons.Outlined.DateRange
        ),
        NavigationItem(
            title = "Add",
            selectedIcon = Icons.Filled.Add,
            unselectedIcon = Icons.Outlined.Add
        )
    )
    var selectedItem by remember { mutableStateOf(0) }

    // The main layout with the BottomNavigationBar and the content
    androidx.compose.foundation.layout.Column {
        // Render main content above the navigation bar
        androidx.compose.foundation.layout.Box(modifier = Modifier.weight(1f)) {
            content()
        }

        // Bottom navigation bar
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
                        }
                    }
                )
            }
        }
    }
}
