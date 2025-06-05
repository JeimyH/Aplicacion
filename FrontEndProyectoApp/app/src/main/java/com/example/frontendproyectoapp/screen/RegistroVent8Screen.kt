package com.example.frontendproyectoapp.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendproyectoapp.model.UserPreferences
import com.example.frontendproyectoapp.viewModel.UsuarioViewModel


@Composable
fun RegistroVent8Screen(navController: NavController, viewModel: UsuarioViewModel) {
    RegistroVent8ScreenContent(
        viewModel = viewModel,
        onBackClick = { navController.popBackStack() },
        onClick = { navController.navigate("inicio") }
    )
}

@Composable
fun RegistroVent8ScreenContent(
    viewModel: UsuarioViewModel,
    onClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }

    var correoError by remember { mutableStateOf("") }
    var contrasenaError by remember { mutableStateOf("") }
    var confirmarContrasenaError by remember { mutableStateOf("") }

    val context = LocalContext.current

    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")

    if (viewModel.cargando) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Creando cuenta") },
            text = { Text("Espera un momento...") },
            confirmButton = {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Botón atrás
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
        }

        // Contenido centrado
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre de Usuario") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    correoError = if (emailRegex.matches(it)) "" else "Correo no válido"
                },
                label = { Text("Correo") },
                isError = correoError.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            if (correoError.isNotEmpty()) {
                Text(
                    text = correoError,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            OutlinedTextField(
                value = contrasena,
                onValueChange = {
                    contrasena = it
                    contrasenaError = if (passwordRegex.matches(it)) "" else
                        "Mínimo 8 caracteres, una mayúscula, una minúscula y un número"
                    // También actualizar la confirmación al cambiar la contraseña
                    confirmarContrasenaError = if (confirmarContrasena == contrasena) "" else "Las contraseñas no coinciden"
                },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                isError = contrasenaError.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            if (contrasenaError.isNotEmpty()) {
                Text(
                    text = contrasenaError,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            OutlinedTextField(
                value = confirmarContrasena,
                onValueChange = {
                    confirmarContrasena = it
                    confirmarContrasenaError = if (it == contrasena) "" else "Las contraseñas no coinciden"
                },
                label = { Text("Confirmar Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                isError = confirmarContrasenaError.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            if (confirmarContrasenaError.isNotEmpty()) {
                Text(
                    text = confirmarContrasenaError,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            viewModel.errorRegistro?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = Color.Red)
            }
        }

        // Botón Registrarse en la parte inferior
        Button(
            onClick = {
                // Validar antes de registrar
                val isEmailValid = emailRegex.matches(correo)
                val isPasswordValid = passwordRegex.matches(contrasena)
                val isConfirmPasswordValid = confirmarContrasena == contrasena

                if (!isEmailValid) {
                    correoError = "Correo no válido"
                    return@Button
                }
                if (!isPasswordValid) {
                    contrasenaError = "Mínimo 8 caracteres, una mayúscula, una minúscula y un número"
                    return@Button
                }
                if (!isConfirmPasswordValid) {
                    confirmarContrasenaError = "Las contraseñas no coinciden"
                    return@Button
                }

                viewModel.nombre = nombre
                viewModel.correo = correo
                viewModel.contrasena = contrasena

                viewModel.registrarUsuario { success ->
                    if (success) {
                        Toast.makeText(context, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()
                        onClick()
                    } else {
                        Toast.makeText(context, viewModel.errorRegistro ?: "Error desconocido", Toast.LENGTH_LONG).show()
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text("Registrarse")
        }
    }
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistroVent8ScreenPreview(viewModel: UsuarioViewModel = viewModel()) {
    RegistroVent8ScreenContent(viewModel = viewModel)
}
