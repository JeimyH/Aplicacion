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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.frontendproyectoapp.viewModel.RegistroViewModel

@Composable
fun RegistroVent4Screen(navController: NavController, viewModel: RegistroViewModel) {
    RegistroVent4ScreenContent(
        viewModel = viewModel,
        onBackClick = { navController.popBackStack() },
        onClick = { navController.navigate("registro5") }
    )
}

@Composable
fun RegistroVent4ScreenContent(
    viewModel: RegistroViewModel,
    onClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val opcionesDieta = listOf(
        "Recomendada",
        "Alta en proteínas",
        "Baja en carbohidratos",
        "Keto",
        "Baja en grasas"
    )

    var seleccionDieta by remember { mutableStateOf("") }

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
            text = "¿Qué tipo de dieta prefieres?",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        opcionesDieta.forEach { tipoDieta ->
            OutlinedButton(
                onClick = {
                    seleccionDieta = tipoDieta
                    viewModel.restriccionesDieta = tipoDieta
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (tipoDieta == seleccionDieta) Color(0xFFB3E5FC) else Color.Transparent
                )
            ) {
                Text(text = tipoDieta)
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onClick,
            enabled = seleccionDieta.isNotEmpty()
        ) {
            Text("Continuar")
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RegistroVent4ScreenPreview(viewModel: RegistroViewModel = viewModel()) {
    MaterialTheme {
        RegistroVent4ScreenContent(viewModel = viewModel)
    }
}