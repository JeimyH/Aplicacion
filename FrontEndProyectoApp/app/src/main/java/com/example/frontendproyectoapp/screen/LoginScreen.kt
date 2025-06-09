package com.example.frontendproyectoapp.screen

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
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
    val uiState by viewModel.uiState

    LoginScreenContent(
        onLoginClick = { correo, password ->
            viewModel.login(correo, password)
        },
        onBackClick = { navController.popBackStack() },
        uiState = uiState,
        onSuccess = {
            navController.navigate("inicio") {
                popUpTo("login") { inclusive = true }
            }
            viewModel.resetState()
        }
    )
}

@Composable
fun LoginScreenContent(
    onLoginClick: (String, String) -> Unit = { _, _ -> },
    onBackClick: () -> Unit = {},
    uiState: LoginUiState = LoginUiState.Idle,
    onSuccess: () -> Unit = {}
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    /*
    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            onSuccess()
        }
    }
    */

    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            onSuccess()
        } else if (uiState is LoginUiState.Error) {
            snackbarHostState.showSnackbar(
                message = uiState.message,
                duration = SnackbarDuration.Short
            )
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
                onValueChange = { correo = it },
                label = { Text("Correo") },
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

            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(20.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                enabled = uiState != LoginUiState.Loading,
                onClick = {
                    if (correo.isNotBlank() && contrasena.isNotBlank() &&
                        android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()
                    ) {
                        onLoginClick(correo, contrasena)
                    } else {
                        println("Mostrando Snackbar de error")
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Por favor, ingresa un correo y contraseña válidos.",
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

            TextButton(onClick = { /* Navegar a recuperación o registro */ }) {
                Text("¿Olvidaste tu contraseña?")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreenContent()
}
