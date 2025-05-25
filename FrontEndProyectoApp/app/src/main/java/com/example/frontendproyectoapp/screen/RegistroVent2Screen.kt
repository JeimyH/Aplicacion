package com.example.frontendproyectoapp.screen

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    val alturas = (140..220).map { "$it cm" }
    val pesos = (40..200).map { "$it kg" }

    var sexo by remember { mutableStateOf(viewModel.sexo.ifEmpty { "Sexo" })}
    var altura by remember { mutableStateOf(if (viewModel.altura > 0) "${viewModel.altura.toInt()} cm" else "Altura") }
    var peso by remember { mutableStateOf(if (viewModel.peso > 0) "${viewModel.peso.toInt()} kg" else "Peso")  }

    var expandedSexo by remember { mutableStateOf(false) }
    var expandedAltura by remember { mutableStateOf(false)}
    var expandedPeso by remember { mutableStateOf(false) }

    val context = LocalContext.current
    var fechaNacimiento by remember {  mutableStateOf(viewModel.fechaNacimiento.ifEmpty { "Selecciona tu fecha de nacimiento" }) }

    // DatePickerDialog
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            fechaNacimiento = format.format(selectedDate.time)
            viewModel.fechaNacimiento = fechaNacimiento
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Atrás",
            modifier = Modifier
                .align(Alignment.Start)
                .clickable { onBackClick() }
        )

        Text("Cuéntanos sobre ti")

        DropdownSelector("Sexo", sexo, sexos, expandedSexo, { expandedSexo = it }) {
            sexo = it
            viewModel.sexo = it
            expandedSexo = false
        }

        OutlinedTextField(
            value = fechaNacimiento,
            onValueChange = {},
            readOnly = true,
            label = { Text("Fecha de Nacimiento") },
            trailingIcon = {
                IconButton(onClick = { datePickerDialog.show() }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        DropdownSelector("Altura", altura, alturas, expandedAltura, { expandedAltura = it }) {
            altura = it
            viewModel.altura = it.replace(" cm", "").toFloat()
            expandedAltura = false
        }

        DropdownSelector("Peso", peso, pesos, expandedPeso, { expandedPeso = it }) {
            peso = it
            viewModel.peso = it.replace(" kg", "").toFloat()
            expandedPeso = false
        }

        Button(
            onClick = {
                onContinuarClick()
            }
        ) {
            Text("Continuar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroVent2ScreenPreview(viewModel: UsuarioViewModel = viewModel()) {
    RegistroVent2ScreenContent(viewModel = viewModel)
}