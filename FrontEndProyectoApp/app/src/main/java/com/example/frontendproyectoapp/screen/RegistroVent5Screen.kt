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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.frontendproyectoapp.model.TarjetaData
import com.example.frontendproyectoapp.ui.theme.PastelMint
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
    val opcionesDieta = listOf(
        TarjetaData(
            titulo = "Recomendada",
            descripcion = "Dieta balanceada con proporciones adecuadas de carbohidratos, proteínas y grasas, ideal para mantener una buena salud general.",
            imagenUrl = "https://drive.google.com/uc?export=view&id=1SpBBOsMFRYO6Nv02Fh8_xcmK3gpch219"
        ),
        TarjetaData(
            titulo = "Alta en proteínas",
            descripcion = "Favorece el consumo de proteínas para ganar músculo, mejorar la saciedad y apoyar la recuperación física.",
            imagenUrl = "https://drive.google.com/uc?export=view&id=1d4vTRyPtQjdmcPmwOMriI_MFUWgph7NV"
        ),
        TarjetaData(
            titulo = "Baja en carbohidratos",
            descripcion = "Reduce alimentos ricos en carbohidratos para controlar peso o niveles de glucosa, priorizando proteínas y grasas.",
            imagenUrl = "https://drive.google.com/uc?export=view&id=1nibgXzxPKAWiSf1LDbAmJxt2V46YediJ"
        ),
        TarjetaData(
            titulo = "Keto",
            descripcion = "Muy baja en carbohidratos y alta en grasas, induce cetosis para usar grasa como principal fuente de energía.",
            imagenUrl = "https://drive.google.com/uc?export=view&id=1u7rbo49gyW41bm9X7LDkhbEKME-krj0t"
        ),
        TarjetaData(
            titulo = "Baja en grasas",
            descripcion = "Limita grasas, especialmente saturadas, para apoyar la salud cardiovascular y digestiva.",
            imagenUrl = "https://drive.google.com/uc?export=view&id=1LsHLUQyz7mwmgV7CeZI-Px_MQlGNPZlU"
        )
    )

    var seleccionDieta by remember { mutableStateOf(viewModel.restriccionesDieta.ifEmpty { "" }) }

    val camposValidos = seleccionDieta.isNotBlank()
    val alpha by animateFloatAsState(targetValue = if (camposValidos) 1f else 0.4f, label = "alphaBtn")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {

        Spacer(modifier = Modifier.height(10.dp))

        // Barra de progreso paso 4/6
        LinearProgressIndicator(
            progress = 4 / 7f,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(4.dp)),
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
                    text = "¿Qué tipo de dieta prefieres?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                opcionesDieta.forEach { (titulo, descripcion, imagenUrl) ->
                    val isSelected = titulo == seleccionDieta

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                seleccionDieta = titulo
                                viewModel.restriccionesDieta = titulo
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
                            AsyncImage(
                                model = imagenUrl,
                                contentDescription = titulo,
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(20.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = titulo,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = descripcion,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
            Text("Continuar", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RegistroVent5ScreenPreview(viewModel: UsuarioViewModel = viewModel()) {
    MaterialTheme {
        RegistroVent5ScreenContent(viewModel = viewModel)
    }
}