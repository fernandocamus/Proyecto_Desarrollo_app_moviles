package com.example.readme_grupo11.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readme_grupo11.model.LoginErrores
import com.example.readme_grupo11.model.LoginUiState
import com.example.readme_grupo11.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import kotlinx.coroutines.delay

// ViewModel para la logica de la pantalla de login
class LoginViewModel : ViewModel() {

    private val userRepository = UserRepository()

    // Estado del formulario del login
    private val _uiState = MutableStateFlow(LoginUiState())
    // Estado de UI
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Estado para los errores de validacion
    private val _errores = MutableStateFlow(LoginErrores())
    // Estado de lectura de los errores
    val errores: StateFlow<LoginErrores> = _errores.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Funcion para actualizar el correo electronico ingresado y validarlo
    fun actualizarCorreo(correo: String) {
        _uiState.value = _uiState.value.copy(correo = correo)
        validarCorreo(correo)
    }

    // Funcion para actualizar la contraseña y validarla
    fun actualizarContrasena(contrasena: String) {
        _uiState.value = _uiState.value.copy(contrasena = contrasena)
        validarContrasena(contrasena)
    }

    // Funcion para validar el formato del correo
    private fun validarCorreo(correo: String) {
        val error = when {
            // Verificar que no este vacio
            correo.isBlank() -> "El correo es obligatorio"
            // Verificar que sea un correo @duoc.cl
            !correo.endsWith("@duoc.cl") -> "Debe usar un correo @duoc.cl"
            // Verificar el formato correcto
            !correo.matches(Regex("^[a-zA-Z0-9._%+-]+@duoc\\.cl$")) -> "Formato de correo inválido"
            else -> null
        }
        _errores.value = _errores.value.copy(correoError = error)
    }

    // Funcion para validar que la contraseña no este vacia (Solo validamos eso ya que la contraseña ya esta creada con las validaciones)
    private fun validarContrasena(contrasena: String) {
        val error = if (contrasena.isBlank()) {
            "La contraseña es obligatoria"
        } else null
        _errores.value = _errores.value.copy(contrasenaError = error)
    }

    // Funcion para validar los campos del login
    fun validarFormulario(): Boolean {
        val state = _uiState.value
        validarCorreo(state.correo)
        validarContrasena(state.contrasena)

        return _errores.value.correoError == null &&
                _errores.value.contrasenaError == null
    }

    // Funcion para iniciar sesion con las credenciales
    fun iniciarSesion(onSuccess: () -> Unit) {
        if (validarFormulario()) {
            viewModelScope.launch {
                _isLoading.value = true
                _errores.value = LoginErrores() // Limpiar errores previos

                try {
                    delay(2000)
                    userRepository.login(_uiState.value.correo, _uiState.value.contrasena)
                    onSuccess()
                } catch (e: IOException) {
                    _errores.value = _errores.value.copy(errorGeneral = e.message)
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    // Se limpia el mensaje de error general
    fun limpiarErrorGeneral() {
        _errores.value = _errores.value.copy(errorGeneral = null)
    }
}