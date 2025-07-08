package com.example.frontendproyectoapp.screen

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.frontendproyectoapp.viewModel.UsuarioViewModel
import kotlinx.coroutines.delay

@Composable
fun RegistroVent6Screen(navController: NavController, viewModel: UsuarioViewModel) {
    RegistroVent6ScreenContent(
        viewModel = viewModel,
        onBackClick = { navController.popBackStack() },
        onClick = { navController.navigate("registro7") }
    )
}

@Composable
fun RegistroVent6ScreenContent(
    viewModel: UsuarioViewModel,
    onClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val peso = viewModel.peso
    val alturaCm = viewModel.altura
    val sexo = viewModel.sexo
    val edad = viewModel.calcularEdadReg6(viewModel.fechaNacimiento)

    val tmb = if (sexo == "Masculino") {
        10 * peso + 6.25 * alturaCm - 5 * edad + 5
    } else {
        10 * peso + 6.25 * alturaCm - 5 * edad - 161
    }

    val caloriasMin = (tmb * 1.2).toInt()
    val caloriasMax = (tmb * 1.55).toInt()
    val caloriasProm = (caloriasMin + caloriasMax) / 2

    val valoresFinales = listOf(
        "Proteínas" to (caloriasProm * 0.2 / 4).toInt(),
        "Carbohidratos" to (caloriasProm * 0.5 / 4).toInt(),
        "Grasas" to (caloriasProm * 0.3 / 9).toInt(),
        "Azúcares" to 25,
        "Fibra" to 30,
        "Sodio" to 23,
        "Grasas Saturadas" to (caloriasProm * 0.1 / 9).toInt()
    )

    // Estado animado por cada nutriente
    val valoresAnimados = remember {
        mutableStateListOf<Int>().apply {
            repeat(valoresFinales.size) { add(0) }
        }
    }

    // Lanzamos la animación una sola vez
    LaunchedEffect(Unit) {
        valoresFinales.forEachIndexed { index, (_, targetValue) ->
            for (i in 0..targetValue) {
                valoresAnimados[index] = i
                delay(5L) // Puedes ajustar la velocidad aquí
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {

        Box(modifier = Modifier.weight(1f)) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Atrás",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 14.dp)
                    .clickable { onBackClick() }
            )

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .verticalScroll(rememberScrollState())
                    .padding(top = 56.dp, bottom = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Muy bien, ")
                        }
                        append("hemos calculado tus necesidades diarias.")
                    },
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )

                CaloriasGraph(caloriasMin = caloriasMin, caloriasMax = caloriasMax)

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Distribución de macronutrientes",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )

                valoresFinales.forEachIndexed { index, (nombre, _) ->
                    val valor = valoresAnimados.getOrNull(index) ?: 0

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = nombre,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = when (nombre) {
                                "Sodio" -> "$valor mg"
                                else -> "$valor g"
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }

        Button(
            onClick = onClick,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Continuar", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistroVent6ScreenPreview(viewModel: UsuarioViewModel = viewModel()) {
    RegistroVent6ScreenContent(viewModel = viewModel)
}
