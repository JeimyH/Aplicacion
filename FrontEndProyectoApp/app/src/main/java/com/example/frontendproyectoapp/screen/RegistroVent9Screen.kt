package com.example.frontendproyectoapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendproyectoapp.viewModel.UsuarioViewModel
import kotlinx.coroutines.launch

@Composable
fun RegistroVent9Screen(navController: NavController, viewModel: UsuarioViewModel) {
    RegistroVent9ScreenContent(
        viewModel = viewModel,
        onBackClick = { navController.popBackStack() },
        onClick = { navController.navigate("inicio") }
    )
}

@Composable
fun RegistroVent9ScreenContent(
    viewModel: UsuarioViewModel,
    onClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

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
            confirmButton = {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                )
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // Barra de progreso
            LinearProgressIndicator(
                progress = 6 / 6f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            // Botón de retroceso
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Campo: Nombre
                OutlinedTextField(
                    value = viewModel.nombre,
                    onValueChange = {
                        viewModel.nombre = it
                        viewModel.nombreValidationError = viewModel.validateNombre(it)
                    },
                    isError = viewModel.nombreValidationError != null,
                    label = { Text("Nombre de Usuario") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                )
                viewModel.nombreValidationError?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                // Campo: Correo
                OutlinedTextField(
                    value = viewModel.correo,
                    onValueChange = {
                        viewModel.correo = it
                        viewModel.correoValidationError = viewModel.validateEmail(it)
                    },
                    isError = viewModel.correoValidationError != null,
                    label = { Text("Correo") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                )
                viewModel.correoValidationError?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                // Campo: Contraseña
                OutlinedTextField(
                    value = viewModel.contrasena,
                    onValueChange = {
                        viewModel.contrasena = it
                        viewModel.contrasenaValidationError = viewModel.validatePassword(it)
                        viewModel.confirmarContrasenaValidationError =
                            viewModel.validateConfirmPassword(viewModel.confirmarContrasena, it)
                    },
                    isError = viewModel.contrasenaValidationError != null,
                    label = { Text("Contraseña") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                )
                viewModel.contrasenaValidationError?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                // Campo: Confirmar Contraseña
                OutlinedTextField(
                    value = viewModel.confirmarContrasena,
                    onValueChange = {
                        viewModel.confirmarContrasena = it
                        viewModel.confirmarContrasenaValidationError =
                            viewModel.validateConfirmPassword(viewModel.contrasena, it)
                    },
                    isError = viewModel.confirmarContrasenaValidationError != null,
                    label = { Text("Confirmar Contraseña") },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (confirmPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                )
                viewModel.confirmarContrasenaValidationError?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (!viewModel.cargando) {
                            viewModel.registrarUsuario { success ->
                                if (success) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Usuario registrado exitosamente")
                                        onClick()
                                    }
                                }
                            }
                        }
                    },
                    enabled = !viewModel.cargando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Registrarse", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistroVent9ScreenPreview(viewModel: UsuarioViewModel = viewModel()) {
    RegistroVent9ScreenContent(viewModel = viewModel)
}


