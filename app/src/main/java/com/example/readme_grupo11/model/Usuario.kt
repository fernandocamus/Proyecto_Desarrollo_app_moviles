package com.example.readme_grupo11.model

data class UsuarioUiState(
    val nombreCompleto: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val confirmarContrasena: String = "",
    val telefono: String = "",
    val generosFavoritos: List<GeneroLiterario> = emptyList(),
)
data class UsuarioErrores(
    val nombreError: String? = null,
    val correoError: String? = null,
    val contrasenaError: String? = null,
    val confirmarContrasenaError: String? = null,
    val telefonoError: String? = null,
    val generosError: String? = null,
)
enum class GeneroLiterario(val displayName: String) {
    FICCION("Ficción"),
    NO_FICCION("No Ficción"),
    MISTERIO("Misterio"),
    TERROR("Terror"),
    SUSPENSO("Suspenso"),
    HISTORIA("Historia")
}
data class LoginUiState(
    val correo: String = "",
    val contrasena: String = "",
)
data class LoginErrores(
    val correoError: String? = null,
    val contrasenaError: String? = null,
    val errorGeneral: String? = null
)
data class RecuperacionUiState(
    val correo: String = ""
)
data class UsuarioRegistrado(
    val nombreCompleto: String,
    val correo: String,
    val contrasena: String,
    val telefono: String,
    val generosFavoritos: List<GeneroLiterario>
)