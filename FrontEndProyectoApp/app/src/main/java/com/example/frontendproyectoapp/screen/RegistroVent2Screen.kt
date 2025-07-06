package com.example.frontendproyectoapp.screen

import android.app.DatePickerDialog
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.frontendproyectoapp.viewModel.UsuarioViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RegistroVent2Screen(navController: NavController, viewModel: UsuarioViewModel) {
    RegistroVent2ScreenContent(
        viewModel = viewModel,
        onBackClick = { navController.popBackStack() },
        onContinuarClick = { navController.navigate("registro3") }
    )
}

@Composable
fun RegistroVent2ScreenContent(
    onBackClick: () -> Unit = {},
    onContinuarClick: () -> Unit = {},
    viewModel: UsuarioViewModel
) {
    val sexos = listOf("Masculino", "Femenino", "Otro")
    val alturas = (100..240).map { "$it cm" }
    val pesos = (40..200).map { "$it kg" }

    var sexo by remember { mutableStateOf(viewModel.sexo.ifEmpty { "" }) }
    var altura by remember { mutableStateOf(if (viewModel.altura > 0) "${viewModel.altura.toInt()} cm" else "") }
    var peso by remember { mutableStateOf(if (viewModel.peso > 0) "${viewModel.peso.toInt()} kg" else "") }

    var expandedSexo by remember { mutableStateOf(false) }
    var expandedAltura by remember { mutableStateOf(false) }
    var expandedPeso by remember { mutableStateOf(false) }

    var edadUsuario by remember { mutableStateOf(0) }
    var mostrarAdvertenciaEdad by remember { mutableStateOf(false) }

    val context = LocalContext.current
    var fechaNacimiento by remember {
        mutableStateOf(viewModel.fechaNacimiento.ifEmpty { "" })
    }

    val camposValidos = sexo.isNotBlank() && altura.isNotBlank() && peso.isNotBlank() && fechaNacimiento.isNotBlank() && edadUsuario >= 18

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            fechaNacimiento = format.format(selectedDate.time)
            viewModel.fechaNacimiento = fechaNacimiento

            edadUsuario = viewModel.calcularEdadCalendario(fechaNacimiento)
            mostrarAdvertenciaEdad = edadUsuario < 18
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val alpha by animateFloatAsState(targetValue = if (camposValidos) 1f else 0.4f, label = "AlphaAnim")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        // Progreso paso 1 de 6
        LinearProgressIndicator(
            progress = 1 / 6f,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Box(modifier = Modifier.weight(1f)) {
            // Icono de volver
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Atrás",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 8.dp)
                    .clickable { onBackClick() }
            )

            // Contenido con scroll
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Cuéntanos sobre ti",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                // GÉNERO
                DropdownSelector(
                    label = "Género",
                    selected = if (sexo.isBlank()) "Selecciona tu género" else sexo,
                    options = sexos,
                    expanded = expandedSexo,
                    onExpandedChange = { expandedSexo = it },
                    onItemSelected = {
                        sexo = it
                        viewModel.sexo = it
                        expandedSexo = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                if (sexo.isBlank()) {
                    Text("Campo obligatorio", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
                }

                // FECHA DE NACIMIENTO
                OutlinedTextField(
                    value = if (fechaNacimiento.isBlank()) "Selecciona tu fecha de nacimiento" else fechaNacimiento,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fecha de nacimiento", color = MaterialTheme.colorScheme.onBackground) },
                    trailingIcon = {
                        IconButton(onClick = { datePickerDialog.show() }) {
                            Icon(Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                    },
                    isError = mostrarAdvertenciaEdad,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        errorContainerColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                if (mostrarAdvertenciaEdad) {
                    Text(
                        text = "Debes tener al menos 18 años para registrarte",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // ALTURA
                DropdownSelector(
                    label = "Altura",
                    selected = if (altura.isBlank()) "Selecciona tu altura" else altura,
                    options = alturas,
                    expanded = expandedAltura,
                    onExpandedChange = { expandedAltura = it },
                    onItemSelected = {
                        altura = it
                        viewModel.altura = it.replace(" cm", "").toFloat()
                        expandedAltura = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                if (altura.isBlank()) {
                    Text("Campo obligatorio", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
                }

                // PESO
                DropdownSelector(
                    label = "Peso",
                    selected = if (peso.isBlank()) "Selecciona tu peso" else peso,
                    options = pesos,
                    expanded = expandedPeso,
                    onExpandedChange = { expandedPeso = it },
                    onItemSelected = {
                        peso = it
                        viewModel.peso = it.replace(" kg", "").toFloat()
                        expandedPeso = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                if (peso.isBlank()) {
                    Text("Campo obligatorio", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
                }

                Spacer(modifier = Modifier.height(60.dp))
            }
        }

        // 🔹 Botón continuar con animación
        Button(
            onClick = {
                if (camposValidos) onContinuarClick()
                else mostrarAdvertenciaEdad = true
            },
            enabled = camposValidos,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .alpha(alpha),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Continuar", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroVent2ScreenPreview(viewModel: UsuarioViewModel = viewModel()) {
    RegistroVent2ScreenContent(viewModel = viewModel)
}