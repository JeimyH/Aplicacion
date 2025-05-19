package com.example.frontendproyectoapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RegistroVent4Screen(navController: NavController) {
    RegistroVent4ScreenContent(
        onBackClick = { navController.popBackStack() },
        onClick = { navController.navigate("registro5") }
    )
}

@Composable
fun RegistroVent4ScreenContent(
    onClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onDietaSeleccionada: (String) -> Unit = {}
) {
    val opcionesDieta = listOf(
        "Recomendada",
        "Alta en proteínas",
        "Baja en carbohidratos",
        "Keto",
        "Baja en grasas"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Flecha de regreso
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Atrás"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¿Qué tipo de dieta prefieres?",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )

        opcionesDieta.forEach { tipoDieta ->
            OutlinedButton(
                onClick = { onDietaSeleccionada(tipoDieta) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = tipoDieta)
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
        Button(
            onClick = onClick
        ) {
            Text("Continuar")
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RegistroVent4ScreenPreview() {
    MaterialTheme {
        RegistroVent4ScreenContent()
    }
}