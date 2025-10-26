package com.example.readme_grupo11.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readme_grupo11.model.LoginErrores
import com.example.readme_grupo11.model.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ViewModel para la logica de la pantalla de login
class LoginViewModel : ViewModel() {

    // Estado del formulario del login
    private val _uiState = MutableStateFlow(LoginUiState())
    // Estado de UI
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Estado para los errores de validacion
    private val _errores = MutableStateFlow(LoginErrores())
    // Estado de lectura de los errores
    val errores: StateFlow<LoginErrores> = _errores.asStateFlow()

    // Simulacion de base de datos para las credenciales d elos usuarios
    companion object {
        val usuariosGuardados = mutableMapOf(
            "alumno@duoc.cl" to "Password123@",
            "profesor@duoc.cl" to "Clave2025#"
        )

        // Funcion para mostrar los usuarios en el logcat
        fun mostrarUsuarios() {
            Log.d("ZONALIBROS", "===========================================")
            Log.d("ZONALIBROS", "📋 USUARIOS GUARDADOS:")
            Log.d("ZONALIBROS", "===========================================")
            usuariosGuardados.forEach { (email, password) ->
                Log.d("ZONALIBROS", "Email: $email")
                Log.d("ZONALIBROS", "Pass:  $password")
                Log.d("ZONALIBROS", "-------------------------------------------")
            }
            Log.d("ZONALIBROS", "Total: ${usuariosGuardados.size} usuarios")
            Log.d("ZONALIBROS", "===========================================")
        }
    }

    // Inicializacion para mostrar los usuarios en el logcat
    init {
        mostrarUsuarios()
    }

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
        // Se limpian los errores de intentos anteriores
        if (validarFormulario()) {
            viewModelScope.launch {
                _errores.value = _errores.value.copy(errorGeneral = null)

                val state = _uiState.value

                // Se busca la contraseña asociada al correo ingresado
                val contrasenaCorrecta = usuariosGuardados[state.correo]

                // Se verifica si el usuario existe y si la contraseña es correcta
                if (contrasenaCorrecta != null && contrasenaCorrecta == state.contrasena) {
                    onSuccess()
                } else {
                    _errores.value = _errores.value.copy(
                        errorGeneral = "Credenciales incorrectas. Verifica tu correo y contraseña."
                    )
                }
            }
        }
    }

    // Se limpia el mensaje de error general
    fun limpiarErrorGeneral() {
        _errores.value = _errores.value.copy(errorGeneral = null)
    }

}