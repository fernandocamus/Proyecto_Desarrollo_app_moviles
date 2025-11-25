package com.example.readme_grupo11.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.readme_grupo11.model.Libro
import com.example.readme_grupo11.model.LibroConstants
import com.example.readme_grupo11.viewmodel.LibroViewModel
import androidx.compose.ui.tooling.preview.Preview

// Pantalla para la gestion de libros (CRUD)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibrosScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    libroViewModel: LibroViewModel = viewModel()
) {
    var showMenu by remember { mutableStateOf(false) }
    var mostrarDialogoCrear by remember { mutableStateOf(false) }
    var libroSeleccionado by remember { mutableStateOf<Libro?>(null) }
    var mostrarDialogoEditar by remember { mutableStateOf(false) }
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    val libros by libroViewModel.libros.collectAsState()
    val isLoading by libroViewModel.isLoading.collectAsState()
    val errores by libroViewModel.errores.collectAsState()
    val mensajeExito by libroViewModel.mensajeExito.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(mensajeExito) {
        mensajeExito?.let {
            snackbarHostState.showSnackbar(it)
            libroViewModel.limpiarMensajes()
        }
    }

    LaunchedEffect(errores.errorGeneral) {
        errores.errorGeneral?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("ZONALIBROS") },
                actions = {
                    // Boton para mostrar o esconder el menu
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }

                    // Menu desplegable
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        //Opcion: Mi perfil (no funcional, simplemente visual)
                        DropdownMenuItem(
                            text = { Text("Mi Perfil") },
                            onClick = { showMenu = false },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null)
                            }
                        )
                        HorizontalDivider()

                        //Opcion: Cerrar sesion
                        DropdownMenuItem(
                            text = { Text("Cerrar Sesión") },
                            onClick = {
                                showMenu = false
                                //Cierra sesion y re dirige al login
                                onNavigateToLogin()
                            },
                            leadingIcon = {
                                Icon(Icons.Default.ExitToApp, contentDescription = null)
                            }
                        )
                    }
                },
                // Colores para el topappbar
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        // Barra de navegacion inferior (funcionan dos botones, inicio y libros)
        bottomBar = {
            // Opcion: Inicio
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Inicio") },
                    selected = false,
                    onClick = { onNavigateToHome() }, // Navega a la pantalla de libros
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
                        indicatorColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.LibraryBooks, contentDescription = null) },
                    label = { Text("Mis Libros") },
                    selected = true,
                    onClick = { /* Ya estamos aquí */ },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
                        indicatorColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                    label = { Text("Carrito") },
                    selected = false,
                    onClick = { },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
                        indicatorColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    libroViewModel.limpiarFormulario()
                    mostrarDialogoCrear = true
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar libro", tint = MaterialTheme.colorScheme.onPrimary)
            }

        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (libros.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.MenuBook,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No hay libros registrados",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Presiona el botón + para agregar uno",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(libros) { libro ->
                        LibroCard(
                            libro = libro,
                            onEditar = {
                                libroViewModel.cargarLibroEnFormulario(libro)
                                libroSeleccionado = libro
                                mostrarDialogoEditar = true
                            },
                            onEliminar = {
                                libroSeleccionado = libro
                                mostrarDialogoEliminar = true
                            }
                        )
                    }
                }
            }
        }
    }

    // Dialogo crear libro
    if (mostrarDialogoCrear) {
        LibroDialog(
            titulo = "Crear Libro",
            libroId = null,
            viewModel = libroViewModel,
            onDismiss = {
                mostrarDialogoCrear = false
                libroViewModel.limpiarFormulario()
            },
            onConfirm = {
                libroViewModel.crearLibro()
                mostrarDialogoCrear = false
            }
        )
    }

    // Dialogo editar libro
    if (mostrarDialogoEditar && libroSeleccionado != null) {
        LibroDialog(
            titulo = "Editar Libro",
            libroId = libroSeleccionado?.id,
            viewModel = libroViewModel,
            onDismiss = {
                mostrarDialogoEditar = false
                libroSeleccionado = null
                libroViewModel.limpiarFormulario()
            },
            onConfirm = {
                libroSeleccionado?.id?.let { id ->
                    libroViewModel.actualizarLibro(id)
                }
                mostrarDialogoEditar = false
                libroSeleccionado = null
            }
        )
    }

    // Dialogo confirmar eliminar libro
    if (mostrarDialogoEliminar && libroSeleccionado != null) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoEliminar = false
                libroSeleccionado = null
            },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar \"${libroSeleccionado?.titulo}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        libroSeleccionado?.id?.let { id ->
                            libroViewModel.eliminarLibro(id)
                        }
                        mostrarDialogoEliminar = false
                        libroSeleccionado = null
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoEliminar = false
                        libroSeleccionado = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun LibroCard(
    libro: Libro,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = libro.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Autor: ${libro.autor}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Categoría: ${libro.categoria}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                )
                Text(
                    text = "Idioma: ${libro.idioma}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                )
                Text(
                    text = "Páginas: ${libro.paginas}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                )
            }

            Row {
                IconButton(onClick = onEditar) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onEliminar) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun LibroDialog(
    titulo: String,
    libroId: Int?,
    viewModel: LibroViewModel,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val errores by viewModel.errores.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(titulo) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Título
                OutlinedTextField(
                    value = uiState.titulo,
                    onValueChange = { viewModel.actualizarTitulo(it) },
                    label = { Text("Título *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errores.tituloError != null,
                    supportingText = {
                        errores.tituloError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    singleLine = true
                )

                // Autor
                OutlinedTextField(
                    value = uiState.autor,
                    onValueChange = { viewModel.actualizarAutor(it) },
                    label = { Text("Autor *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errores.autorError != null,
                    supportingText = {
                        errores.autorError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    singleLine = true
                )

                // Idiomas como chips seleccionables
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Idioma *",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (errores.idiomaError != null)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.onSurface
                    )

                    // Grid de idiomas (2 columnas)
                    LibroConstants.IDIOMAS.chunked(2).forEach { fila ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            fila.forEach { idioma ->
                                val isSelected = uiState.idioma == idioma

                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.actualizarIdioma(idioma) },
                                    label = { Text(idioma) },
                                    modifier = Modifier.weight(1f),
                                    leadingIcon = if (isSelected) {
                                        {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    } else null,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                )
                            }

                            if (fila.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    errores.idiomaError?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }

                // Páginas
                OutlinedTextField(
                    value = uiState.paginas,
                    onValueChange = { viewModel.actualizarPaginas(it) },
                    label = { Text("Páginas *") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = errores.paginasError != null,
                    supportingText = {
                        errores.paginasError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    singleLine = true
                )

                // Categorías como seleccionables
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Categoría *",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (errores.categoriaError != null)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.onSurface
                    )

                    // Grid de categorías (2 columnas)
                    LibroConstants.CATEGORIAS.chunked(2).forEach { fila ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            fila.forEach { categoria ->
                                val isSelected = uiState.categoria == categoria

                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.actualizarCategoria(categoria) },
                                    label = { Text(categoria) },
                                    modifier = Modifier.weight(1f),
                                    leadingIcon = if (isSelected) {
                                        {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    } else null,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                )
                            }

                            if (fila.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    errores.categoriaError?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

// Preview de la pantalla Home
@Composable
@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Preview HomeScreen"
)
fun LibrosScreenPreview() {
    MaterialTheme {
        LibrosScreen(
            onNavigateToLogin = {},
            onNavigateToHome = {}
        )
    }
}