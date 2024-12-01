package edu.ucne.taskmaster.presentation.sign_in

data class UserUiState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String = "",
    val usuarioId: Int? = null,
    val nombre: String? = null,
    val telefono: String? = null,
    val correo: String? = null,
    val contrasena: String? = null,
    val profilePictureUrl: String? = null
)