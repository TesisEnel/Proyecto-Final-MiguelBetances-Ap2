package edu.ucne.taskmaster.navigation

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.taskmaster.presentation.Calendar.CalendarScreen
import edu.ucne.taskmaster.presentation.Label.LabelListScreen
import edu.ucne.taskmaster.presentation.Label.LabelScreen
import edu.ucne.taskmaster.presentation.TaskList.TaskListScreen
import edu.ucne.taskmaster.presentation.TaskList.TaskScreen
import edu.ucne.taskmaster.presentation.sign_in.GoogleAuthUiClient
import edu.ucne.taskmaster.presentation.sign_in.LoginScreen
import edu.ucne.taskmaster.presentation.sign_in.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun NavHostApp(
    navHostController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient,
    onScreenChange: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    NavHost(
        navController = navHostController,
        startDestination = Screen.LoginScreen
    ) {

        composable<Screen.Calendar> {
            CalendarScreen()
            onScreenChange()
        }
        composable<Screen.TaskList> {
            TaskListScreen(
                createTask = {
                    navHostController.navigate(Screen.Task(0))
                },
                onTaskClick = {
                    navHostController.navigate(Screen.Task(it))
                },
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

        composable<Screen.LabelList> {
            LabelListScreen(
                onAddLabel = {
                    navHostController.navigate(Screen.Label(0))
                }
            )
        }

        composable<Screen.Label> {
            val args = it.toRoute<Screen.Label>()
            LabelScreen(
                labelId = args.id,
                goBack = {
                    navHostController.popBackStack()
                }
            )
        }

        composable<Screen.LoginScreen> {
            val viewModel: UserViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == RESULT_OK) {
                        scope.launch {
                            val signInResult =
                                googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                            viewModel.onSignInResult(signInResult)
                            navHostController.navigate(Screen.Calendar) {
                                popUpTo(Screen.LoginScreen) { inclusive = true }
                            }
                        }
                    }
                }
            )

            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if (state.isSignInSuccessful) {
                    Toast.makeText(
                        context,
                        "Sign in successful",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            LoginScreen(
                onLoginClicked = { navHostController.navigate(Screen.Calendar) },
                onGoogleSignInClicked = {
                    scope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                        if (state.isSignInSuccessful)
                            navHostController.navigate(Screen.Calendar)
                    }
                }
            )
        }

    }
}