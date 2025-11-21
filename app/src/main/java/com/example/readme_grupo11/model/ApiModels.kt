package com.example.readme_grupo11.model

/**
 * Representa el cuerpo de la solicitud para el endpoint de login.
 *
 * @property correo Correo electrónico del usuario.
 * @property contrasena Contraseña del usuario.
 */
data class LoginRequest(
    val correo: String,
    val contrasena: String
)

/**
 * Representa la respuesta del servidor tras un login exitoso.
 *
 * @property token Token de autenticación JWT.
 * @property user Objeto con la información del usuario registrado.
 */
data class LoginResponse(
    val token: String,
    val user: UsuarioRegistrado
)
