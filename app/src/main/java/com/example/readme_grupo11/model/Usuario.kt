package com.example.readme_grupo11.model

// Campos que el usuario debe completar en el registro
data class UsuarioUiState(
    val nombreCompleto: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val confirmarContrasena: String = "",
    val telefono: String = "",
    val generosFavoritos: List<GeneroLiterario> = emptyList(),
    val fotoPerfilUri: String? = null
)

// Errores de cada campo de registro
data class UsuarioErrores(
    val nombreError: String? = null,
    val correoError: String? = null,
    val contrasenaError: String? = null,
    val confirmarContrasenaError: String? = null,
    val telefonoError: String? = null,
    val generosError: String? = null,
)

// Generos literarios disponibles
enum class GeneroLiterario(val displayName: String) {
    FICCION("Ficción"),
    NO_FICCION("No Ficción"),
    MISTERIO("Misterio"),
    TERROR("Terror"),
    SUSPENSO("Suspenso"),
    HISTORIA("Historia")
}

// Campos necesarios para iniciar sesion
data class LoginUiState(
    val correo: String = "",
    val contrasena: String = "",
)

// Errores en inicio de sesion
data class LoginErrores(
    val correoError: String? = null,
    val contrasenaError: String? = null,
    val errorGeneral: String? = null
)

// Campo correo de usuario
data class RecuperacionUiState(
    val correo: String = ""
)

// Clase para guardar los datos de usuario en la base de datos
data class UsuarioRegistrado(
    val nombreCompleto: String,
    val correo: String,
    val contrasena: String,
    val telefono: String,
    val generosFavoritos: List<GeneroLiterario>
)