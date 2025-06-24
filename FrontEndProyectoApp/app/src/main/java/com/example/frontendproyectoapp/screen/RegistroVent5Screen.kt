package com.example.frontendproyectoapp.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.frontendproyectoapp.viewModel.UsuarioViewModel

@Composable
fun RegistroVent5Screen(navController: NavController, viewModel: UsuarioViewModel) {
    RegistroVent5ScreenContent(
        viewModel = viewModel,
        onBackClick = { navController.popBackStack() },
        onClick = { navController.navigate("registro6") }
    )
}

@Composable
fun RegistroVent5ScreenContent(
    viewModel: UsuarioViewModel,
    onClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    // Lista de pares (nombre dieta, URL imagen)
    val opcionesDieta = listOf(
        "Recomendada" to "https://drive.google.com/uc?export=view&id=1SpBBOsMFRYO6Nv02Fh8_xcmK3gpch219",
        "Alta en proteínas" to "https://drive.google.com/uc?export=view&id=1d4vTRyPtQjdmcPmwOMriI_MFUWgph7NV",
        "Baja en carbohidratos" to "https://drive.google.com/uc?export=view&id=1nibgXzxPKAWiSf1LDbAmJxt2V46YediJ",
        "Keto" to "https://drive.google.com/uc?export=view&id=1u7rbo49gyW41bm9X7LDkhbEKME-krj0t",
        "Baja en grasas" to "https://drive.google.com/uc?export=view&id=1LsHLUQyz7mwmgV7CeZI-Px_MQlGNPZlU"
    )

    var seleccionDieta by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Atrás")
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¿Qué tipo de dieta prefieres?",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            opcionesDieta.forEach { (tipoDieta, imagenUrl) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            seleccionDieta = tipoDieta
                            viewModel.restriccionesDieta = tipoDieta
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (tipoDieta == seleccionDieta) Color(0xFFB3E5FC) else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        AsyncImage(
                            model = imagenUrl,
                            contentDescription = tipoDieta,
                            modifier = Modifier.size(90.dp)
                                .clip(RoundedCornerShape(20.dp)), // Bordes redondeados
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = tipoDieta,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

        Button(
            onClick = onClick,
            enabled = seleccionDieta.isNotEmpty(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
                .fillMaxWidth(0.5f)
        ) {
            Text("Continuar")
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RegistroVent5ScreenPreview(viewModel: UsuarioViewModel = viewModel()) {
    MaterialTheme {
        RegistroVent5ScreenContent(viewModel = viewModel)
    }
}