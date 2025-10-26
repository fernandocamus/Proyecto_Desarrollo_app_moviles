package com.example.readme_grupo11.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readme_grupo11.model.RecuperacionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ViewModel para la logica de recuperacion de contraseña
class RecuperacionViewModel : ViewModel() {

    // Estado del formulario de recuperacion de contraseña
    private val _uiState = MutableStateFlow(RecuperacionUiState())
    val uiState: StateFlow<RecuperacionUiState> = _uiState.asStateFlow()

    // Estado para los errorres ede validacion
    private val _correoError = MutableStateFlow<String?>(null)
    val correoError: StateFlow<String?> = _correoError.asStateFlow()

    // Estado para ver si fue enviado correctamente
    private val _envioExitoso = MutableStateFlow(false)
    val envioExitoso: StateFlow<Boolean> = _envioExitoso.asStateFlow()

    // Funcion para ingresar el correo y validarlo
    fun actualizarCorreo(correo: String) {
        _uiState.value = _uiState.value.copy(correo = correo)
        validarCorreo(correo)
    }

    // Funcion para validar el formato del correo
    private fun validarCorreo(correo: String) {
        _correoError.value = when {
            // Verificar que no este vacio
            correo.isBlank() -> "El correo es obligatorio"
            // Verificar que sea un correo @duoc.cl
            !correo.endsWith("@duoc.cl") -> "Debe usar un correo @duoc.cl"
            // Verificar el formato correcto
            !correo.matches(Regex("^[a-zA-Z0-9._%+-]+@duoc\\.cl$")) -> "Formato de correo inválido"
            else -> null
        }
    }

    // Funcion para enviar el correo de recuperacion (SOLO SIMULACION, NO FUNCIONAL)
    fun enviarRecuperacion() {
        val correo = _uiState.value.correo
        validarCorreo(correo)

        // verifica que no haya errores, si no hay, se simula el envio de manera correcta
        if (_correoError.value == null) {
            _envioExitoso.value = true
        }
    }

    // Funcion para reiniciar el estado de envio
    fun resetEnvioExitoso() {
        _envioExitoso.value = false
    }
}