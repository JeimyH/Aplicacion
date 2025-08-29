package com.example.frontendproyectoapp.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendproyectoapp.DataStores.dataStore
import com.example.frontendproyectoapp.viewModel.UsuarioViewModel
import com.github.barteksc.pdfviewer.PDFView
import kotlinx.coroutines.launch

@Composable
fun RegistroVent10Screen(navController: NavController, viewModel: UsuarioViewModel) {
    RegistroVent10ScreenContent(
        viewModel = viewModel,
        onBackClick = { navController.popBackStack() },
        onClick = { navController.navigate("inicio") }
    )
}

@Composable
fun RegistroVent10ScreenContent(
    viewModel: UsuarioViewModel,
    onClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var aceptaPoliticas by remember { mutableStateOf(false) }
    var aceptaTerminos by remember { mutableStateOf(false) }

    var mostrarPoliticaDialog by remember { mutableStateOf(false) }
    var mostrarTerminosDialog by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Mensaje de error en caso de fallo de registro
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

    val botonHabilitado = viewModel.nombre.isNotBlank() &&
            viewModel.correo.isNotBlank() &&
            viewModel.contrasena.isNotBlank() &&
            viewModel.confirmarContrasena.isNotBlank() &&
            viewModel.nombreValidationError == null &&
            viewModel.correoValidationError == null &&
            viewModel.contrasenaValidationError == null &&
            viewModel.confirmarContrasenaValidationError == null &&
            aceptaPoliticas && aceptaTerminos

    val alpha by animateFloatAsState(targetValue = if (botonHabilitado) 1f else 0.4f, label = "AlphaAnim")

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }, containerColor = MaterialTheme.colorScheme.background) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = 7 / 7f,
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Box(modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Atrás",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.TopStart).padding(top = 8.dp).clickable { onBackClick() }
                    )

                    Column(
                        modifier = Modifier.align(Alignment.Center).verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Crea tu cuenta",
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )

                        CampoTextoConTeclado(
                            valor = viewModel.nombre,
                            onValorCambio = {
                                viewModel.nombre = it
                                viewModel.nombreValidationError = viewModel.validateNombre(it)
                            },
                            etiqueta = "Nombre de Usuario",
                            esError = viewModel.nombreValidationError != null,
                            mensajeError = viewModel.nombreValidationError,
                            imeAction = ImeAction.Next
                        )

                        CampoTextoConTeclado(
                            valor = viewModel.correo,
                            onValorCambio = {
                                viewModel.correo = it
                                viewModel.correoValidationError = viewModel.validateEmail(it)
                            },
                            etiqueta = "Correo",
                            esError = viewModel.correoValidationError != null,
                            mensajeError = viewModel.correoValidationError,
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )

                        CampoTextoConTeclado(
                            valor = viewModel.contrasena,
                            onValorCambio = {
                                viewModel.contrasena = it
                                viewModel.contrasenaValidationError = viewModel.validatePassword(it)
                                viewModel.confirmarContrasenaValidationError =
                                    viewModel.validateConfirmPassword(viewModel.confirmarContrasena, it)
                            },
                            etiqueta = "Contraseña",
                            esError = viewModel.contrasenaValidationError != null,
                            mensajeError = viewModel.contrasenaValidationError,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = { IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = null)
                            } },
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        )

                        CampoTextoConTeclado(
                            valor = viewModel.confirmarContrasena,
                            onValorCambio = {
                                viewModel.confirmarContrasena = it
                                viewModel.confirmarContrasenaValidationError =
                                    viewModel.validateConfirmPassword(viewModel.contrasena, it)
                            },
                            etiqueta = "Confirmar Contraseña",
                            esError = viewModel.confirmarContrasenaValidationError != null,
                            mensajeError = viewModel.confirmarContrasenaValidationError,
                            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = { IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(imageVector = if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = null)
                            } },
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            "Debes aceptar las políticas de privacidad y los términos y condiciones para poder registrarte",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )

                        // Checkbox Política
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = aceptaPoliticas,
                                onCheckedChange = { aceptaPoliticas = it },
                                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            TextButton(onClick = { mostrarPoliticaDialog = true }) {
                                Text("Política de Privacidad    ", color = MaterialTheme.colorScheme.primary)
                            }
                        }

                        // Checkbox Términos
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = aceptaTerminos,
                                onCheckedChange = { aceptaTerminos = it },
                                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            TextButton(onClick = { mostrarTerminosDialog = true }) {
                                Text("Términos y Condiciones", color = MaterialTheme.colorScheme.primary)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                // Botón de registro
                Button(
                    onClick = {
                        if (!botonHabilitado) {
                            scope.launch { snackbarHostState.showSnackbar("Completa todos los campos y acepta las políticas y términos.") }
                            return@Button
                        }

                        if (!viewModel.cargando) {
                            viewModel.registrarUsuario { success ->
                                if (success) {
                                    scope.launch {
                                        context.dataStore.edit { prefs ->
                                            prefs[booleanPreferencesKey("politica_aceptada")] = aceptaPoliticas
                                            prefs[booleanPreferencesKey("terminos_aceptados")] = aceptaTerminos
                                        }
                                        snackbarHostState.showSnackbar("Usuario registrado exitosamente")
                                        onClick()
                                    }
                                }
                            }
                        }
                    },
                    enabled = botonHabilitado && !viewModel.cargando,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).alpha(alpha),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                    )
                ) {
                    Text("Registrarse", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        if (mostrarPoliticaDialog) {
            AlertDialog(
                onDismissRequest = { mostrarPoliticaDialog = false },
                title = { Text("Política de Privacidad") },

                // Modificar para incluir el documento correcto

                text = { PDFViewerFromAssets("ee_formato_ieee.pdf") },
                confirmButton = { TextButton(onClick = { mostrarPoliticaDialog = false }) { Text("Cerrar") } }
            )
        }

        if (mostrarTerminosDialog) {
            AlertDialog(
                onDismissRequest = { mostrarTerminosDialog = false },
                title = { Text("Términos y Condiciones") },

                // Modificar para incluir el documento correcto

                text = { PDFViewerFromAssets("ee_formato_ieee.pdf") },
                confirmButton = { TextButton(onClick = { mostrarTerminosDialog = false }) { Text("Cerrar") } }
            )
        }
    }
}

@Composable
fun CampoTextoConTeclado(
    valor: String,
    onValorCambio: (String) -> Unit,
    etiqueta: String,
    esError: Boolean = false,
    mensajeError: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = valor,
            onValueChange = onValorCambio,
            label = { Text(etiqueta, style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant) },
            isError = esError,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                },
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                errorContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp)
        )
        if (esError && mensajeError != null) {
            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
fun PDFViewerFromAssets(fileName: String) {
    AndroidView(factory = { context ->
        PDFView(context, null).apply {
            fromAsset(fileName)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .load()
        }
    }, modifier = Modifier.fillMaxSize())
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistroVent10ScreenPreview(viewModel: UsuarioViewModel = viewModel()) {
    RegistroVent10ScreenContent(viewModel = viewModel)
}


