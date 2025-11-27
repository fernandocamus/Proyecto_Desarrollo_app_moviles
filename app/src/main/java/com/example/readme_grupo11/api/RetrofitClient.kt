package com.example.readme_grupo11.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // URL base, apuntando a la IP especial del emulador y al prefijo /api/
    private const val BASE_URL = "http://192.168.100.55:8080/api/"

    // URL para MÓVIL FÍSICO (DEBES USAR ESTA y reemplazar la IP)
    // private const val BASE_URL = "http://[TU_IP_DE_PC]:8080/api/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}
