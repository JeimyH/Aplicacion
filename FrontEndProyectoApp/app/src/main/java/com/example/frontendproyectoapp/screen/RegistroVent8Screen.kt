package com.example.frontendproyectoapp.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendproyectoapp.DTO.UsuarioEntradaDTO
import com.example.frontendproyectoapp.viewModel.RegistroViewModel
import com.example.frontendproyectoapp.viewModel.UsuarioViewModel


@Composable
fun RegistroVent8Screen(navController: NavController, viewModel: RegistroViewModel) {
    RegistroVent8ScreenContent(
        viewModel = viewModel,
        onBackClick = { navController.popBackStack() },
        onClick = { navController.navigate("inicio") }
    )
}

@Composable
fun RegistroVent8ScreenContent(
    viewModel: RegistroViewModel,
    onClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val usuarioViewModel = remember { UsuarioViewModel() }

    if (showDialog) {
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
                viewModel.nombre = nombre
                viewModel.correo = correo
                viewModel.contrasena = contrasena

                val usuario = UsuarioEntradaDTO(
                    correo = viewModel.correo,
                    contrasena = viewModel.contrasena,
                    nombre = viewModel.nombre,
                    fechaNacimiento = viewModel.fechaNacimiento.toString(),
                    altura = viewModel.altura,
                    peso = viewModel.peso,
                    sexo = viewModel.sexo,
                    restriccionesDieta = viewModel.restriccionesDieta,
                    objetivosSalud = viewModel.objetivosSalud,
                    pesoObjetivo = viewModel.pesoObjetivo
                )

                showDialog = true
                isLoading = true

                usuarioViewModel.registrarUsuario(usuario) { success ->
                    isLoading = false
                    showDialog = false
                    if (success) {
                        Toast.makeText(context, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()
                        onClick()
                    } else {
                        Toast.makeText(context, "Error al registrar", Toast.LENGTH_LONG).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistroVent8ScreenPreview(viewModel: RegistroViewModel = viewModel()) {
    RegistroVent8ScreenContent(viewModel = viewModel)
}
