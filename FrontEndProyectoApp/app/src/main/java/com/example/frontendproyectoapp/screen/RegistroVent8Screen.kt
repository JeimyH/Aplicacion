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
import androidx.lifecycle.viewmodel.compose.viewModel
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
    var correo by remember { mutableStateOf(viewModel.nombre) }
    var contrasena by remember { mutableStateOf(viewModel.correo) }
    var nombre by remember { mutableStateOf(viewModel.contrasena) }

    val context = LocalContext.current

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
        }

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
            onValueChange = { correo = it },
            label = { Text("Correo") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = {
                // Actualiza los valores en el ViewModel
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }
        viewModel.errorRegistro?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = Color.Red)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistroVent8ScreenPreview(viewModel: UsuarioViewModel = viewModel()) {
    RegistroVent8ScreenContent(viewModel = viewModel)
}
