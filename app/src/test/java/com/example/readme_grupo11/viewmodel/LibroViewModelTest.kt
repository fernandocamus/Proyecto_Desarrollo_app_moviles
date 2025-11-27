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
            val mockApiService = mockk<ApiService>()
            val librosEsperados = listOf(
                Libro(1, "El Quijote", "Cervantes", "Español", 500, "Ficción"),
                Libro(2, "1984", "Orwell", "Inglés", 328, "Ciencia Ficción")
            )

            coEvery { mockApiService.obtenerLibros() } returns Response.success(librosEsperados)

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.libros.value shouldBe librosEsperados
            viewModel.isLoading.value shouldBe false
            viewModel.errores.value.errorGeneral.shouldBeNull()
        }
    }

    "Muestra error al fallar la carga de libros" {
        runTest {
            val mockApiService = mockk<ApiService>()

            coEvery { mockApiService.obtenerLibros() } returns Response.error(
                500,
                "".toResponseBody()
            )

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.libros.value shouldBe emptyList()
            viewModel.isLoading.value shouldBe false
            viewModel.errores.value.errorGeneral shouldBe "Error al cargar libros: 500"
        }
    }

    "Maneja la excepción de red al cargar libros" {
        runTest {
            val mockApiService = mockk<ApiService>()

            coEvery { mockApiService.obtenerLibros() } throws Exception("Error de red")

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.errores.value.errorGeneral shouldBe "Error de conexión: Error de red"
        }
    }

    // Test de validacion (Titulo)

    "Valida titulo correctamente - campo vacío" {
        runTest {
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.actualizarTitulo("")

            viewModel.errores.value.tituloError shouldBe "El título es obligatorio"
        }
    }

    "Valida titulo correctamente - muy corto" {
        runTest {
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.actualizarTitulo("Ab")

            viewModel.errores.value.tituloError shouldBe "El título debe tener al menos 3 caracteres"
        }
    }

    "Valida titulo correctamente - caracteres inválidos" {
        runTest {
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.actualizarTitulo("Libro @#$")

            viewModel.errores.value.tituloError shouldBe "El título contiene caracteres no permitidos"
        }
    }

    "Acepta título válido" {
        runTest {
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.actualizarTitulo("El Quijote de la Mancha")

            viewModel.errores.value.tituloError.shouldBeNull()
            viewModel.uiState.value.titulo shouldBe "El Quijote de la Mancha"
        }
    }

    // Test de validacion (Autor)

    "Valida autor correctamente - campo vacío" {
        runTest {
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.actualizarAutor("")

            viewModel.errores.value.autorError shouldBe "El autor es obligatorio"
        }
    }

    "Valida autor correctamente - caracteres inválidos" {
        runTest {
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.actualizarAutor("Autor123")

            viewModel.errores.value.autorError shouldBe "El autor solo puede contener letras, espacios y guiones"
        }
    }

    "Acepta autor válido" {
        runTest {
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.actualizarAutor("Gabriel García Márquez")

            viewModel.errores.value.autorError.shouldBeNull()
            viewModel.uiState.value.autor shouldBe "Gabriel García Márquez"
        }
    }

    // Test de validacion (Idioma)

    "Valida idioma correctamente - campo vacío" {
        runTest {
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.actualizarIdioma("")

            viewModel.errores.value.idiomaError shouldBe "El idioma es obligatorio"
        }
    }

    "Valida idioma correctamente - idioma inválido" {
        runTest {
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.actualizarIdioma("Marciano")

            viewModel.errores.value.idiomaError shouldBe "Debe seleccionar un idioma válido"
        }
    }

    "Acepta idioma válido" {
        runTest {
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.actualizarIdioma("Español")

            viewModel.errores.value.idiomaError.shouldBeNull()
            viewModel.uiState.value.idioma shouldBe "Español"
        }
    }

    // Test de validacion (Pagina)

    "Valida páginas correctamente - campo vacío" {
        runTest {
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.actualizarPaginas("")

            viewModel.errores.value.paginasError shouldBe "Las páginas son obligatorias"
        }
    }

    "Valida páginas correctamente - caracteres no numéricos" {
        runTest {
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.actualizarPaginas("abc")

            viewModel.errores.value.paginasError shouldBe "Solo se permiten números"
        }
    }

    "Valida páginas correctamente - número menor a 1" {
        runTest {
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.actualizarPaginas("0")

            viewModel.errores.value.paginasError shouldBe "Debe tener al menos 1 página"
        }
    }

    "Valida páginas correctamente - número mayor o igual a 10000" {
        runTest {
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.actualizarPaginas("10000")

            viewModel.errores.value.paginasError shouldBe "No puede tener 10,000 o más páginas"
        }
    }

    "Acepta páginas válidas" {
        runTest {
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.actualizarPaginas("500")

            viewModel.errores.value.paginasError.shouldBeNull()
            viewModel.uiState.value.paginas shouldBe "500"
        }
    }

    // Test de validacion (Categoria)

    "Valida categoría correctamente - campo vacío" {
        runTest {
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.actualizarCategoria("")

            viewModel.errores.value.categoriaError shouldBe "La categoría es obligatoria"
        }
    }

    "Valida categoría correctamente - categoría inválida" {
        runTest {
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.actualizarCategoria("Inexistente")

            viewModel.errores.value.categoriaError shouldBe "Debe seleccionar una categoría válida"
        }
    }

    "Acepta categoría válida" {
        runTest {
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.actualizarCategoria("Ficción")

            viewModel.errores.value.categoriaError.shouldBeNull()
            viewModel.uiState.value.categoria shouldBe "Ficción"
        }
    }

    // Test de creacion de libro

    "Creacion de libro exitoso con datos válidos" {
        runTest {
            val mockApiService = mockk<ApiService>()
            val libroCreado = Libro(1, "Nuevo Libro", "Autor Test", "Español", 300, "Ficción")

            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())
            coEvery { mockApiService.crearLibro(any()) } returns Response.success(libroCreado)

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.actualizarTitulo("Nuevo Libro")
            viewModel.actualizarAutor("Autor Test")
            viewModel.actualizarIdioma("Español")
            viewModel.actualizarPaginas("300")
            viewModel.actualizarCategoria("Ficción")
            viewModel.crearLibro()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.mensajeExito.value shouldBe "Libro creado exitosamente"
            coVerify { mockApiService.crearLibro(any()) }
        }
    }

    "No crear libro si el formulario es inválido" {
        runTest {
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // Formulario vacío
            viewModel.crearLibro()
            testDispatcher.scheduler.advanceUntilIdle()

            coVerify(exactly = 0) { mockApiService.crearLibro(any()) }
        }
    }

    // Test de actualizacion de libro

    "Actualiza libro exitosamente" {
        runTest {
            val mockApiService = mockk<ApiService>()
            val libroActualizado = Libro(1, "Título Actualizado", "Autor Actualizado", "Inglés", 400, "Misterio")

            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())
            coEvery { mockApiService.actualizarLibro(1, any()) } returns Response.success(libroActualizado)

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.actualizarTitulo("Título Actualizado")
            viewModel.actualizarAutor("Autor Actualizado")
            viewModel.actualizarIdioma("Inglés")
            viewModel.actualizarPaginas("400")
            viewModel.actualizarCategoria("Misterio")
            viewModel.actualizarLibro(1)
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.mensajeExito.value shouldBe "Libro actualizado exitosamente"
            coVerify { mockApiService.actualizarLibro(1, any()) }
        }
    }

    // Test de eliminacion de libro

    "Elimina libro exitosamente" {
        runTest {
            val mockApiService = mockk<ApiService>()

            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())
            coEvery { mockApiService.eliminarLibro(1) } returns Response.success(mockk(relaxed = true))

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.eliminarLibro(1)
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.mensajeExito.value shouldBe "Libro eliminado exitosamente"
            coVerify { mockApiService.eliminarLibro(1) }
        }
    }

    // Test de funciones auxiliares

    "Carga libro en formulario correctamente" {
        runTest {
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            val libro = Libro(1, "Test", "Autor", "Español", 200, "Ficción")

            viewModel.cargarLibroEnFormulario(libro)

            viewModel.uiState.value.titulo shouldBe "Test"
            viewModel.uiState.value.autor shouldBe "Autor"
            viewModel.uiState.value.idioma shouldBe "Español"
            viewModel.uiState.value.paginas shouldBe "200"
            viewModel.uiState.value.categoria shouldBe "Ficción"
        }
    }

    "Limpia formulario correctamente" {
        runTest {
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            // Llenar formulario
            viewModel.actualizarTitulo("Test")
            viewModel.actualizarAutor("Autor")

            viewModel.limpiarFormulario()

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
            val mockApiService = mockk<ApiService>(relaxed = true)
            coEvery { mockApiService.obtenerLibros() } returns Response.success(emptyList())

            mockkObject(com.example.readme_grupo11.api.RetrofitClient)
            every { com.example.readme_grupo11.api.RetrofitClient.instance } returns mockApiService

            val viewModel = LibroViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.limpiarMensajes()

            viewModel.mensajeExito.value.shouldBeNull()
            viewModel.errores.value.errorGeneral.shouldBeNull()
        }
    }
})