package com.example.frontendproyectoapp.screen

import android.app.Application
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(context.applicationContext as Application)
    )

    LoginScreenContent(
        viewModel = viewModel,
        onLoginClick = {
            val correoValido = viewModel.validateCorreo(viewModel.correo) == null
            if (correoValido) {
                viewModel.login(viewModel.correo, viewModel.contrasena)
            }
        },
        onBackClick = {
            navController.navigate("registro1")
        },
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
    viewModel: LoginViewModel,
    onLoginClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onSuccess: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.uiState
    val correo by remember { derivedStateOf { viewModel.correo } }
    val contrasena by remember { derivedStateOf { viewModel.contrasena } }
    val correoValidationError by remember { derivedStateOf { viewModel.correoValidationError } }

    var passwordVisible by remember { mutableStateOf(false) }

    val botonHabilitado = correo.isNotBlank() &&
            contrasena.isNotBlank() &&
            correoValidationError == null &&
            uiState != LoginUiState.Loading

    val alpha by animateFloatAsState(
        targetValue = if (botonHabilitado) 1f else 0.4f,
        label = "AlphaAnim"
    )

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is LoginUiState.Success -> onSuccess()
            is LoginUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures { focusManager.clearFocus(); keyboardController?.hide() }
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .clickable { onBackClick() }
                            .padding(top = 8.dp)
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Bienvenido de nuevo",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )

                        Text(
                            text = "Inicia sesión con tu cuenta de DietaSmart",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        CampoTextoConTeclado(
                            valor = correo,
                            onValorCambio = { viewModel.onCorreoChanged(it) },
                            etiqueta = "Correo",
                            esError = correoValidationError != null,
                            mensajeError = correoValidationError,
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )

                        CampoTextoConTeclado(
                            valor = contrasena,
                            onValorCambio = { viewModel.onContrasenaChanged(it) },
                            etiqueta = "Contraseña",
                            esError = false,
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible)
                                            Icons.Default.VisibilityOff
                                        else
                                            Icons.Default.Visibility,
                                        contentDescription = null
                                    )
                                }
                            }
                        )

                        TextButton(onClick = {
                            // Acción para recuperar contraseña
                        }) {
                            Text("¿Olvidaste tu contraseña?", color = MaterialTheme.colorScheme.primary)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Button(
                    onClick = {
                        if (botonHabilitado) {
                            onLoginClick()
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Completa todos los campos correctamente.")
                            }
                        }
                    },
                    enabled = botonHabilitado,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .alpha(alpha),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                    )
                ) {
                    if (uiState == LoginUiState.Loading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Iniciar sesión", style = MaterialTheme.typography.labelLarge)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
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