package com.example.readme_grupo11.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readme_grupo11.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ViewModel para la logica de pantalla de registro
class RegistroViewModel : ViewModel() {

    //Estado del formulario de registro
    private val _uiState = MutableStateFlow(UsuarioUiState())
    // Estado de UI
    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    //Estado para los errores de validacion
    private val _errores = MutableStateFlow(UsuarioErrores())
    // Estado de lectura de los errores
    val errores: StateFlow<UsuarioErrores> = _errores.asStateFlow()

    // Estado para ver si el registro fue exitoso
    private val _registroExitoso = MutableStateFlow(false)
    val registroExitoso: StateFlow<Boolean> = _registroExitoso.asStateFlow()

    // Funcion para actualizar el nombre del usuario y validacion
    fun actualizarNombre(nombre: String) {
        _uiState.value = _uiState.value.copy(nombreCompleto = nombre)
        validarNombre(nombre)
    }

    //Funcion para actualizar el correo y validacion
    fun actualizarCorreo(correo: String) {
        _uiState.value = _uiState.value.copy(correo = correo)
        validarCorreo(correo)
    }

    // Funcion para actualizar la contraseña y validacion
    fun actualizarContrasena(contrasena: String) {
        _uiState.value = _uiState.value.copy(contrasena = contrasena)
        validarContrasena(contrasena)
    }

    // Funcion para actualizar la confirmacion de contraseña y validar que coincide con la contraseña
    fun actualizarConfirmarContrasena(confirmar: String) {
        _uiState.value = _uiState.value.copy(confirmarContrasena = confirmar)
        validarConfirmarContrasena(confirmar)
    }

    // Funcion para actualizar el telefono y validar el formato
    fun actualizarTelefono(telefono: String) {
        _uiState.value = _uiState.value.copy(telefono = telefono)
        validarTelefono(telefono)
    }

    // Funcion para elegir un genero literario favorito
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

    // Funcion para las validaciones del nombre
    private fun validarNombre(nombre: String) {
        val error = when {
            // Verificar que no este vacio
            nombre.isBlank() -> "El nombre es obligatorio"
            // Verificar que como maximo debe tener 100 caracteres
            nombre.length > 100 -> "El nombre no puede exceder 100 caracteres"
            // Verificar que solo debe tener letras y espacios
            !nombre.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) -> "El nombre solo puede contener letras y espacios"
            else -> null
        }
        _errores.value = _errores.value.copy(nombreError = error)
    }

    // Funcion para las validaciones del correo
    private fun validarCorreo(correo: String) {
        val error = when {
            // Verificar que no este vacio
            correo.isBlank() -> "El correo es obligatorio"
            // Verificar que tenga @duoc.cl
            !correo.endsWith("@duoc.cl") -> "Solo se permiten correos @duoc.cl"
            // Verificar que como maximo debe tener 60 caracteres
            correo.length > 60 -> "El correo no puede exceder 60 caracteres"
            // Verificar que debe tener el formato
            !correo.matches(Regex("^[a-zA-Z0-9._%+-]+@duoc\\.cl$")) -> "Formato de correo inválido"
            else -> null
        }
        _errores.value = _errores.value.copy(correoError = error)
    }

    // Funcion para las validaciones de la contraseña
    private fun validarContrasena(contrasena: String) {
        val error = when {
            // Verificar que no este vacia
            contrasena.isBlank() -> "La contraseña es obligatoria"
            // Verificar que debe tener al menos 10 caracteres
            contrasena.length < 10 -> "La contraseña debe tener al menos 10 caracteres"
            // Verificar que tenga minimo una Mayus
            !contrasena.contains(Regex("[A-Z]")) -> "Debe contener al menos una mayúscula"
            // Verificar que tenga minimo una Minus
            !contrasena.contains(Regex("[a-z]")) -> "Debe contener al menos una minúscula"
            // Verificar que tenga minimo un Num
            !contrasena.contains(Regex("[0-9]")) -> "Debe contener al menos un número"
            // Verificar que tenga minimo un caracter especial
            !contrasena.contains(Regex("[@#$%&*!?]")) -> "Debe contener un carácter especial (@#$%&*!?)"
            else -> null
        }
        _errores.value = _errores.value.copy(contrasenaError = error)
    }

    //Funcion para validar que la contraseña coincida con la originak
    private fun validarConfirmarContrasena(confirmar: String) {
        val error = when {
            confirmar.isBlank() -> "Debe confirmar la contraseña"
            confirmar != _uiState.value.contrasena -> "Las contraseñas no coinciden"
            else -> null
        }
        _errores.value = _errores.value.copy(confirmarContrasenaError = error)
    }

    //Funcion para validar el telefono (solo si se ingresa)
    private fun validarTelefono(telefono: String) {
        // Verificar que solo tenga numeros mas el simbolo +
        val error = if (telefono.isNotBlank() && !telefono.matches(Regex("^[0-9+]+$"))) {
            "El teléfono solo puede contener números"
        } else null
        _errores.value = _errores.value.copy(telefonoError = error)
    }

    // Funcion para validar que se seleccione al menos 1 genero
    private fun validarGeneros(generos: List<GeneroLiterario>) {
        val error = if (generos.isEmpty()) {
            "Debe seleccionar al menos un género favorito"
        } else null
        _errores.value = _errores.value.copy(generosError = error)
    }

    // Valida que el formulario ejecute las validaciones
    fun validarFormulario(): Boolean {
        val state = _uiState.value

        // Ejecucion de las validaciones
        validarNombre(state.nombreCompleto)
        validarCorreo(state.correo)
        validarContrasena(state.contrasena)
        validarConfirmarContrasena(state.confirmarContrasena)
        validarTelefono(state.telefono)
        validarGeneros(state.generosFavoritos)

        // V erificacion de que no haya ningun error
        val erroresActuales = _errores.value
        return erroresActuales.nombreError == null &&
                erroresActuales.correoError == null &&
                erroresActuales.contrasenaError == null &&
                erroresActuales.confirmarContrasenaError == null &&
                erroresActuales.telefonoError == null &&
                erroresActuales.generosError == null
    }

    // Resetear el estado de registro exitoso, para que cuando se registre un nuevo usuario popee de nuevo
    fun resetRegistroExitoso() {
        _registroExitoso.value = false
    }
}