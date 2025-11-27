package com.example.readme_grupo11.viewmodel

import com.example.readme_grupo11.api.ApiService
import com.example.readme_grupo11.model.Libro
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class LibroViewModelTest : StringSpec({

    val testDispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    // Verificacion de carga de libros

    "Carga libros exitosamente al iniciar" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>()
            val librosEsperados = listOf(
                Libro(1, "El Quijote", "Cervantes", "Español", 500, "Ficción"),
                Libro(2, "1984", "Orwell", "Inglés", 328, "Ciencia Ficción")
            )

            coEvery { mockApiService.obtenerLibros() } returns Response.success(librosEsperados)

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            // When
            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            viewModel.libros.value shouldBe librosEsperados
            viewModel.isLoading.value shouldBe false
            viewModel.errores.value.errorGeneral.shouldBeNull()
        }
    }

    "Muestra error al fallar la carga de libros" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>()

            coEvery { mockApiService.obtenerLibros() } returns Response.error(
                500,
                "".toResponseBody()
            )

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            // When
            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            viewModel.libros.value shouldBe emptyList()
            viewModel.isLoading.value shouldBe false
            viewModel.errores.value.errorGeneral shouldBe "Error al cargar libros: 500"
        }
    }

    "Maneja la excepción de red al cargar libros" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>()

            coEvery { mockApiService.obtenerLibros() } throws Exception("Error de red")

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            // When
            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            viewModel.errores.value.errorGeneral shouldBe "Error de conexión: Error de red"
        }
    }

    // Test de validacion (Titulo)

    "Valida titulo correctamente - campo vacío" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.actualizarTitulo("")

            // Then
            viewModel.errores.value.tituloError shouldBe "El título es obligatorio"
        }
    }

    "Valida titulo correctamente - muy corto" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.actualizarTitulo("Ab")

            // Then
            viewModel.errores.value.tituloError shouldBe "El título debe tener al menos 3 caracteres"
        }
    }

    "Valida titulo correctamente - caracteres inválidos" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.actualizarTitulo("Libro @#$")

            // Then
            viewModel.errores.value.tituloError shouldBe "El título contiene caracteres no permitidos"
        }
    }

    "Acepta título válido" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.actualizarTitulo("El Quijote de la Mancha")

            // Then
            viewModel.errores.value.tituloError.shouldBeNull()
            viewModel.uiState.value.titulo shouldBe "El Quijote de la Mancha"
        }
    }

    // Test de validacion (Autor)

    "Valida autor correctamente - campo vacío" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.actualizarAutor("")

            // Then
            viewModel.errores.value.autorError shouldBe "El autor es obligatorio"
        }
    }

    "Valida autor correctamente - caracteres inválidos" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.actualizarAutor("Autor123")

            // Then
            viewModel.errores.value.autorError shouldBe "El autor solo puede contener letras, espacios y guiones"
        }
    }

    "Acepta autor válido" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.actualizarAutor("Gabriel García Márquez")

            // Then
            viewModel.errores.value.autorError.shouldBeNull()
            viewModel.uiState.value.autor shouldBe "Gabriel García Márquez"
        }
    }

    // Test de validacion (Idioma)

    "Valida idioma correctamente - campo vacío" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.actualizarIdioma("")

            // Then
            viewModel.errores.value.idiomaError shouldBe "El idioma es obligatorio"
        }
    }

    "Valida idioma correctamente - idioma inválido" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.actualizarIdioma("Marciano")

            // Then
            viewModel.errores.value.idiomaError shouldBe "Debe seleccionar un idioma válido"
        }
    }

    "Acepta idioma válido" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.actualizarIdioma("Español")

            // Then
            viewModel.errores.value.idiomaError.shouldBeNull()
            viewModel.uiState.value.idioma shouldBe "Español"
        }
    }

    // Test de validacion (Pagina)

    "Valida páginas correctamente - campo vacío" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.actualizarPaginas("")

            // Then
            viewModel.errores.value.paginasError shouldBe "Las páginas son obligatorias"
        }
    }

    "Valida páginas correctamente - caracteres no numéricos" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.actualizarPaginas("abc")

            // Then
            viewModel.errores.value.paginasError shouldBe "Solo se permiten números"
        }
    }

    "Valida páginas correctamente - número menor a 1" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.actualizarPaginas("0")

            // Then
            viewModel.errores.value.paginasError shouldBe "Debe tener al menos 1 página"
        }
    }

    "Valida páginas correctamente - número mayor o igual a 10000" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.actualizarPaginas("10000")

            // Then
            viewModel.errores.value.paginasError shouldBe "No puede tener 10,000 o más páginas"
        }
    }

    "Acepta páginas válidas" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.actualizarPaginas("500")

            // Then
            viewModel.errores.value.paginasError.shouldBeNull()
            viewModel.uiState.value.paginas shouldBe "500"
        }
    }

    // Test de validacion (Categoria)

    "Valida categoría correctamente - campo vacío" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.actualizarCategoria("")

            // Then
            viewModel.errores.value.categoriaError shouldBe "La categoría es obligatoria"
        }
    }

    "Valida categoría correctamente - categoría inválida" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.actualizarCategoria("Inexistente")

            // Then
            viewModel.errores.value.categoriaError shouldBe "Debe seleccionar una categoría válida"
        }
    }

    "Acepta categoría válida" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.actualizarCategoria("Ficción")

            // Then
            viewModel.errores.value.categoriaError.shouldBeNull()
            viewModel.uiState.value.categoria shouldBe "Ficción"
        }
    }

    // Test de creacion de libro

    "Creacion de libro exitoso con datos válidos" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>()
            val libroCreado = Libro(1, "Nuevo Libro", "Autor Test", "Español", 300, "Ficción")

            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())
            coEvery { mockApiService.crearLibro(any()) } returns Response.success(libroCreado)

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.actualizarTitulo("Nuevo Libro")
            viewModel.actualizarAutor("Autor Test")
            viewModel.actualizarIdioma("Español")
            viewModel.actualizarPaginas("300")
            viewModel.actualizarCategoria("Ficción")
            viewModel.crearLibro()
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            viewModel.mensajeExito.value shouldBe "Libro creado exitosamente"
            coVerify { mockApiService.crearLibro(any()) }
        }
    }

    "No crear libro si el formulario es inválido" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // When - formulario vacío
            viewModel.crearLibro()
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            coVerify(exactly = 0) { mockApiService.crearLibro(any()) }
        }
    }

    // Test de actualizacion de libro

    "Actualiza libro exitosamente" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>()
            val libroActualizado = Libro(1, "Título Actualizado", "Autor Actualizado", "Inglés", 400, "Misterio")

            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())
            coEvery { mockApiService.actualizarLibro(1, any()) } returns Response.success(libroActualizado)

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.actualizarTitulo("Título Actualizado")
            viewModel.actualizarAutor("Autor Actualizado")
            viewModel.actualizarIdioma("Inglés")
            viewModel.actualizarPaginas("400")
            viewModel.actualizarCategoria("Misterio")
            viewModel.actualizarLibro(1)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            viewModel.mensajeExito.value shouldBe "Libro actualizado exitosamente"
            coVerify { mockApiService.actualizarLibro(1, any()) }
        }
    }

    // Test de eliminacion de libro

    "Elimina libro exitosamente" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>()

            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())
            coEvery { mockApiService.eliminarLibro(1) } returns Response.success(mockk(relaxed = true))

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.eliminarLibro(1)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            viewModel.mensajeExito.value shouldBe "Libro eliminado exitosamente"
            coVerify { mockApiService.eliminarLibro(1) }
        }
    }

    // Test de funciones auxiliares

    "Carga libro en formulario correctamente" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            val libro = Libro(1, "Test", "Autor", "Español", 200, "Ficción")

            // When
            viewModel.cargarLibroEnFormulario(libro)

            // Then
            viewModel.uiState.value.titulo shouldBe "Test"
            viewModel.uiState.value.autor shouldBe "Autor"
            viewModel.uiState.value.idioma shouldBe "Español"
            viewModel.uiState.value.paginas shouldBe "200"
            viewModel.uiState.value.categoria shouldBe "Ficción"
        }
    }

    "Limpia formulario correctamente" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // Llenar formulario
            viewModel.actualizarTitulo("Test")
            viewModel.actualizarAutor("Autor")

            // When
            viewModel.limpiarFormulario()

            // Then
            viewModel.uiState.value.titulo shouldBe ""
            viewModel.uiState.value.autor shouldBe ""
            viewModel.uiState.value.idioma shouldBe ""
            viewModel.uiState.value.paginas shouldBe ""
            viewModel.uiState.value.categoria shouldBe ""
            viewModel.errores.value.tituloError.shouldBeNull()
            viewModel.errores.value.autorError.shouldBeNull()
        }
    }

    "Limpia mensajes correctamente" {
        runTest {
            // Given
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.limpiarMensajes()

            // Then
            viewModel.mensajeExito.value.shouldBeNull()
            viewModel.errores.value.errorGeneral.shouldBeNull()
        }
    }
})