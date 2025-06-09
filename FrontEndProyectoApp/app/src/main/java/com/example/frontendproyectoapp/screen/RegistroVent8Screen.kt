package com.example.frontendproyectoapp.screen

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
import com.example.frontendproyectoapp.viewModel.UsuarioViewModel
import kotlinx.coroutines.launch


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

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.errorRegistro) {
        viewModel.errorRegistro?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.errorRegistro = null
        }
    }

    if (viewModel.cargando) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Creando cuenta") },
            text = { Text("Espera un momento...") },
            confirmButton = { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
        )
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }

            Column(
                modifier = Modifier.align(Alignment.Center).padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        viewModel.nombreValidationError = viewModel.validateNombre(it)
                        viewModel.verificarNombreExistente(it)
                    },
                    isError = viewModel.nombreValidationError != null,
                    label = { Text("Nombre de Usuario") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                viewModel.nombreValidationError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(start = 8.dp))
                }

                OutlinedTextField(
                    value = correo,
                    onValueChange = {
                        correo = it
                        viewModel.correoValidationError = viewModel.validateEmail(it)
                        viewModel.verificarCorreoExistente(it)
                    },
                    isError = viewModel.correoValidationError != null,
                    label = { Text("Correo") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                viewModel.correoValidationError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(start = 8.dp))
                }

                OutlinedTextField(
                    value = contrasena,
                    onValueChange = {
                        contrasena = it
                        viewModel.contrasenaValidationError = viewModel.validatePassword(it)
                        viewModel.confirmarContrasenaValidationError = viewModel.validateConfirmPassword(confirmarContrasena, it)
                    },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = viewModel.contrasenaValidationError != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                viewModel.contrasenaValidationError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(start = 8.dp))
                }

                OutlinedTextField(
                    value = confirmarContrasena,
                    onValueChange = {
                        confirmarContrasena = it
                        viewModel.confirmarContrasenaValidationError = viewModel.validateConfirmPassword(contrasena, it)
                    },
                    label = { Text("Confirmar Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = viewModel.confirmarContrasenaValidationError != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                viewModel.confirmarContrasenaValidationError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(start = 8.dp))
                }
            }

            Button(
                onClick = {
                    // Validar todo
                    viewModel.nombreValidationError = viewModel.validateNombre(nombre)
                    viewModel.correoValidationError = viewModel.validateEmail(correo)
                    viewModel.contrasenaValidationError = viewModel.validatePassword(contrasena)
                    viewModel.confirmarContrasenaValidationError = viewModel.validateConfirmPassword(contrasena, confirmarContrasena)

                    if (
                        viewModel.nombreValidationError != null ||
                        viewModel.correoValidationError != null ||
                        viewModel.contrasenaValidationError != null ||
                        viewModel.confirmarContrasenaValidationError != null
                    ) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Por favor corrige los errores antes de continuar")
                        }
                        return@Button
                    }

                    viewModel.nombre = nombre
                    viewModel.correo = correo
                    viewModel.contrasena = contrasena

                    viewModel.registrarUsuario { success ->
                        if (success) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Usuario registrado exitosamente")
                            }
                            onClick()
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
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistroVent8ScreenPreview(viewModel: UsuarioViewModel = viewModel()) {
    RegistroVent8ScreenContent(viewModel = viewModel)
}


