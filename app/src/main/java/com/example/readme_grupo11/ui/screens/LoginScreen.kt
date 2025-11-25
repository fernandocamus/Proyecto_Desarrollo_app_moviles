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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.readme_grupo11.viewmodel.LoginViewModel
import androidx.compose.ui.tooling.preview.Preview

// Pantalla de inicio de sesion de la aplicacion
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToRegistro: () -> Unit,
    onNavigateToRecuperacion: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    // ver los estados del LoginViewModel
    val uiState by viewModel.uiState.collectAsState()
    val errores by viewModel.errores.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Estado para ver o esconder la contraseña
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(
        // Barra con el nombre de la app
        topBar = {
            TopAppBar(
                title = { Text("ZONALIBROS") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Columna con el scroll
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                // Contenido centrado
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Lgoo de la aplicacion
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = "Logo",
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

                Spacer(modifier = Modifier.height(32.dp))

                // Mostrar mensajes de error cuando las credenciales sean incorrectas
                if (errores.errorGeneral != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = errores.errorGeneral ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Campo: correo electronico
                OutlinedTextField(
                    value = uiState.correo,
                    onValueChange = { viewModel.actualizarCorreo(it) },
                    label = { Text("Correo electrónico") },
                    placeholder = { Text("usuario@duoc.cl") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null)
                    },
                    // El borde se pone rojo si hay algun error
                    isError = errores.correoError != null,
                    supportingText = {
                        // Se muestra el mensaje de error especifico de correo
                        if (errores.correoError != null) {
                            Text(errores.correoError!!)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        // Teclado para email
                        keyboardType = KeyboardType.Email
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo: Contraseña
                OutlinedTextField(
                    value = uiState.contrasena,
                    onValueChange = { viewModel.actualizarContrasena(it) },
                    label = { Text("Contraseña") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null)
                    },
                    trailingIcon = {
                        // Boton para mostrar y esconder la contraseña
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible)
                                    "Ocultar contraseña"
                                else
                                    "Mostrar contraseña"
                            )
                        }
                    },
                    // Esconde el texto con puntos si no esta activado el mostrar contraseñas
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    isError = errores.contrasenaError != null,
                    supportingText = {
                        if (errores.contrasenaError != null) {
                            Text(errores.contrasenaError!!)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Link que redirige a recuperar contraseña (Aun no funcional)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onNavigateToRecuperacion,
                        enabled = !isLoading
                    ) {
                        Text("¿Olvidaste tu contraseña?")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Boton para iniciar sesion con loading
                Button(
                    onClick = {
                        // Limpia errores
                        viewModel.limpiarErrorGeneral()
                        // Inicia sesion y redirige al home
                        viewModel.iniciarSesion(onNavigateToHome)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        // Mostrar CircularProgressIndicator mientras carga
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 3.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Iniciando sesión...",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    } else {
                        Text(
                            text = "Iniciar Sesión",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Spacer(modifier = Modifier.height(24.dp))

                // Divisor visual con un "o" para dividir
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f))
                    Text(
                        text = " o ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Boton para redirigir a crear cuenta
                OutlinedButton(
                    onClick = onNavigateToRegistro,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !isLoading
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Crear cuenta nueva",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

// Preview de la pantalla Login
@Composable
@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Preview LoginScreen"
)
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(
            onNavigateToRegistro = {},
            onNavigateToRecuperacion = {},
            onNavigateToHome = {}
        )
    }
}