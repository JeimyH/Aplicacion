package com.example.frontendproyectoapp.screen

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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

    var sexo by remember { mutableStateOf(viewModel.sexo.ifEmpty { "Genero" }) }
    var altura by remember { mutableStateOf(if (viewModel.altura > 0) "${viewModel.altura.toInt()} cm" else "Altura") }
    var peso by remember { mutableStateOf(if (viewModel.peso > 0) "${viewModel.peso.toInt()} kg" else "Peso") }

    var expandedSexo by remember { mutableStateOf(false) }
    var expandedAltura by remember { mutableStateOf(false) }
    var expandedPeso by remember { mutableStateOf(false) }

    var edadUsuario by remember { mutableStateOf(0) }
    var mostrarAdvertenciaEdad by remember { mutableStateOf(false) }


    val context = LocalContext.current
    var fechaNacimiento by remember {
        mutableStateOf(viewModel.fechaNacimiento.ifEmpty { "Selecciona tu fecha de nacimiento" })
    }

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            fechaNacimiento = format.format(selectedDate.time)
            viewModel.fechaNacimiento = fechaNacimiento

            edadUsuario = calcularEdadReg2(fechaNacimiento)
            mostrarAdvertenciaEdad = edadUsuario < 18
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Botón atrás en la parte superior izquierda
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Atrás",
            modifier = Modifier
                .align(Alignment.TopStart)
                .clickable { onBackClick() }
        )

        // Contenido centrado y desplazable
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Cuéntanos sobre ti",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Menú desplegable de género
            DropdownSelector(
                label = "Género",
                selected = sexo,
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

            // Campo de fecha de nacimiento
            OutlinedTextField(
                value = fechaNacimiento,
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha de nacimiento") },
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            if (mostrarAdvertenciaEdad) {
                Text(
                    text = "Debes tener al menos 18 años para registrarte",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                )
            }

            // Altura
            DropdownSelector(
                label = "Altura",
                selected = altura,
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

            // Peso
            DropdownSelector(
                label = "Peso",
                selected = peso,
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

            Spacer(modifier = Modifier.height(60.dp)) // Espacio para el botón inferior
        }

        // Botón continuar
        Button(
            onClick = {
                if (edadUsuario >= 18) {
                    onContinuarClick()
                } else {
                    mostrarAdvertenciaEdad = true
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            Text("Continuar")
        }

    }
}

fun calcularEdadReg2(fechaNacimiento: String): Int {
    return try {
        val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fecha = formato.parse(fechaNacimiento)
        val dob = Calendar.getInstance().apply { time = fecha!! }
        val hoy = Calendar.getInstance()

        var edad = hoy.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        if (hoy.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            edad--
        }
        edad
    } catch (e: Exception) {
        0
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroVent2ScreenPreview(viewModel: UsuarioViewModel = viewModel()) {
    RegistroVent2ScreenContent(viewModel = viewModel)
}