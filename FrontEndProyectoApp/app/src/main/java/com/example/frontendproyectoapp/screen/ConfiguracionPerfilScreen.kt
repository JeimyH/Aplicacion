package com.example.frontendproyectoapp.screen

import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import com.example.frontendproyectoapp.DataStores.UserPreferences
import com.example.frontendproyectoapp.viewModel.LoginViewModel
import com.example.frontendproyectoapp.viewModel.LoginViewModelFactory
import com.example.frontendproyectoapp.viewModel.UsuarioViewModel
import com.example.frontendproyectoapp.viewModel.UsuarioViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun ConfiguracionPerfilScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val usuarioViewModel: UsuarioViewModel = viewModel(
        factory = UsuarioViewModelFactory(application)
    )
    val loginViewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(application)
    )

    // Recupera idUsuario desde DataStore como Flow y conviértelo a Long
    val idUsuario by UserPreferences.obtenerIdUsuario(context)
        .map { it ?: 0L } // si es null, por defecto 0
        .collectAsState(initial = 0L)

    val scope = rememberCoroutineScope()

    LaunchedEffect(idUsuario) {
        if (idUsuario != 0L) {
            usuarioViewModel.cargarUsuario(idUsuario)
        }
    }

    ConfiguracionPerfilScreenContent(
        navController = navController,
        viewModel = usuarioViewModel,
        onBackClick = { navController.popBackStack() },
        idUsuario = idUsuario,
        onLogout = {
            scope.launch {
                loginViewModel.logout()
                navController.navigate("login") {
                    popUpTo("inicio") { inclusive = true }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionPerfilScreenContent(
    navController: NavHostController,
    viewModel: UsuarioViewModel,
    onBackClick: () -> Unit = {},
    idUsuario: Long,
    onLogout: () -> Unit
) {
    // Observar los estados de actualización para altura y peso
    val actualizacionAlturaState by viewModel.actualizacionAlturaState.collectAsState()
    val actualizacionPesoState by viewModel.actualizacionPesoState.collectAsState()
    val actualizacionPesoObjetivoState by viewModel.actualizacionPesoObjetivoState.collectAsState()
    val actualizacionCorreoState by viewModel.actualizacionCorreoState.collectAsState()
    val actualizacionDietaState by viewModel.actualizacionDietaState.collectAsState()
    val actualizacionObjetivoState by viewModel.actualizacionObjetivoState.collectAsState()
    val actualizacionNivelActState by viewModel.actualizacionNivelActState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val context = LocalContext.current

    // Usar un LaunchedEffect para reaccionar a los cambios de estado
    // Se observa tanto la altura como el peso

    LaunchedEffect(
        actualizacionAlturaState, actualizacionPesoState,
        actualizacionPesoObjetivoState, actualizacionCorreoState,
        actualizacionDietaState, actualizacionObjetivoState, actualizacionNivelActState
    ) {
        // Maneja el resultado de la actualización de altura
        actualizacionAlturaState?.let { result ->
            result.onSuccess { usuarioActualizado ->
                viewModel.alturaUsuario = usuarioActualizado.altura.toString()
                viewModel.mensajeActualizacionInformacion = "Altura actualizada con éxito."
                viewModel.isEditingAltura = false
                keyboardController?.hide()
                viewModel.clearActualizacionState()
            }.onFailure { throwable ->
                viewModel.mensajeActualizacionInformacion = "Error al actualizar la altura: ${throwable.message}"
                viewModel.isEditingAltura = false
                keyboardController?.hide()
                viewModel.clearActualizacionState()
            }
        }

        // Maneja el resultado de la actualización de peso
        actualizacionPesoState?.let { result ->
            result.onSuccess { usuarioActualizado ->
                viewModel.pesoUsuario = usuarioActualizado.peso.toString()
                viewModel.mensajeActualizacionInformacion = "Peso actualizado con éxito."
                viewModel.isEditingPeso = false
                keyboardController?.hide()
                viewModel.clearActualizacionState()
            }.onFailure { throwable ->
                viewModel.mensajeActualizacionInformacion = "Error al actualizar el peso: ${throwable.message}"
                viewModel.isEditingPeso = false
                keyboardController?.hide()
                viewModel.clearActualizacionState()
            }
        }

        // Maneja el resultado de la actualización de peso Objetivo
        actualizacionPesoObjetivoState?.let { result ->
            result.onSuccess { usuarioActualizado ->
                viewModel.pesoObjetivoUsuario = usuarioActualizado.pesoObjetivo.toString()
                viewModel.mensajeActualizacionObjetivos = "Peso Objetivo actualizado con éxito."
                viewModel.isEditingPesoObjetivo = false
                keyboardController?.hide()
                viewModel.clearActualizacionState()
            }.onFailure { throwable ->
                viewModel.mensajeActualizacionObjetivos = "Error al actualizar el peso objetivo: ${throwable.message}"
                viewModel.isEditingPesoObjetivo = false
                keyboardController?.hide()
                viewModel.clearActualizacionState()
            }
        }

        // Maneja el resultado de la actualización del correo
        actualizacionCorreoState?.let { result ->
            result.onSuccess { usuarioActualizado ->
                viewModel.correoUsuario = usuarioActualizado.correo
                viewModel.mensajeActualizacionCuenta = "Correo actualizado con éxito."
                UserPreferences.guardarCorreoUsuario(context, usuarioActualizado.correo)
                viewModel.isEditingCorreo = false
                keyboardController?.hide()
                viewModel.clearActualizacionState()
            }.onFailure { throwable ->
                viewModel.mensajeActualizacionCuenta = "Error al actualizar el correo: ${throwable.message}"
                viewModel.isEditingCorreo = false
                keyboardController?.hide()
                viewModel.clearActualizacionState()
            }
        }

        actualizacionDietaState?.let { result ->
            result.onSuccess { usuarioActualizado ->
                viewModel.restriccionesDietaUsuario = usuarioActualizado.restriccionesDieta ?: ""
                viewModel.mensajeActualizacionInformacion = "Dieta actualizada con éxito."
                viewModel.isEditingDieta = false
                viewModel.clearActualizacionState()
            }.onFailure { throwable ->
                viewModel.mensajeActualizacionInformacion = "Error al actualizar la dieta: ${throwable.message}"
                viewModel.isEditingDieta = false
                viewModel.clearActualizacionState()
            }
        }

        actualizacionObjetivoState?.let { result ->
            result.onSuccess { usuarioActualizado ->
                viewModel.objetivosSaludUsuario = usuarioActualizado.objetivosSalud ?: ""
                viewModel.mensajeActualizacionObjetivos = "Objetivo de Salud actualizado con éxito."
                viewModel.isEditingObjetivo = false
                viewModel.clearActualizacionState()
            }.onFailure { throwable ->
                viewModel.mensajeActualizacionObjetivos = "Error al actualizar el objetivo de salud: ${throwable.message}"
                viewModel.isEditingObjetivo = false
                viewModel.clearActualizacionState()
            }
        }

        actualizacionNivelActState?.let { result ->
            result.onSuccess { usuarioActualizado ->
                viewModel.nivelActividadUsuario = usuarioActualizado.nivelActividad ?: ""
                viewModel.mensajeActualizacionInformacion = "Nivel de Actividad actualizado con éxito."
                viewModel.isEditingNivelAct = false
                viewModel.clearActualizacionState()
            }.onFailure { throwable ->
                viewModel.mensajeActualizacionInformacion = "Error al actualizar el nivel de actividad: ${throwable.message}"
                viewModel.isEditingNivelAct = false
                viewModel.clearActualizacionState()
            }
        }

    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Perfil",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            // Header
            ConfiguracionPerfilHeader(nombre = viewModel.nombreUsuario)

            Spacer(modifier = Modifier.height(24.dp))

            // Información Personal
            Text("Información Personal", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            // Campos de solo lectura para Género y Edad
            DisplayFieldCard("Genero", viewModel.sexoUsuario)
            DisplayFieldCard("Edad", "${viewModel.calcularEdadReg7(viewModel.fechaNacimientoUsuario)} años")

            // Campo de Altura (editable)
            EditableFieldCard(
                label = "Altura",
                value = if (viewModel.isEditingAltura) viewModel.nuevaAltura else "${viewModel.alturaUsuario} cm",
                isEditing = viewModel.isEditingAltura,
                onValueChange = { viewModel.nuevaAltura = it },
                onClickEdit = {
                    if (!viewModel.isEditingAltura) {
                        viewModel.nuevaAltura = viewModel.alturaUsuario
                    }
                    viewModel.toggleEditingAltura()
                },
                onSave = {
                    keyboardController?.hide()
                    val nuevaAlturaFloat = viewModel.nuevaAltura.toFloatOrNull()
                    if (nuevaAlturaFloat != null) {
                        viewModel.actualizarAltura(idUsuario, nuevaAlturaFloat)
                    } else {
                        viewModel.mensajeActualizacionInformacion = "La altura debe ser un número válido."
                    }
                }
            )

            // Campo de Peso Actual (editable)
            EditableFieldCard(
                label = "Peso Actual",
                value = if (viewModel.isEditingPeso) viewModel.nuevoPeso else "${viewModel.pesoUsuario} kg",
                isEditing = viewModel.isEditingPeso,
                onValueChange = { viewModel.nuevoPeso = it },
                onClickEdit = {
                    if (!viewModel.isEditingPeso) {
                        viewModel.nuevoPeso = viewModel.pesoUsuario
                    }
                    viewModel.toggleEditingPeso()
                },
                onSave = {
                    keyboardController?.hide()
                    val nuevoPesoFloat = viewModel.nuevoPeso.toFloatOrNull()
                    if (nuevoPesoFloat != null) {
                        viewModel.actualizarPeso(idUsuario, nuevoPesoFloat)
                    } else {
                        viewModel.mensajeActualizacionInformacion = "El peso debe ser un número válido."
                    }
                }
            )

            DropdownFieldCard(
                label = "Tipo de Dieta",
                value = if (viewModel.isEditingDieta) viewModel.nuevoDieta else viewModel.restriccionesDietaUsuario,
                isEditing = viewModel.isEditingDieta,
                options = listOf("Recomendada", "Alta en proteínas", "Keto", "Baja en carbohidratos", "Baja en grasas"),
                onClickEdit = {
                    if (!viewModel.isEditingDieta) {
                        viewModel.nuevoDieta = viewModel.restriccionesDietaUsuario
                    } else {
                        // Cuando guarda, actualiza en backend
                        if (viewModel.nuevoDieta.isNotBlank()) {
                            viewModel.actualizarDieta(idUsuario, viewModel.nuevoDieta)
                        } else {
                            viewModel.mensajeActualizacionInformacion = "Debe seleccionar una dieta."
                        }
                    }
                    viewModel.toggleEditingDieta()
                },
                onOptionSelected = { selected ->
                    viewModel.nuevoDieta = selected
                }
            )

            DropdownFieldCard(
                label = "Nivel de Actividad",
                value = if (viewModel.isEditingNivelAct) viewModel.nuevoNivelAct else viewModel.nivelActividadUsuario,
                isEditing = viewModel.isEditingNivelAct,
                options = listOf("Sedentario", "Ligera Actividad", "Actividad Moderada", "Alta Actividad", "Actividad Extrema"),
                onClickEdit = {
                    if (!viewModel.isEditingNivelAct) {
                        viewModel.nuevoNivelAct = viewModel.nivelActividadUsuario
                    } else {
                        // Cuando guarda, actualiza en backend
                        if (viewModel.nuevoNivelAct.isNotBlank()) {
                            viewModel.actualizarNivelAct(idUsuario, viewModel.nuevoNivelAct)
                        } else {
                            viewModel.mensajeActualizacionInformacion = "Debe seleccionar un Nivel de Actividad."
                        }
                    }
                    viewModel.toggleEditingNivelAct()
                },
                onOptionSelected = { selected ->
                    viewModel.nuevoNivelAct = selected
                }
            )

            // Muestra el mensaje de éxito o error
            viewModel.mensajeActualizacionInformacion?.let { mensaje ->
                LaunchedEffect(mensaje) {
                    delay(4000) // Espera 4 segundos
                    // Limpia el mensaje en el ViewModel para ocultarlo
                    viewModel.mensajeActualizacionInformacion = null
                }

                Text(
                    text = mensaje,
                    color = if (mensaje.contains("Error")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Objetivos", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            DropdownFieldCard(
                label = "Objetivo",
                value = if (viewModel.isEditingObjetivo) viewModel.nuevoObjetivo else viewModel.objetivosSaludUsuario,
                isEditing = viewModel.isEditingObjetivo,
                options = listOf("Pérdida de peso", "Mantener Peso", "Ganancia de Masa Muscular", "Hábitos alimenticios saludables"),
                onClickEdit = {
                    if (!viewModel.isEditingObjetivo) {
                        viewModel.nuevoObjetivo = viewModel.objetivosSaludUsuario
                    } else {
                        // Cuando guarda, actualiza en backend
                        if (viewModel.nuevoObjetivo.isNotBlank()) {
                            viewModel.actualizarObjetivo(idUsuario, viewModel.nuevoObjetivo)
                        } else {
                            viewModel.mensajeActualizacionInformacion = "Debe seleccionar un Objetivo de Salud."
                        }
                    }
                    viewModel.toggleEditingObjetivo()
                },
                onOptionSelected = { selected ->
                    viewModel.nuevoObjetivo = selected
                }
            )

            // Campo de Peso Objetivo (editable)
            EditableFieldCard(
                label = "Peso Objetivo",
                value = if (viewModel.isEditingPesoObjetivo) viewModel.nuevoPesoObjetivo else "${viewModel.pesoObjetivoUsuario} kg",
                isEditing = viewModel.isEditingPesoObjetivo,
                onValueChange = { viewModel.nuevoPesoObjetivo = it },
                onClickEdit = {
                    if (!viewModel.isEditingPesoObjetivo) {
                        viewModel.nuevoPesoObjetivo = viewModel.pesoObjetivoUsuario
                    }
                    viewModel.toggleEditingPesoObjetivo()
                },
                onSave = {
                    keyboardController?.hide()
                    val nuevoPesoObjetivoFloat = viewModel.nuevoPesoObjetivo.toFloatOrNull()
                    if (nuevoPesoObjetivoFloat != null) {
                        viewModel.actualizarPesoObjetivo(idUsuario, nuevoPesoObjetivoFloat)
                    } else {
                        viewModel.mensajeActualizacionObjetivos = "El peso objetivo debe ser un número válido."
                    }
                }
            )

            // Muestra el mensaje de éxito o error para objetivos
            viewModel.mensajeActualizacionObjetivos?.let { mensaje ->
                // Utiliza un LaunchedEffect que se dispara cuando el mensaje no es nulo
                LaunchedEffect(mensaje) {
                    delay(4000) // Espera 4 segundos
                    // Limpia el mensaje en el ViewModel para ocultarlo
                    viewModel.mensajeActualizacionObjetivos = null
                }

                Text(
                    text = mensaje,
                    color = if (mensaje.contains("Error")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Cuenta", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            // Campo de Peso Objetivo (editable)
            EditableFieldCard(
                label = "Correo",
                value = if (viewModel.isEditingCorreo) viewModel.nuevoCorreo else viewModel.correoUsuario,
                isEditing = viewModel.isEditingCorreo,
                onValueChange = { viewModel.nuevoCorreo = it },
                onClickEdit = {
                    if (!viewModel.isEditingCorreo) viewModel.nuevoCorreo = viewModel.correoUsuario
                    viewModel.toggleEditingCorreo()
                },
                onSave = {
                    keyboardController?.hide()
                    val nuevo = viewModel.nuevoCorreo.trim()
                    if (nuevo.isNotBlank()) {
                        viewModel.actualizarCorreo(idUsuario, nuevo)
                    } else {
                        viewModel.mensajeActualizacionCuenta = "El correo no puede estar vacío."
                    }
                },
                keyboardType = KeyboardType.Email
            )

            ButtonFieldCard("Cambiar contraseña") {}

            //Spacer(modifier = Modifier.height(16.dp))

            // Sección Cuenta integrada con eliminación de usuario
            SeccionCuenta(
                navController = navController,
                viewModel = viewModel,
                idUsuario = idUsuario,
                onLogout = onLogout
            )

            // Muestra el mensaje de éxito o error para objetivos
            viewModel.mensajeActualizacionCuenta?.let { mensaje ->
                // Utiliza un LaunchedEffect que se dispara cuando el mensaje no es nulo
                LaunchedEffect(mensaje) {
                    delay(4000) // Espera 4 segundos
                    // Limpia el mensaje en el ViewModel para ocultarlo
                    viewModel.mensajeActualizacionCuenta = null
                }

                Text(
                    text = mensaje,
                    color = if (mensaje.contains("Error")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

        }
    }
}

@Composable
fun ConfiguracionPerfilHeader(nombre: String = "Usuario") {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // El Row ahora solo contiene la imagen para facilitar el centrado
        Row(
            modifier = Modifier.padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.22f)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = "https://cdn-icons-png.flaticon.com/128/8709/8709730.png",
                    contentDescription = "Avatar usuario",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Text(
            text = nombre,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun DisplayFieldCard(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 26.dp, horizontal = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium
            )

            // Espaciador para empujar el valor a la derecha
            Spacer(modifier = Modifier.weight(1f))

            Text(
                value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.End
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.weight(0.2f))
        }
    }
}

@Composable
fun EditableFieldCard(
    label: String,
    value: String,
    isEditing: Boolean,
    onValueChange: (String) -> Unit,
    onClickEdit: () -> Unit,
    onSave: () -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium
            )

            // Espaciador para empujar el resto de los elementos a la derecha
            Spacer(modifier = Modifier.weight(1f))

            if (isEditing) {
                // Modo de Edición
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.width(200.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { onSave() }),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                // Botón para guardar
                IconButton(onClick = onSave) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Guardar",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                // Modo de Visualización
                Text(
                    value,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.End // Alinea el texto a la derecha
                    ),
                    // Añade un espaciado horizontal para separarlo del icono
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
                // Botón para editar
                IconButton(onClick = onClickEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownFieldCard(
    label: String,
    value: String,
    isEditing: Boolean,
    options: List<String>,
    onClickEdit: () -> Unit,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.weight(1f))

            if (isEditing) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = value,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(label, color = MaterialTheme.colorScheme.onSurface) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().width(180.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    onOptionSelected(option)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            } else {
                Text(
                    value,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.End
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
            IconButton(onClick = onClickEdit) {
                Icon(
                    imageVector = if (isEditing) Icons.Default.Done else Icons.Default.Edit,
                    contentDescription = if (isEditing) "Guardar" else "Editar",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}


@Composable
fun ButtonFieldCard(label: String, textColor: Color = MaterialTheme.colorScheme.onSurface, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(modifier = Modifier.padding(vertical = 26.dp, horizontal = 12.dp), contentAlignment = Alignment.Center) {
            Text(label, style = MaterialTheme.typography.bodyMedium, color = textColor)
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun SeccionCuenta(
    navController: NavHostController,
    viewModel: UsuarioViewModel,
    idUsuario: Long,
    onLogout: () -> Unit
) {
    var mostrarDialogo by remember { mutableStateOf(false) }
    val eliminacionState by viewModel.eliminacionState.collectAsState()

    val context2 = LocalContext.current // contexto seguro fuera de LaunchedEffect

        Column(Modifier.padding(vertical = 4.dp)) {
            TextButton(onClick = onLogout) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Cerrar sesión")
            }

            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))

            TextButton(onClick = { mostrarDialogo = true }) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                Spacer(Modifier.width(8.dp))
                Text("Eliminar cuenta", color = Color.Red)
            }
        }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.eliminarCuenta(idUsuario)
                    mostrarDialogo = false
                }) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) { Text("Cancelar") }
            },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar tu cuenta? Esta acción no se puede deshacer.") }
        )
    }

    // Observa el resultado de la eliminación
    eliminacionState?.let { result ->
        result.onSuccess {
            LaunchedEffect(Unit) {
                navController.navigate("registro1")
            }
        }
        result.onFailure { throwable ->
            LaunchedEffect(throwable) {
                Toast.makeText(
                    context2,
                    "Error al eliminar cuenta: ${throwable.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
