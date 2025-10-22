package com.example.readme_grupo11.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Formulario(navController: NavController) {

    //Campos
    var nombreCompleto by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contrasenia by remember { mutableStateOf("") }
    var confirmacionContrasenia by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    var errorNombreUsuario by remember { mutableStateOf("") }
    var errorEmail by remember { mutableStateOf("") }
    var errorContrasenia by remember { mutableStateOf("") }
    var errorConfirmacionContrasenia by remember { mutableStateOf("") }
    var errorTelefono by remember { mutableStateOf("") }

    var mensajeError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Text("Crear cuenta", style = MaterialTheme.typography.headlineSmall)
        Text("Crear una cuenta para empezar a explorar nuestra colección de libros")

        Spacer(modifier = Modifier.height(20.dp))

        Column {
            Text("Nombre")
            OutlinedTextField(
                value = nombreCompleto,
                onValueChange = {
                    nombreCompleto = it
                    errorNombreUsuario = ""
                    mensajeError = ""
                },
                label = { Text("Nombre") },
                isError = errorNombreUsuario.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (errorNombreUsuario.isNotEmpty()) {
            Text(errorNombreUsuario, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(10.dp))

        Column {
            Text("Email")
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorEmail = ""
                    mensajeError = ""
                },
                label = { Text("tu@correo.com") },
                isError = errorEmail.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (errorEmail.isNotEmpty()) {
            Text(errorEmail, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(20.dp))

        Column {
            Text("Telefono")
            OutlinedTextField(
                value = telefono,
                onValueChange = {
                    telefono = it
                    errorTelefono = ""
                    mensajeError = ""
                },
                label = { Text("Numero de telefono") },
                isError = errorTelefono.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (errorTelefono.isNotEmpty()) {
            Text(errorTelefono, color = MaterialTheme.colorScheme.error)
        }

        Column {
            Text("Contraseña")
            OutlinedTextField(
                value = contrasenia,
                onValueChange = {
                    contrasenia = it
                    errorContrasenia = ""
                    mensajeError = ""
                },
                label = { Text("Crea una contraseña segura") },
                isError = errorContrasenia.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (errorContrasenia.isNotEmpty()) {
            Text(errorContrasenia, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(20.dp))

        Column {
            Text("Confirmar contraseña")
            OutlinedTextField(
                value = confirmacionContrasenia,
                onValueChange = {
                    confirmacionContrasenia = it
                    mensajeError = ""
                },
                label = { Text("Vuelve a escribir la contraseña") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (errorConfirmacionContrasenia.isNotEmpty()) {
            Text(errorConfirmacionContrasenia, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val valido = validarFormulario(nombreCompleto, email, contrasenia, confirmacionContrasenia, telefono, onError = { campo, mensaje ->
                    when (campo) {
                        "nombre" -> errorNombreUsuario = mensaje
                        "email" -> errorEmail = mensaje
                        "contrasenia" -> errorContrasenia = mensaje
                        "confirmacionContrasenia" -> errorConfirmacionContrasenia = mensaje
                        "telefono" -> errorTelefono = mensaje
                    }
                })

                if (!valido) {
                    mensajeError = "⚠️ Por favor corrige los errores antes de continuar."
                } else {
                    mensajeError = ""
                    //Cambia la pestaña - NAVEGACION
                    navController.navigate("resultado/${nombreCompleto}/${email}")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar")
        }
    }
}

fun validarFormulario(nombre: String, email: String, contrasenia: String, confirmacionContrasenia: String, telefono:String, onError: (campo: String, mensaje: String) -> Unit): Boolean {
    var esValido = true

    //Validacion nombre
    if (nombre.isBlank()) {
        onError("nombre", "El nombre no puede estar vacío")
        esValido = false
    }

    //Validacion Email
    if (email.isBlank()) {
        onError("email", "El correo no puede estar vacío")
        esValido = false
    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        onError("email", "Correo electrónico inválido")
        esValido = false
    }

    //Validacion Contrasenia
    if(contrasenia.isBlank()){
        onError("contrasenia", "La contraseña no puede estar vacia")
        esValido = false
    }
    else if (!contrasenia.equals(confirmacionContrasenia)){
        onError("contrasenia", "Las contraseñas deben coincidir")
        esValido = false
    }


    if(telefono.length > 0 && telefono.length < 9){
        onError("telefono" , "Debes ingresar un telefono valido")
        esValido = false
    }

    if(confirmacionContrasenia.isBlank()){
        onError("confirmacionContrasenia", "Debes confirmar la contraseña")
    }

    return esValido
}
