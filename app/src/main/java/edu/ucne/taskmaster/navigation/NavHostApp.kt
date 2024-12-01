package edu.ucne.taskmaster.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.taskmaster.presentation.Calendar.CalendarScreen
import edu.ucne.taskmaster.presentation.Label.LabelScreen
import edu.ucne.taskmaster.presentation.TaskList.TaskListScreen
import edu.ucne.taskmaster.presentation.TaskList.TaskScreen

@Composable
fun NavHostApp(navHostController: NavHostController, drawerState: DrawerState) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Calendar
    ) {
        composable<Screen.Calendar> {
            CalendarScreen()
        }
        composable<Screen.TaskList> {
            TaskListScreen(
                createTask = {
                    navHostController.navigate(Screen.Task(0))
                },
                gotoTask = {
                    navHostController.navigate(Screen.Task(it))
                },
                gotoEditTask = {
                    navHostController.navigate(Screen.Task(it))
                }
            )
        }

        composable<Screen.Task> {
            val args = it.toRoute<Screen.Task>()
            TaskScreen(
                taskId = args.id,
                goBackToTaskList = {
                    navHostController.navigate(Screen.TaskList)
                }
            )
        }

        composable<Screen.Label> {
            LabelScreen()
        }

    }
}