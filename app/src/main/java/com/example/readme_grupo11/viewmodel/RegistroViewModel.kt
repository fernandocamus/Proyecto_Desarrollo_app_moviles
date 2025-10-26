package com.example.readme_grupo11.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readme_grupo11.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegistroViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioUiState())
    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    private val _errores = MutableStateFlow(UsuarioErrores())
    val errores: StateFlow<UsuarioErrores> = _errores.asStateFlow()

    private val _registroExitoso = MutableStateFlow(false)
    val registroExitoso: StateFlow<Boolean> = _registroExitoso.asStateFlow()

    fun actualizarNombre(nombre: String) {
        _uiState.value = _uiState.value.copy(nombreCompleto = nombre)
        validarNombre(nombre)
    }


    fun actualizarCorreo(correo: String) {
        _uiState.value = _uiState.value.copy(correo = correo)
        validarCorreo(correo)
    }

    fun actualizarContrasena(contrasena: String) {
        _uiState.value = _uiState.value.copy(contrasena = contrasena)
        validarContrasena(contrasena)
    }

    fun actualizarConfirmarContrasena(confirmar: String) {
        _uiState.value = _uiState.value.copy(confirmarContrasena = confirmar)
        validarConfirmarContrasena(confirmar)
    }

    fun actualizarTelefono(telefono: String) {
        _uiState.value = _uiState.value.copy(telefono = telefono)
        validarTelefono(telefono)
    }

    fun toggleGenero(genero: GeneroLiterario) {
        val generosActuales = _uiState.value.generosFavoritos.toMutableList()
        if (generosActuales.contains(genero)) {
            generosActuales.remove(genero)
        } else {
            generosActuales.add(genero)
        }
        _uiState.value = _uiState.value.copy(generosFavoritos = generosActuales)
        validarGeneros(generosActuales)
    }

    private fun validarNombre(nombre: String) {
        val error = when {
            nombre.isBlank() -> "El nombre es obligatorio"
            nombre.length > 100 -> "El nombre no puede exceder 100 caracteres"
            !nombre.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) -> "El nombre solo puede contener letras y espacios"
            else -> null
        }
        _errores.value = _errores.value.copy(nombreError = error)
    }

    private fun validarCorreo(correo: String) {
        val error = when {
            correo.isBlank() -> "El correo es obligatorio"
            !correo.endsWith("@duoc.cl") -> "Solo se permiten correos @duoc.cl"
            correo.length > 60 -> "El correo no puede exceder 60 caracteres"
            !correo.matches(Regex("^[a-zA-Z0-9._%+-]+@duoc\\.cl$")) -> "Formato de correo inválido"
            else -> null
        }
        _errores.value = _errores.value.copy(correoError = error)
    }

    private fun validarContrasena(contrasena: String) {
        val error = when {
            contrasena.isBlank() -> "La contraseña es obligatoria"
            contrasena.length < 10 -> "La contraseña debe tener al menos 10 caracteres"
            !contrasena.contains(Regex("[A-Z]")) -> "Debe contener al menos una mayúscula"
            !contrasena.contains(Regex("[a-z]")) -> "Debe contener al menos una minúscula"
            !contrasena.contains(Regex("[0-9]")) -> "Debe contener al menos un número"
            !contrasena.contains(Regex("[@#$%&*!?]")) -> "Debe contener un carácter especial (@#$%&*!?)"
            else -> null
        }
        _errores.value = _errores.value.copy(contrasenaError = error)
    }

    private fun validarConfirmarContrasena(confirmar: String) {
        val error = when {
            confirmar.isBlank() -> "Debe confirmar la contraseña"
            confirmar != _uiState.value.contrasena -> "Las contraseñas no coinciden"
            else -> null
        }
        _errores.value = _errores.value.copy(confirmarContrasenaError = error)
    }

    private fun validarTelefono(telefono: String) {
        val error = if (telefono.isNotBlank() && !telefono.matches(Regex("^[0-9+]+$"))) {
            "El teléfono solo puede contener números"
        } else null
        _errores.value = _errores.value.copy(telefonoError = error)
    }

    private fun validarGeneros(generos: List<GeneroLiterario>) {
        val error = if (generos.isEmpty()) {
            "Debe seleccionar al menos un género favorito"
        } else null
        _errores.value = _errores.value.copy(generosError = error)
    }

    fun validarFormulario(): Boolean {
        val state = _uiState.value

        validarNombre(state.nombreCompleto)
        validarCorreo(state.correo)
        validarContrasena(state.contrasena)
        validarConfirmarContrasena(state.confirmarContrasena)
        validarTelefono(state.telefono)
        validarGeneros(state.generosFavoritos)

        val erroresActuales = _errores.value
        return erroresActuales.nombreError == null &&
                erroresActuales.correoError == null &&
                erroresActuales.contrasenaError == null &&
                erroresActuales.confirmarContrasenaError == null &&
                erroresActuales.telefonoError == null &&
                erroresActuales.generosError == null
    }

    fun resetRegistroExitoso() {
        _registroExitoso.value = false
    }
}