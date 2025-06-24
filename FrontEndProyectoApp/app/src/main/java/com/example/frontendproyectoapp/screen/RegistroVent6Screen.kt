package com.example.frontendproyectoapp.screen

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

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
    val altura = alturaCm / 100
    val sexo = viewModel.sexo
    val edad = calcularEdadReg5(viewModel.fechaNacimiento)

    // Cálculo de TMB y calorías
    val tmb = if (sexo == "Masculino") {
        10 * peso + 6.25 * alturaCm - 5 * edad + 5
    } else {
        10 * peso + 6.25 * alturaCm - 5 * edad - 161
    }

    val caloriasMin = (tmb * 1.2).toInt()
    val caloriasMax = (tmb * 1.55).toInt()
    val caloriasProm = (caloriasMin + caloriasMax) / 2

    val proteinas = (caloriasProm * 0.2 / 4).toInt()
    val carbohidratos = (caloriasProm * 0.5 / 4).toInt()
    val grasas = (caloriasProm * 0.3 / 9).toInt()
    val grasasSaturadas = (caloriasProm * 0.1 / 9).toInt()

    val nutrientes = listOf(
        "Proteínas" to "${proteinas}g",
        "Carbohidratos" to "${carbohidratos}g",
        "Grasas" to "${grasas}g",
        "Azúcares" to "25g",
        "Fibra" to "30g",
        "Sodio" to "2300mg",
        "Grasas Saturadas" to "${grasasSaturadas}g"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // Botón atrás
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Atrás"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 56.dp, bottom = 80.dp), // espacio para botón y flecha atrás
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            // Gráfico de calorías
            CaloriasGraph(caloriasMin = caloriasMin, caloriasMax = caloriasMax)

            Spacer(modifier = Modifier.height(32.dp))

            // Título de sección
            Text(
                text = "Distribución de macronutrientes",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Lista de nutrientes
            nutrientes.forEach { (nombre, valor) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = nombre, fontSize = 16.sp)
                    Text(text = valor, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        // Botón continuar
        Button(
            onClick = onClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp)
        ) {
            Text("Continuar")
        }
    }
}


fun calcularEdadReg5(fechaNacimiento: String): Int {
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val fecha = LocalDate.parse(fechaNacimiento, formatter)
        val hoy = LocalDate.now()
        Period.between(fecha, hoy).years
    } catch (e: Exception) {
        25 // edad por defecto en caso de error
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistroVent6ScreenPreview(viewModel: UsuarioViewModel = viewModel()) {
    RegistroVent6ScreenContent(viewModel = viewModel)
}
