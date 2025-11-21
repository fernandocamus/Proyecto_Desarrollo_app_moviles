package com.example.readme_grupo11.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readme_grupo11.model.*
import com.example.readme_grupo11.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class RegistroViewModel : ViewModel() {

    private val userRepository = UserRepository()

    private val _uiState = MutableStateFlow(UsuarioUiState())
    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    private val _errores = MutableStateFlow(UsuarioErrores())
    val errores: StateFlow<UsuarioErrores> = _errores.asStateFlow()

    private val _registroExitoso = MutableStateFlow(false)
    val registroExitoso: StateFlow<Boolean> = _registroExitoso.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

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

    fun actualizarFotoPerfilUri(uri: Uri?) {
        _uiState.value = _uiState.value.copy(fotoPerfilUri = uri.toString())
    }

    fun registrarUsuario(onSuccess: () -> Unit) {
        if (validarFormulario()) {
            viewModelScope.launch {
                _isLoading.value = true
                _errores.value = _errores.value.copy(errorGeneral = null) // Limpiar error previo
                try {
                    val state = _uiState.value
                    val usuario = UsuarioRegistrado(
                        nombreCompleto = state.nombreCompleto,
                        correo = state.correo,
                        contrasena = state.contrasena,
                        telefono = state.telefono.ifEmpty { null },
                        generosFavoritos = state.generosFavoritos.map { it.name },
                        fotoPerfil = state.fotoPerfilUri
                    )

                    userRepository.registrarUsuario(usuario)
                    _registroExitoso.value = true
                    onSuccess()

                } catch (e: IOException) {
                    _errores.value = _errores.value.copy(errorGeneral = e.message)
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun limpiarErrorGeneral() {
        _errores.value = _errores.value.copy(errorGeneral = null)
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
