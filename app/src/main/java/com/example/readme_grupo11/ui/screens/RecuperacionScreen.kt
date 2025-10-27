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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.readme_grupo11.viewmodel.RecuperacionViewModel
import androidx.compose.ui.tooling.preview.Preview

// Pantalla de recuperacion de contraseña
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecuperacionScreen(
    onNavigateBack: () -> Unit,
    viewModel: RecuperacionViewModel = viewModel()
) {
    // Estados del ViewModel de recuperacion de contraseña
    val uiState by viewModel.uiState.collectAsState()
    val correoError by viewModel.correoError.collectAsState()
    val envioExitoso by viewModel.envioExitoso.collectAsState()

    Scaffold(
        // Batta superior con recuperar contraseña como titulo
        topBar = {
            TopAppBar(
                title = { Text("Recuperar Contraseña") },
                navigationIcon = {
                    // Boton para volver atras
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
            // Columna con scroll vertical
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    // scroll vertical
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                // Centrado horizontal y vertical del contenido
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Icono de recuperacion de contraseña
                Icon(
                    imageVector = Icons.Default.LockReset,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Titulo principal con estilos, tamaños y alineacion
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Instrucciones con la personalizacion
                Text(
                    text = "Ingresa tu correo electrónico y te enviaremos las instrucciones para recuperar tu contraseña.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                //Campo: correo electronico
                OutlinedTextField(
                    value = uiState.correo,
                    onValueChange = { viewModel.actualizarCorreo(it) },
                    label = { Text("Correo electrónico") },
                    placeholder = { Text("usuario@duoc.cl") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null)
                    },
                    // Si hay error el borde se pone en rojo
                    isError = correoError != null,
                    supportingText = {
                        if (correoError != null) {
                            // mensaje de error especifico para recuperacion
                            Text(correoError!!)
                        } else {
                            // Cuando no hay error muestra este mensaje como guia
                            Text("Usa tu correo @duoc.cl")
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        // Teclado para email
                        keyboardType = KeyboardType.Email
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Boton para enviar correo de recuperacion (SOLO VISUAL, NO FUNCIONAL)
                Button(
                    onClick = {
                        viewModel.enviarRecuperacion()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Enviar correo",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar mensaje cuando el envio fue correcto
                if (envioExitoso) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Icono para simular correo enviado
                            Icon(
                                imageVector = Icons.Default.MarkEmailRead,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            // Mensaje simulado
                            Text(
                                text = "¡Correo Enviado!",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            // Instrucciones cuando el correo ya fue enviado
                            Text(
                                text = "Revisa tu bandeja de entrada. Te hemos enviado las instrucciones para recuperar tu contraseña.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                //Boton para volver al login
                TextButton(
                    onClick = onNavigateBack,
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Volver al inicio de sesión")
                }
            }
        }
    }
}

// Preview de la pantalla recuperacionScreen
@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Preview RecuperacionScreen"
)
@Composable
fun RecuperacionScreenPreview() {
    MaterialTheme {
        RecuperacionScreen(
            onNavigateBack = {}
        )
    }
}