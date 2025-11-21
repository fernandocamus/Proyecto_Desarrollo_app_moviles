package com.example.readme_grupo11.repository

import com.example.readme_grupo11.api.RetrofitClient
import com.example.readme_grupo11.model.LoginRequest
import com.example.readme_grupo11.model.LoginResponse
import com.example.readme_grupo11.model.UsuarioRegistrado
import java.io.IOException

class UserRepository {
    private val apiService = RetrofitClient.instance

    suspend fun login(correo: String, contrasena: String): LoginResponse {
        val response = apiService.login(LoginRequest(correo, contrasena))
        if (response.isSuccessful) {
            return response.body() ?: throw IOException("Respuesta de login vacía")
        } else {
            throw IOException(response.errorBody()?.string() ?: "Error desconocido en el login")
        }
    }

    suspend fun registrarUsuario(usuario: UsuarioRegistrado): UsuarioRegistrado {
        val response = apiService.registrarUsuario(usuario)
        if (response.isSuccessful) {
            return response.body() ?: throw IOException("Respuesta de registro vacía")
        } else {
            throw IOException(response.errorBody()?.string() ?: "Error desconocido en el registro")
        }
    }
}
