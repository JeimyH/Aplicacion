package com.example.frontendproyectoapp.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun RegistroVent1Screen(navController: NavController) {
    RegistroVent1ScreenContent(
        onClick = { navController.navigate("registro2") },
        onLoginClick = { /* navController.navigate("login") */ }
    )
}

@Composable
fun RegistroVent1ScreenContent(
    onClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Aquí podrías añadir imagen o más contenido
        Row {
            Text("PERSONALIZA Y DISEÑA TU RUTINA CON DIETASMART")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClick
        ) {
            Text("Comenzar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text("¿Ya tienes una cuenta?")
            Text(
                text = " Inicia Sesión",
                color = Color.Blue,
                modifier = Modifier.clickable { onLoginClick() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroVent1ScreenPreview() {
    RegistroVent1ScreenContent()
}
