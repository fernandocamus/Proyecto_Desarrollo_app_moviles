package com.example.readme_grupo11.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.collections.toMutableList
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Checkbox
import androidx.compose.material3.TopAppBarDefaults


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearLibroScreen(
    onNavigateBack: () -> Unit
) {

    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var idioma by remember { mutableStateOf("") }
    var paginas by remember { mutableStateOf("") }
    var generosSeleccionados by remember { mutableStateOf<List<String>>(emptyList()) }

    // Estados para simular la validación y la carga.
    var tituloError by remember { mutableStateOf<String?>(null) }
    var autorError by remember { mutableStateOf<String?>(null) }
    var paginasError by remember { mutableStateOf<String?>(null) }
    var errorGeneral by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Scaffold proporciona la estructura básica de la pantalla.
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Nuevo Libro") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            // Columna principal con scroll para el formulario.
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                // Contenido centrado
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                //Texto de bienvenida
                Text(
                    text = "Ingresa tus credenciales para continuar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                //Titulo
                OutlinedTextField(
                    value = titulo,
                    // Ahora actualizamos directamente la variable local.
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    leadingIcon = { Icon(Icons.Default.Title, null) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = tituloError != null,
                    singleLine = true
                )
                if (tituloError != null) {
                    Text(
                        text = tituloError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Autor
                OutlinedTextField(
                    value = autor,
                    onValueChange = { autor = it },
                    label = { Text("Autor") },
                    leadingIcon = { Icon(Icons.Default.Person, null) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = autorError != null,
                    singleLine = true
                )
                if (autorError != null) {
                    Text(
                        text = autorError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Idioma y paginas
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = idioma,
                        onValueChange = { idioma = it },
                        label = { Text("Idioma") },
                        leadingIcon = { Icon(Icons.Default.Language, null) },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = paginas,
                        // SOLUCIÓN: Se añade lógica de filtrado para aceptar solo dígitos.
                        onValueChange = { nuevoValor ->
                            // Se comprueba si el nuevo valor está vacío o si todos sus caracteres son dígitos.
                            if (nuevoValor.isEmpty() || nuevoValor.all { it.isDigit() }) {
                                // Si es válido, se actualiza el estado.
                                paginas = nuevoValor
                            }
                        },
                        label = { Text("Páginas") },
                        leadingIcon = { Icon(Icons.Default.MenuBook, null) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = paginasError != null
                    )
                }
                if (paginasError != null) {
                    Text(
                        text = paginasError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Generos
                Text("Géneros", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                // Usamos una Columna para organizar los checkboxes verticalmente.
                Column(modifier = Modifier.fillMaxWidth()) {
                    val todosLosGeneros = remember {
                        listOf("Ficción", "No Ficción", "Misterio", "Terror", "Suspenso", "Historia")
                    }

                    todosLosGeneros.forEach { genero ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val generosActuales = generosSeleccionados.toMutableList()
                                    if (generosActuales.contains(genero)) {
                                        generosActuales.remove(genero)
                                    } else {
                                        generosActuales.add(genero)
                                    }
                                    generosSeleccionados = generosActuales
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = generosSeleccionados.contains(genero),
                                onCheckedChange = { isChecked ->
                                    val generosActuales = generosSeleccionados.toMutableList()
                                    if (isChecked) {
                                        if (!generosActuales.contains(genero)) generosActuales.add(genero)
                                    } else {
                                        generosActuales.remove(genero)
                                    }
                                    generosSeleccionados = generosActuales
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = genero, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Registro
                if (errorGeneral != null) {
                    Text(
                        text = errorGeneral!!,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                }

                Button(
                    onClick = { /* Lógica de prueba aquí ej: mostrar errores o un log */ },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Registrar Libro", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CrearLibroScreenPreview() {
    CrearLibroScreen(onNavigateBack = {})
}