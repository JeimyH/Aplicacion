package com.example.frontendproyectoapp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RegistroVent6Screen(navController: NavController) {
    RegistroVent6ScreenContent(
        onBackClick = { navController.popBackStack() },
        onClick = { navController.navigate("registro7") }
    )
}

@Composable
fun RegistroVent6ScreenContent(
    onBackClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
        }

        Spacer(modifier = Modifier.height(8.dp))
        // Aquí podrías añadir imagen o más contenido
        Row {
            Text("Continuemos, ahora personalizaremos tu rutina de comidas para alcanzar tus calorías diarias")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClick
        ) {
            Text("Continuar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroVent6ScreenPreview() {
    RegistroVent6ScreenContent()
}
