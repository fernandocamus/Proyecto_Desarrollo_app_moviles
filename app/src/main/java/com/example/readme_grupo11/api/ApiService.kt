package com.example.readme_grupo11.api

import com.example.readme_grupo11.model.LoginRequest
import com.example.readme_grupo11.model.LoginResponse
import com.example.readme_grupo11.model.UsuarioRegistrado
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    // Endpoint para el login, relativo a la BASE_URL. La ruta completa será: http://10.0.2.2:8080/api/login
    @POST("/api/usuarios/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // Endpoint para registrar usuarios. La ruta completa será: http://10.0.2.2:8080/api/usuarios
    @POST("usuarios")
    suspend fun registrarUsuario(@Body usuario: UsuarioRegistrado): Response<UsuarioRegistrado>
}
