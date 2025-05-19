package com.example.frontendproyectoapp.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendproyectoapp.viewModel.RegistroViewModel

@Composable
fun RegistroVent3Screen(navController: NavController, viewModel: RegistroViewModel) {
    RegistroVent3ScreenContent(
        viewModel = viewModel,
        onBackClick = { navController.popBackStack() },
        onClick = { navController.navigate("registro4") }
    )
}

@Composable
fun RegistroVent3ScreenContent(
    viewModel: RegistroViewModel,
    onClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var pesoObjetivo by remember { mutableStateOf(viewModel.peso.toInt()) }
    val (pesoMin, pesoMax) = calcularRangoPesoNormal(viewModel.altura)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Atrás")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Establece tu peso objetivo",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Según tu altura, tu peso saludable está entre $pesoMin kg y $pesoMax kg",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = pesoObjetivo.toString(),
            onValueChange = {
                pesoObjetivo = it.toIntOrNull() ?: pesoObjetivo
            },
            label = { Text("Peso objetivo (kg)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                viewModel.pesoObjetivo = pesoObjetivo.toFloat()
                onClick()
            },
            enabled = pesoObjetivo in 30..300
        ) {
            Text("Continuar")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegistroVent3ScreenPreview(viewModel: RegistroViewModel = viewModel()) {
    RegistroVent3ScreenContent(viewModel = viewModel)
}

fun calcularRangoPesoNormal(alturaCm: Float): Pair<Int, Int> {
    val alturaM = alturaCm / 100f
    val pesoMin = 18.5 * (alturaM * alturaM)
    val pesoMax = 24.9 * (alturaM * alturaM)
    return Pair(pesoMin.toInt(), pesoMax.toInt())
}