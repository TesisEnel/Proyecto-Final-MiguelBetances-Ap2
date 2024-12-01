package edu.ucne.taskmaster.presentation.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(

) : ViewModel() {


    private val _uiState = MutableStateFlow(UserUiState())
    val uiState = _uiState.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSignInSuccessful = result.data != null,
                    signInError = result.errorMessage ?: "",
                    usuarioId = null,
                    nombre = result.data?.userName,
                    telefono = result.data?.phoneNumber,
                    correo = result.data?.email,
                    contrasena = result.data?.password,
                    profilePictureUrl = result.data?.profilePictureUrl
                )
            }
        }
    }
}