package com.example.readme_grupo11.ui.screens

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.readme_grupo11.model.GeneroLiterario
import com.example.readme_grupo11.viewmodel.RegistroViewModel
import androidx.compose.ui.tooling.preview.Preview

// Pantalla de registro para crear nuevos usuarios
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegistroViewModel = viewModel()
) {
    // ver los estados del RegistroViewModel
    val uiState by viewModel.uiState.collectAsState()
    val errores by viewModel.errores.collectAsState()
    val registroExitoso by viewModel.registroExitoso.collectAsState()

    // Estado para ver o esconder las contraseñas
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta") },
                navigationIcon = {
                    //Boton para volver atras
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Columna con scroll vertical para el formulario
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                //Titulo del formulario
                Text(
                    text = "Registro de Usuario",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Instrucciones
                Text(
                    text = "Completa todos los campos para crear tu cuenta",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Campo: Nombre completo
                OutlinedTextField(
                    value = uiState.nombreCompleto,
                    onValueChange = { viewModel.actualizarNombre(it) },
                    label = { Text("Nombre Completo *") },
                    placeholder = { Text("Fernando Pino") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    },
                    // Si hay errores, mostrarlos
                    isError = errores.nombreError != null,
                    supportingText = {
                        if (errores.nombreError != null) {
                            Text(errores.nombreError!!)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(16.dp))

                //Campo: correo electronico
                OutlinedTextField(
                    value = uiState.correo,
                    onValueChange = { viewModel.actualizarCorreo(it) },
                    label = { Text("Correo Electrónico *") },
                    placeholder = { Text("usuario@duoc.cl") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null)
                    },
                    isError = errores.correoError != null,
                    supportingText = {
                        if (errores.correoError != null) {
                            Text(errores.correoError!!)
                        } else {
                            // Especificar el tipo de correo (cuando no hay error)
                            Text("Solo correos @duoc.cl")
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        //Teclado para email
                        keyboardType = KeyboardType.Email
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(16.dp))

                //Campo: contraseña
                OutlinedTextField(
                    value = uiState.contrasena,
                    onValueChange = { viewModel.actualizarContrasena(it) },
                    label = { Text("Contraseña *") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null)
                    },
                    trailingIcon = {
                        // Boton para mostrar o esconder la contraseña
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible)
                                    "Ocultar"
                                else
                                    "Mostrar"
                            )
                        }
                    },
                    // Esconder el texto como puntos
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    isError = errores.contrasenaError != null,
                    supportingText = {
                        if (errores.contrasenaError != null) {
                            Text(errores.contrasenaError!!)
                        } else {
                            // Requisitos de la contraseña
                            Text("Mín. 10 caracteres, mayúscula, minúscula, número y símbolo (@#$%)")
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo: confirmar contraseña
                OutlinedTextField(
                    value = uiState.confirmarContrasena,
                    onValueChange = { viewModel.actualizarConfirmarContrasena(it) },
                    label = { Text("Confirmar Contraseña *") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null)
                    },
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,
                                contentDescription = if (confirmPasswordVisible)
                                    "Ocultar"
                                else
                                    "Mostrar"
                            )
                        }
                    },
                    visualTransformation = if (confirmPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    isError = errores.confirmarContrasenaError != null,
                    supportingText = {
                        if (errores.confirmarContrasenaError != null) {
                            Text(errores.confirmarContrasenaError!!)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo: telefono (es opcional)
                OutlinedTextField(
                    value = uiState.telefono,
                    onValueChange = { viewModel.actualizarTelefono(it) },
                    label = { Text("Teléfono (opcional)") },
                    placeholder = { Text("+56912345678") },
                    leadingIcon = {
                        Icon(Icons.Default.Phone, contentDescription = null)
                    },
                    isError = errores.telefonoError != null,
                    supportingText = {
                        if (errores.telefonoError != null) {
                            Text(errores.telefonoError!!)
                        }
                    },
                    // Tipo de teclado numerico para el telefono
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Géneros Favoritos *",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                //Campo generos favoritos
                //Mostrar error de generos
                if (errores.generosError != null) {
                    Text(
                        text = errores.generosError!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                GeneroLiterario.entries.forEach { genero ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Lista para cada genero literario
                        Checkbox(
                            checked = uiState.generosFavoritos.contains(genero),
                            onCheckedChange = { viewModel.toggleGenero(genero) },
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = genero.displayName,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Spacer(modifier = Modifier.height(32.dp))

                // Boton para crear cuenta
                Button(
                    onClick = {
                        // Cambiar ya que debe dirigir al login (aun no esta creado)
                        viewModel.registrarUsuario(onNavigateToLogin)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Crear Cuenta",
                        style = MaterialTheme.typography.titleMedium
                    )

                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar mensaje de confirmacion cuando el usuario se registre de manera correcta
                if (registroExitoso) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "¡Registro exitoso! Redirigiendo...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                Spacer(modifier = Modifier.height(16.dp))
                }

                // Botón para volver al Login si el usuario tiene cuenta
                TextButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("¿Ya tienes cuenta? Inicia sesión")
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// Preview de la pantalla de registro
@Preview(showBackground = true)
@Composable
fun RegistroScreenPreview() {
    RegistroScreen(
        onNavigateBack = {},
        onNavigateToLogin = {}
    )
}