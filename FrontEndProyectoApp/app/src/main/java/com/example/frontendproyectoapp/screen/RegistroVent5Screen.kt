package com.example.frontendproyectoapp.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun RegistroVent5Screen(navController: NavController) {
    RegistroVent5ScreenContent(
        onBackClick = { navController.popBackStack() },
        onClick = { navController.navigate("registro6") }
    )
}

@Composable
fun RegistroVent5ScreenContent(
    onClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    caloriasMin: Int = 1800,
    caloriasMax: Int = 2200
) {
    val nutrientes = listOf(
        "Proteínas" to "150g",
        "Carbohidratos" to "250g",
        "Grasas" to "70g",
        "Azúcares" to "25g",
        "Fibra" to "30g",
        "Sodio" to "2300mg",
        "Grasas Saturadas" to "20g"
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

        // Título
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Muy bien, ")
                }
                append("hemos calculado las calorías que necesitas al día")
            },
            fontSize = 18.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        // Gráfico de calorías
        CaloriasGraph(caloriasMin = caloriasMin, caloriasMax = caloriasMax)

        Spacer(modifier = Modifier.height(24.dp))

        // Nutrientes y valores recomendados
        nutrientes.forEach { (nombre, valor) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = nombre, fontSize = 16.sp)
                Text(text = valor, fontSize = 16.sp, fontWeight = FontWeight.Medium)
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

@Composable
fun CaloriasGraph(caloriasMin: Int, caloriasMax: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)) {
            val width = size.width
            val height = size.height

            val start = Offset(x = width * 0.2f, y = height * 0.8f)
            val end = Offset(x = width * 0.8f, y = height * 0.8f)
            val peak = Offset(x = width * 0.5f, y = height * 0.2f)

            val path = Path().apply {
                moveTo(start.x, start.y)
                quadraticBezierTo(peak.x, peak.y, end.x, end.y)
            }

            drawPath(
                path = path,
                color = Color.Black,
                style = Stroke(width = 4f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // Línea vertical en inicio (mínimo)
            drawLine(
                color = Color.Black,
                start = Offset(start.x, start.y),
                end = Offset(start.x, start.y - 20),
                strokeWidth = 4f
            )

            // Línea vertical en final (máximo)
            drawLine(
                color = Color.Black,
                start = Offset(end.x, end.y),
                end = Offset(end.x, end.y - 20),
                strokeWidth = 4f
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Mínimo", fontWeight = FontWeight.Medium)
                Text("$caloriasMin kcal", fontSize = 14.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Máximo", fontWeight = FontWeight.Medium)
                Text("$caloriasMax kcal", fontSize = 14.sp)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistroVent5ScreenPreview() {
    MaterialTheme {
        RegistroVent5ScreenContent()
    }
}