package com.example.readme_grupo11.model

data class LibroUiState(
    val titulo: String = "",
    val autor: String = "",
    val idioma: String = "",
    val paginas: String = "",
    val generos: List<String> = emptyList(),
)

// posibles errores de validación para cada campo del formulario
data class LibroErrores(
    val tituloError: String? = null,
    val autorError: String? = null,
    val paginasError: String? = null,
    val generosError: String? = null,
    val errorGeneral: String? = null
)

data class Libro(
    val id: Int? = null,
    val titulo: String,
    val autor: String,
    val idioma: String,
    val paginas: Int,
    val generos: List<String>,
)