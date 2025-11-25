package com.example.readme_grupo11.model

// Modelo para la api
data class Libro(
    val id: Int? = null,
    val titulo: String,
    val autor: String,
    val idioma: String,
    val paginas: Int,
    val categoria: String
)

// Estado de UI para la creacion de libro y su edicion
data class LibroUiState(
    val titulo: String = "",
    val autor: String = "",
    val idioma: String = "",
    val paginas: String = "",
    val categoria: String = ""
)

// Errores de validacion
data class LibroErrores(
    val tituloError: String? = null,
    val autorError: String? = null,
    val idiomaError: String? = null,
    val paginasError: String? = null,
    val categoriaError: String? = null,
    val errorGeneral: String? = null
)

// Opciones de eleccion
object LibroConstants {
    val IDIOMAS = listOf(
        "Español",
        "Inglés",
        "Italiano",
        "Portugués",
        "Francés",
        "Alemán",
        "Ruso",
        "Japonés",
        "Chino",
        "Coreano"
    )

    val CATEGORIAS = listOf(
        "Ficción",
        "No Ficción",
        "Misterio",
        "Terror",
        "Suspenso",
        "Historia",
        "Romance",
        "Ciencia Ficción",
        "Fantasía",
        "Biografía"
    )
}
