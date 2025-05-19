package com.example.frontendproyectoapp.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview



@Composable
fun RegistroVent3Screen(navController: NavController) {
    RegistroVent3ScreenContent(
        onBackClick = { navController.popBackStack() },
        onClick = { navController.navigate("registro4")
        }
    )
}

@Composable
fun RegistroVent3ScreenContent(
    onClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onContinuarClick: () -> Unit = {}
) {
    var unidad by remember { mutableStateOf("Kg / lbs") }
    var pesoObjetivo by remember { mutableStateOf(43f) }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .align(Alignment.Start)
                .clickable { onBackClick() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Peso Objetivo", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown de unidad
        Box {
            OutlinedTextField(
                value = unidad,
                onValueChange = {},
                readOnly = true,
                label = { Text("Unidad") },
                trailingIcon = {
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.clickable { expanded = !expanded }
                    )
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(text = { Text("Kg") }, onClick = {
                    unidad = "Kg"
                    expanded = false
                })
                DropdownMenuItem(text = { Text("lbs") }, onClick = {
                    unidad = "lbs"
                    expanded = false
                })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Según la escala de IMC, tu rango de peso normal está entre ( ) - ( ) kg")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = pesoObjetivo.toInt().toString(),
            onValueChange = {
                pesoObjetivo = it.toFloatOrNull() ?: pesoObjetivo
            },
            label = { Text("Peso") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onClick) {
            Text("Continuar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroVent3ScreenPreview() {
    RegistroVent3ScreenContent()
}