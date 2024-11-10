package edu.ucne.taskmaster.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.ucne.taskmaster.presentation.Calendar.CalendarScreen

@Composable
fun NavHostApp(navHostController: NavHostController, drawerState: DrawerState) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Calendar
    ) {
        composable<Screen.Calendar> {
            CalendarScreen()
        }

    }
}