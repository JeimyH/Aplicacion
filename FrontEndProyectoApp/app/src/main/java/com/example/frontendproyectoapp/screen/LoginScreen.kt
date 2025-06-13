package com.example.frontendproyectoapp.screen

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.frontendproyectoapp.viewModel.LoginUiState
import com.example.frontendproyectoapp.viewModel.LoginViewModel
import com.example.frontendproyectoapp.viewModel.LoginViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(context.applicationContext as Application)
    )

    LoginScreenContent(
        viewModel = viewModel,
        onLoginClick = {
            // Solo intenta loguear si el correo es válido
            val correoValido = viewModel.validateCorreo(viewModel.correo) == null
            if (correoValido) {
                viewModel.login(viewModel.correo, viewModel.contrasena)
            }
        },
        onBackClick = {
            navController.popBackStack()
        },
        onSuccess = {
            // Navega a la pantalla de inicio si el login fue exitoso
            navController.navigate("inicio") {
                popUpTo("login") { inclusive = true }
            }
            viewModel.resetState()
        }
    )
}


@Composable
fun LoginScreenContent(
    viewModel: LoginViewModel,
    onLoginClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onSuccess: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.uiState

    val correo by remember { derivedStateOf { viewModel.correo } }
    val contrasena by remember { derivedStateOf { viewModel.contrasena } }
    val correoValidationError by remember { derivedStateOf { viewModel.correoValidationError } }

    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.uiState.value) {
        when (val state = viewModel.uiState.value) {
            is LoginUiState.Success -> onSuccess()
            is LoginUiState.Error -> {
                snackbarHostState.showSnackbar(
                    message = state.message, // <-- Aquí se hace cast explícito
                    duration = SnackbarDuration.Short
                )
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Bienvenido de nuevo",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Inicia sesión con tu cuenta de DIETASMART",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { viewModel.onCorreoChanged(it) },
                label = { Text("Correo") },
                isError = correoValidationError != null,
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(20.dp)
            )
            if (correoValidationError != null) {
                Text(
                    text = correoValidationError ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            OutlinedTextField(
                value = contrasena,
                onValueChange = { viewModel.onContrasenaChanged(it) },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(20.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                enabled = uiState != LoginUiState.Loading,
                onClick = {
                    if (correoValidationError == null) {
                        onLoginClick()
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Por favor, ingresa un correo válido.",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (uiState == LoginUiState.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Iniciar sesión")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = {
                // Aquí puedes navegar a una pantalla de recuperación de contraseña
            }) {
                Text("¿Olvidaste tu contraseña?")
            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    // Muestra contenido sin ViewModel (modo preview)
    LoginScreenContent(
        viewModel = object : LoginViewModel(Application()) {
            override val uiState: State<LoginUiState> = mutableStateOf(LoginUiState.Idle)
        }
    )
}
 */