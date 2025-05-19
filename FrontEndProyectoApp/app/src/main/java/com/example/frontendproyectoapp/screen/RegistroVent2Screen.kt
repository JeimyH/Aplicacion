package com.example.frontendproyectoapp.screen

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.*

@Composable
fun RegistroVent2Screen(navController: NavController) {
    RegistroVent2ScreenContent(
        onBackClick = { navController.popBackStack() },
        onContinuarClick = { navController.navigate("registro3") }
    )
}

@Composable
fun RegistroVent2ScreenContent(
    onBackClick: () -> Unit = {},
    onContinuarClick: () -> Unit = {}
) {
    val sexos = listOf("Masculino", "Femenino", "Otro")
    val edades = (18..100).map { it.toString() }
    val alturas = (140..220).map { "$it cm" }
    val pesos = (40..200).map { "$it kg" }

    var sexo by remember { mutableStateOf("Sexo") }
    var edad by remember { mutableStateOf("Edad") }
    var altura by remember { mutableStateOf("Altura") }
    var peso by remember { mutableStateOf("Peso") }

    var expandedSexo by remember { mutableStateOf(false) }
    var expandedEdad by remember { mutableStateOf(false) }
    var expandedAltura by remember { mutableStateOf(false) }
    var expandedPeso by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var fechaNacimiento by remember { mutableStateOf("Selecciona tu fecha de nacimiento") }

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            fechaNacimiento = "$dayOfMonth/${month + 1}/$year"
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

        Spacer(modifier = Modifier.height(24.dp))

        Text("Cuéntanos sobre ti", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Esta información nos ayudará a conocerte\ny personalizar tu rutina")

        Spacer(modifier = Modifier.height(24.dp))

        DropdownSelector("Sexo", sexo, sexos, expandedSexo, { expandedSexo = it }) {
            sexo = it
            expandedSexo = false
        }

        Spacer(modifier = Modifier.height(16.dp))

        DropdownSelector("Edad", edad, edades, expandedEdad, { expandedEdad = it }) {
            edad = it
            expandedEdad = false
        }
        Spacer(modifier = Modifier.height(16.dp))

        // 📅 Selector de fecha
        OutlinedTextField(
            value = fechaNacimiento,
            onValueChange = {},
            readOnly = true,
            label = { Text("Fecha de nacimiento") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { datePickerDialog.show() }
        )
        Spacer(modifier = Modifier.height(16.dp))

        DropdownSelector("Altura", altura, alturas, expandedAltura, { expandedAltura = it }) {
            altura = it
            expandedAltura = false
        }

        Spacer(modifier = Modifier.height(16.dp))

        DropdownSelector("Peso", peso, pesos, expandedPeso, { expandedPeso = it }) {
            peso = it
            expandedPeso = false
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onContinuarClick) {
            Text("Continuar")
        }
    }
}

@Composable
fun DropdownSelector(
    label: String,
    selected: String,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onItemSelected: (String) -> Unit
) {
    Box {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.clickable { onExpandedChange(!expanded) }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = { onItemSelected(option) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroVent2ScreenPreview() {
    RegistroVent2ScreenContent()
}