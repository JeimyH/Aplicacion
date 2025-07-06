package com.example.frontendproyectoapp.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.example.frontendproyectoapp.ui.theme.PastelMint
import com.example.frontendproyectoapp.viewModel.UsuarioViewModel

@Composable
fun RegistroVent3Screen(navController: NavController, viewModel: UsuarioViewModel) {
    RegistroVent3ScreenContent(
        viewModel = viewModel,
        onBackClick = { navController.popBackStack() },
        onClick = { navController.navigate("registro4") }
    )
}

@Composable
fun RegistroVent3ScreenContent(
    viewModel: UsuarioViewModel,
    onClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val opcionesObjetivoSalud = listOf(
        "Perdida de Peso" to "https://drive.google.com/uc?export=view&id=1BS-SBL_ilrVoGXPEZi8MN7lDZeQ5K1mW",
        "Mantener Peso" to "https://drive.google.com/uc?export=view&id=1QWORYcrRq4gTAcd1YDtBQU6fdF69myrF",
        "Ganancia de Masa Muscular" to "https://drive.google.com/uc?export=view&id=1fyQ1sUpcLXodP7hPzKVtM0lkE2vglDmy",
        "Desarrollo de hábitos alimenticios saludables" to "https://drive.google.com/uc?export=view&id=1nNjb9gbDVxrLsRttq7iYeT2ETq-hKz9X"
    )

    var seleccionObjetivoSalud by remember { mutableStateOf(viewModel.objetivosSalud.ifEmpty { "" }) }

    val camposValidos = seleccionObjetivoSalud.isNotBlank()
    val alpha by animateFloatAsState(targetValue = if (camposValidos) 1f else 0.4f, label = "alpha")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        // Barra de progreso paso 2/6
        LinearProgressIndicator(
            progress = 2 / 6f,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Box(modifier = Modifier.weight(1f)) {
            // Botón volver
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Atrás",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 8.dp)
                    .clickable { onBackClick() }
            )

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "¿Cuál es el objetivo que quieres alcanzar?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                opcionesObjetivoSalud.forEach { (tipoObjetivo, imagenUrl) ->
                    val isSelected = tipoObjetivo == seleccionObjetivoSalud

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                seleccionObjetivoSalud = tipoObjetivo
                                viewModel.objetivosSalud = tipoObjetivo
                            },
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 2.dp),
                        border = if (isSelected) BorderStroke(2.dp, PastelMint) else null,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .background(if (isSelected) PastelMint.copy(alpha = 0.25f) else Color.Transparent)
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                AsyncImage(
                                    model = imagenUrl,
                                    contentDescription = tipoObjetivo,
                                    modifier = Modifier
                                        .size(90.dp)
                                        .clip(RoundedCornerShape(20.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                Text(
                                    text = tipoObjetivo,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Seleccionado",
                                    tint = PastelMint,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))
            }
        }

        // Botón continuar
        Button(
            onClick = { if (camposValidos) onClick() },
            enabled = camposValidos,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .alpha(alpha),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PastelMint,
                contentColor = Color.White,
                disabledContainerColor = PastelMint.copy(alpha = 0.3f),
                disabledContentColor = Color.White.copy(alpha = 0.6f)
            )
        ) {
            Text("Continuar", style = MaterialTheme.typography.labelLarge)
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RegistroVent3ScreenPreview(viewModel: UsuarioViewModel = viewModel()) {
    MaterialTheme {
        RegistroVent4ScreenContent(viewModel = viewModel)
    }
}