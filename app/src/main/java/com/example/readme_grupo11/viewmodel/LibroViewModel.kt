package com.example.readme_grupo11.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readme_grupo11.api.RetrofitClient
import com.example.readme_grupo11.model.Libro
import com.example.readme_grupo11.model.LibroUiState
import com.example.readme_grupo11.model.LibroErrores
import com.example.readme_grupo11.model.LibroConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LibroViewModel : ViewModel() {

    // Conexión con la API (backend)
    private val apiService = RetrofitClient.instance

    // Datos del formulario (lo que el usuario escribe)
    private val _uiState = MutableStateFlow(LibroUiState())
    val uiState: StateFlow<LibroUiState> = _uiState.asStateFlow()

    // Lista de todos los libros
    private val _libros = MutableStateFlow<List<Libro>>(emptyList())
    val libros: StateFlow<List<Libro>> = _libros.asStateFlow()

    // Para mostrar el círculo de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Mensajes de error (cuando algo sale mal)
    private val _errores = MutableStateFlow(LibroErrores())
    val errores: StateFlow<LibroErrores> = _errores.asStateFlow()

    // Mensajes de éxito (ej: "Libro creado exitosamente")
    private val _mensajeExito = MutableStateFlow<String?>(null)
    val mensajeExito: StateFlow<String?> = _mensajeExito.asStateFlow()

    // Cuando se crea el ViewModel, carga los libros automáticamente
    init {
        cargarLibros()
    }

    // Funciones que se ejecutan cuando el usuario escribe

    fun actualizarTitulo(titulo: String) {
        _uiState.value = _uiState.value.copy(titulo = titulo)
        validarTitulo(titulo)
    }

    fun actualizarAutor(autor: String) {
        _uiState.value = _uiState.value.copy(autor = autor)
        validarAutor(autor)
    }

    fun actualizarIdioma(idioma: String) {
        _uiState.value = _uiState.value.copy(idioma = idioma)
        validarIdioma(idioma)
    }

    fun actualizarPaginas(paginas: String) {
        _uiState.value = _uiState.value.copy(paginas = paginas)
        validarPaginas(paginas)
    }

    fun actualizarCategoria(categoria: String) {
        _uiState.value = _uiState.value.copy(categoria = categoria)
        validarCategoria(categoria)
    }

    // Validaciones (revisan si lo que escribió el usuario está bien)

    private fun validarTitulo(titulo: String) {
        val error = when {
            titulo.isBlank() -> "El título es obligatorio"
            titulo.length < 3 -> "El título debe tener al menos 3 caracteres"
            titulo.length > 200 -> "El título no puede exceder 200 caracteres"
            !titulo.matches(Regex("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ.,;:()¿?¡!'\" -]+$")) ->
                "El título contiene caracteres no permitidos"
            else -> null
        }
        _errores.value = _errores.value.copy(tituloError = error)
    }

    private fun validarAutor(autor: String) {
        val error = when {
            autor.isBlank() -> "El autor es obligatorio"
            autor.length < 3 -> "El autor debe tener al menos 3 caracteres"
            autor.length > 100 -> "El autor no puede exceder 100 caracteres"
            !autor.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ -]+$")) ->
                "El autor solo puede contener letras, espacios y guiones"
            else -> null
        }
        _errores.value = _errores.value.copy(autorError = error)
    }

    private fun validarIdioma(idioma: String) {
        val error = when {
            idioma.isBlank() -> "El idioma es obligatorio"
            idioma !in LibroConstants.IDIOMAS -> "Debe seleccionar un idioma válido"
            else -> null
        }
        _errores.value = _errores.value.copy(idiomaError = error)
    }

    private fun validarPaginas(paginas: String) {
        val error = when {
            paginas.isBlank() -> "Las páginas son obligatorias"
            !paginas.matches(Regex("^[0-9]+$")) -> "Solo se permiten números"
            else -> {
                val paginasNum = paginas.toIntOrNull()
                when {
                    paginasNum == null -> "Número inválido"
                    paginasNum < 1 -> "Debe tener al menos 1 página"
                    paginasNum >= 10000 -> "No puede tener 10,000 o más páginas"
                    else -> null
                }
            }
        }
        _errores.value = _errores.value.copy(paginasError = error)
    }

    private fun validarCategoria(categoria: String) {
        val error = when {
            categoria.isBlank() -> "La categoría es obligatoria"
            categoria !in LibroConstants.CATEGORIAS -> "Debe seleccionar una categoría válida"
            else -> null
        }
        _errores.value = _errores.value.copy(categoriaError = error)
    }

    // Valida el formulario antes de guardar
    private fun validarFormulario(): Boolean {
        val state = _uiState.value

        validarTitulo(state.titulo)
        validarAutor(state.autor)
        validarIdioma(state.idioma)
        validarPaginas(state.paginas)
        validarCategoria(state.categoria)

        val erroresActuales = _errores.value
        return erroresActuales.tituloError == null &&
                erroresActuales.autorError == null &&
                erroresActuales.idiomaError == null &&
                erroresActuales.paginasError == null &&
                erroresActuales.categoriaError == null
    }

    // Operaciones CRUD: Crear, Editar, Eliminar, Cargar

    // Trae todos los libros de la base de datos
    fun cargarLibros() {
        viewModelScope.launch {
            _isLoading.value = true
            _errores.value = _errores.value.copy(errorGeneral = null)

            try {
                val response = apiService.obtenerLibros()
                if (response.isSuccessful) {
                    _libros.value = response.body() ?: emptyList()
                } else {
                    _errores.value = _errores.value.copy(
                        errorGeneral = "Error al cargar libros: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                _errores.value = _errores.value.copy(
                    errorGeneral = "Error de conexión: ${e.message}"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Crea un libro nuevo en la base de datos
    fun crearLibro() {
        if (!validarFormulario()) return

        viewModelScope.launch {
            _isLoading.value = true
            _errores.value = _errores.value.copy(errorGeneral = null)

            try {
                val state = _uiState.value
                val libro = Libro(
                    titulo = state.titulo,
                    autor = state.autor,
                    idioma = state.idioma,
                    paginas = state.paginas.toInt(),
                    categoria = state.categoria
                )

                val response = apiService.crearLibro(libro)
                if (response.isSuccessful) {
                    _mensajeExito.value = "Libro creado exitosamente"
                    limpiarFormulario()
                    cargarLibros()
                } else {
                    _errores.value = _errores.value.copy(
                        errorGeneral = "Error al crear libro: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                _errores.value = _errores.value.copy(
                    errorGeneral = "Error: ${e.message}"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Actualiza un libro que ya existe
    fun actualizarLibro(id: Int) {
        if (!validarFormulario()) return

        viewModelScope.launch {
            _isLoading.value = true
            _errores.value = _errores.value.copy(errorGeneral = null)

            try {
                val state = _uiState.value
                val libro = Libro(
                    id = id,
                    titulo = state.titulo,
                    autor = state.autor,
                    idioma = state.idioma,
                    paginas = state.paginas.toInt(),
                    categoria = state.categoria
                )

                val response = apiService.actualizarLibro(id, libro)
                if (response.isSuccessful) {
                    _mensajeExito.value = "Libro actualizado exitosamente"
                    limpiarFormulario()
                    cargarLibros()
                } else {
                    _errores.value = _errores.value.copy(
                        errorGeneral = "Error al actualizar libro: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                _errores.value = _errores.value.copy(
                    errorGeneral = "Error: ${e.message}"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Elimina un libro de la base de datos
    fun eliminarLibro(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errores.value = _errores.value.copy(errorGeneral = null)

            try {
                val response = apiService.eliminarLibro(id)
                if (response.isSuccessful) {
                    _mensajeExito.value = "Libro eliminado exitosamente"
                    cargarLibros()
                } else {
                    _errores.value = _errores.value.copy(
                        errorGeneral = "Error al eliminar libro: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                _errores.value = _errores.value.copy(
                    errorGeneral = "Error: ${e.message}"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Funciones auxiliares (para ayudar con otras tareas)

    // Llena el formulario con los datos de un libro (para editarlo)
    // Se usa cuando presionas el botón "Editar" en un libro
    fun cargarLibroEnFormulario(libro: Libro) {
        _uiState.value = LibroUiState(
            titulo = libro.titulo,
            autor = libro.autor,
            idioma = libro.idioma,
            paginas = libro.paginas.toString(),
            categoria = libro.categoria
        )
        validarTitulo(libro.titulo)
        validarAutor(libro.autor)
        validarIdioma(libro.idioma)
        validarPaginas(libro.paginas.toString())
        validarCategoria(libro.categoria)
    }

    // Limpia el formulario (deja los campos vacíos)
    fun limpiarFormulario() {
        _uiState.value = LibroUiState()
        _errores.value = LibroErrores()
    }

    // Borra los mensajes de éxito y error
    fun limpiarMensajes() {
        _mensajeExito.value = null
        _errores.value = _errores.value.copy(errorGeneral = null)
    }
}