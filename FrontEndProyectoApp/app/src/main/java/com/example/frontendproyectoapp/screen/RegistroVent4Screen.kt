package com.example.frontendproyectoapp.screen

import android.app.Application
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendproyectoapp.ui.theme.FrontEndProyectoAppTheme
import com.example.frontendproyectoapp.viewModel.UsuarioViewModel

@Composable
fun RegistroVent4Screen(navController: NavController, viewModel: UsuarioViewModel) {
    RegistroVent4ScreenContent(
        viewModel = viewModel,
        onBackClick = { navController.popBackStack() },
        onClick = { navController.navigate("registro5") }
    )
}

@Composable
fun RegistroVent4ScreenContent(
    viewModel: UsuarioViewModel,
    onClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val (pesoMin, pesoMax) = viewModel.calcularRangoPesoNormal(viewModel.altura)
    val pesoMinFloat = pesoMin.toFloat()
    val pesoMaxFloat = pesoMax.toFloat()

    var pesoObjetivo by remember { mutableStateOf(pesoMin.toInt().toString()) }
    val pesoFloat = pesoObjetivo.toFloatOrNull()
    val esPesoValido = pesoFloat != null && pesoFloat in pesoMinFloat..pesoMaxFloat

    val animatedPesoObjetivo by animateFloatAsState(
        targetValue = pesoFloat ?: pesoMinFloat,
        label = "pesoObjetivoAnim"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        // Barra de progreso superior (paso 4 de 6)
        LinearProgressIndicator(
            progress = 3 / 6f,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Box(modifier = Modifier.weight(1f)) {
            // Icono de retroceso
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Atrás",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 8.dp)
                    .clickable { onBackClick() }
            )

            // Contenido scrollable centrado
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Establece tu peso objetivo",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Según tu altura, tu peso saludable está entre ${pesoMin.toInt()} kg y ${pesoMax.toInt()} kg",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "${animatedPesoObjetivo.toInt()} kg",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Slider(
                    value = pesoFloat ?: pesoMinFloat,
                    onValueChange = {
                        pesoObjetivo = it.toInt().toString()
                    },
                    valueRange = pesoMinFloat..pesoMaxFloat,
                    steps = (pesoMaxFloat - pesoMinFloat).toInt() - 1,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )

                if (!esPesoValido) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Selecciona un peso dentro del rango válido",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(60.dp))
            }
        }

        // Botón inferior
        Button(
            onClick = {
                viewModel.pesoObjetivo = pesoFloat ?: 0f
                onClick()
            },
            enabled = esPesoValido,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Continuar", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroVent4ScreenPreview() {
    FrontEndProyectoAppTheme {
        val context = LocalContext.current.applicationContext as Application
        val viewModel: UsuarioViewModel = viewModel(
            factory = ViewModelProvider.AndroidViewModelFactory(context)
        )
        RegistroVent4ScreenContent(viewModel = viewModel)
    }
}

