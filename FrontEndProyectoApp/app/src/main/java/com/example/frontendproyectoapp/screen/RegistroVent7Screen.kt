package com.example.frontendproyectoapp.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.frontendproyectoapp.model.NutrientesRecomendados
import com.example.frontendproyectoapp.viewModel.UsuarioViewModel
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun RegistroVent7Screen(navController: NavController, viewModel: UsuarioViewModel) {
    RegistroVent7ScreenContent(
        viewModel = viewModel,
        onBackClick = { navController.popBackStack() },
        onClick = { navController.navigate("registro8") }
    )
}

@Composable
fun RegistroVent7ScreenContent(
    viewModel: UsuarioViewModel,
    onClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val edad = viewModel.calcularEdadReg7(viewModel.fechaNacimiento)

    val recomendaciones = calcularRecomendacionesLocales(
        peso = viewModel.peso,
        altura = viewModel.altura,
        edad = edad,
        sexo = viewModel.sexo,
        objetivo = viewModel.objetivosSalud,
        restriccion = viewModel.restriccionesDieta,
        nivelActividad = viewModel.nivelActividad
    )

    val caloriasMin = (recomendaciones.calorias * 0.9).toInt()
    val caloriasMax = (recomendaciones.calorias * 1.1).toInt()

    val valoresFinales = listOf(
        "Proteínas" to recomendaciones.proteinas.toInt(),
        "Carbohidratos" to recomendaciones.carbohidratos.toInt(),
        "Grasas" to recomendaciones.grasas.toInt(),
        "Azúcares" to recomendaciones.azucares.toInt(),
        "Fibra" to recomendaciones.fibra.toInt(),
        "Sodio" to recomendaciones.sodio.toInt(),
        "Grasas Saturadas" to recomendaciones.grasasSaturadas.toInt()
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

        Spacer(modifier = Modifier.height(16.dp))

        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Atrás",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 8.dp)
                .clickable { onBackClick() }
        )

        Box(modifier = Modifier.weight(1f)) {

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .verticalScroll(rememberScrollState())
                    .padding(top = 56.dp, bottom = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(
                            fontWeight = FontWeight.Bold)
                        ) {
                            append("Muy bien, ")
                        }
                        append("hemos calculado tus necesidades diarias.")
                    },
                    style = MaterialTheme.typography.titleLarge,
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
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
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
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = when (nombre) {
                                "Sodio" -> "$valor mg"
                                else -> "$valor g"
                            },
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
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
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Continuar", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

fun calcularRecomendacionesLocales(
    peso: Float,
    altura: Float,
    edad: Int,
    sexo: String,
    objetivo: String,
    restriccion: String,
    nivelActividad: String
): NutrientesRecomendados {

    // Fórmula Mifflin-St Jeor
    val tmb = if (sexo.lowercase() == "masculino") {
        (10 * peso) + (6.25 * altura) - (5 * edad) + 5
    } else {
        (10 * peso) + (6.25 * altura) - (5 * edad) - 161
    }

    // Factor de actividad
    val factorActividad = when (nivelActividad.lowercase()) {
        "sedentario" -> 1.2
        "ligera actividad" -> 1.375
        "actividad moderada" -> 1.55
        "alta actividad" -> 1.725
        "actividad extrema" -> 1.9
        else -> 1.2
    }

    val gct = tmb * factorActividad

    // Ajuste por objetivo
    var caloriasObjetivo = gct
    when (objetivo.lowercase()) {
        "pérdida de peso" -> caloriasObjetivo -= 500
        "ganancia de masa muscular" -> caloriasObjetivo += 500
    }

    val calorias = caloriasObjetivo.toFloat()

    // Distribución inicial
    var porcProte = 0.20f
    var porcCarb = 0.50f
    var porcGrasa = 0.30f

    // Ajustes por restricción
    when (restriccion.lowercase()) {
        "alta en proteínas" -> { porcProte = 0.30f; porcCarb = 0.40f; porcGrasa = 0.30f }
        "baja en carbohidratos" -> { porcProte = 0.30f; porcCarb = 0.20f; porcGrasa = 0.50f }
        "keto" -> { porcProte = 0.25f; porcCarb = 0.10f; porcGrasa = 0.65f }
        "baja en grasas" -> { porcProte = 0.30f; porcCarb = 0.60f; porcGrasa = 0.10f }
    }

    val proteinas = (calorias * porcProte) / 4f
    val carbohidratos = (calorias * porcCarb) / 4f
    val grasas = (calorias * porcGrasa) / 9f
    val azucares = (calorias * 0.10f) / 4f
    val fibra = (calorias / 1000f) * 14f
    val sodio = 1500f
    val grasasSaturadas = (calorias * 0.10f) / 9f

    return NutrientesRecomendados(
        calorias,
        proteinas,
        carbohidratos,
        grasas,
        azucares,
        fibra,
        sodio,
        grasasSaturadas
    )
}

@Composable
fun CaloriasGraph(caloriasMin: Int, caloriasMax: Int) {
    val caloriasProm = (caloriasMin + caloriasMax) / 2
    val colorScheme = MaterialTheme.colorScheme
    val textColor = colorScheme.onBackground

    // Animaciones
    var playAnim by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (playAnim) 1f else 0f,
        animationSpec = tween(durationMillis = 1800, easing = LinearOutSlowInEasing),
        label = "lineProgress"
    )
    LaunchedEffect(Unit) { playAnim = true }

    val pulseAnim = rememberInfiniteTransition(label = "pulseAnim")
    val pulseRadius by pulseAnim.animateFloat(
        initialValue = 20f, targetValue = 60f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseRadius"
    )
    val pulseAlpha by pulseAnim.animateFloat(
        initialValue = 0.5f, targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    var tooltipText by remember { mutableStateOf<String?>(null) }
    var tooltipOffset by remember { mutableStateOf(Offset.Zero) }

    val animatedMin = (caloriasMin * progress).toInt()
    val animatedProm = (caloriasProm * progress).toInt()
    val animatedMax = (caloriasMax * progress).toInt()

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .pointerInput(Unit) {
                        detectTapGestures { tapOffset ->
                            val width = size.width
                            val height = size.height

                            val start = Offset(width * 0.1f, height * 0.75f)
                            val end = Offset(width * 0.9f, height * 0.75f)
                            val control1 = Offset(width * 0.3f, height * 0.2f)
                            val control2 = Offset(width * 0.7f, height * 0.2f)

                            val androidPath = android.graphics.Path().apply {
                                moveTo(start.x, start.y)
                                cubicTo(control1.x, control1.y, control2.x, control2.y, end.x, end.y)
                            }
                            val measure = android.graphics.PathMeasure(androidPath, false)
                            val length = measure.length
                            val pos = FloatArray(2)
                            measure.getPosTan(length / 2, pos, null)
                            val promOffset = Offset(pos[0], pos[1])

                            when {
                                (tapOffset - start).getDistance() < 40f -> {
                                    tooltipText = "Mínimo: $animatedMin kcal"
                                    tooltipOffset = start
                                }
                                (tapOffset - promOffset).getDistance() < 40f -> {
                                    tooltipText = "Promedio: $animatedProm kcal"
                                    tooltipOffset = promOffset
                                }
                                (tapOffset - end).getDistance() < 40f -> {
                                    tooltipText = "Máximo: $animatedMax kcal"
                                    tooltipOffset = end
                                }
                                else -> tooltipText = null
                            }
                        }
                    }
            ) {
                val width = size.width
                val height = size.height

                val start = Offset(width * 0.1f, height * 0.75f)
                val end = Offset(width * 0.9f, height * 0.75f)
                val control1 = Offset(width * 0.3f, height * 0.2f)
                val control2 = Offset(width * 0.7f, height * 0.2f)

                val androidPath = android.graphics.Path().apply {
                    moveTo(start.x, start.y)
                    cubicTo(control1.x, control1.y, control2.x, control2.y, end.x, end.y)
                }

                val measure = android.graphics.PathMeasure(androidPath, false)
                val length = measure.length
                val animPath = android.graphics.Path()
                measure.getSegment(0f, length * progress, animPath, true)

                val areaPath = android.graphics.Path(animPath).apply {
                    lineTo(end.x, height)
                    lineTo(start.x, height)
                    close()
                }

                // Grid
                val stepY = height / 5
                for (i in 1..4) {
                    drawLine(
                        color = colorScheme.onBackground.copy(alpha = 0.1f),
                        start = Offset(0f, i * stepY),
                        end = Offset(width, i * stepY),
                        strokeWidth = 1f
                    )
                }

                // Curva + área
                drawIntoCanvas { canvas ->
                    val fillPaint = android.graphics.Paint().apply {
                        shader = android.graphics.LinearGradient(
                            0f, 0f, 0f, height,
                            intArrayOf(colorScheme.primary.copy(alpha = 0.3f).toArgb(), Color.Transparent.toArgb()),
                            null,
                            android.graphics.Shader.TileMode.CLAMP
                        )
                        style = android.graphics.Paint.Style.FILL
                    }
                    canvas.nativeCanvas.drawPath(areaPath, fillPaint)

                    val strokePaint = android.graphics.Paint().apply {
                        shader = android.graphics.LinearGradient(
                            start.x, start.y, end.x, end.y,
                            intArrayOf(colorScheme.primary.toArgb(), colorScheme.tertiary.toArgb(), colorScheme.error.toArgb()),
                            floatArrayOf(0f, 0.5f, 1f),
                            android.graphics.Shader.TileMode.CLAMP
                        )
                        style = android.graphics.Paint.Style.STROKE
                        strokeWidth = 8f
                        isAntiAlias = true
                        strokeCap = android.graphics.Paint.Cap.ROUND
                    }
                    canvas.nativeCanvas.drawPath(animPath, strokePaint)
                }

                if (progress > 0.95f) {
                    val pos = FloatArray(2)
                    measure.getPosTan(length / 2, pos, null)
                    val promOffset = Offset(pos[0], pos[1])

                    // Pulso promedio
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color.White, colorScheme.tertiary.copy(alpha = 0.5f), Color.Transparent),
                            center = promOffset,
                            radius = pulseRadius
                        ),
                        center = promOffset,
                        radius = pulseRadius,
                        alpha = pulseAlpha
                    )
                    drawCircle(colorScheme.tertiary.copy(alpha = 0.2f), radius = pulseRadius * 0.8f, center = promOffset)
                    drawCircle(colorScheme.tertiary, radius = 14f, center = promOffset)

                    // Puntos min y max
                    drawCircle(colorScheme.primary, radius = 12f, center = start)
                    drawCircle(colorScheme.error, radius = 12f, center = end)
                }
            }

            Tooltip(tooltipText, tooltipOffset)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Etiquetas
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Mínimo", style = MaterialTheme.typography.labelMedium, color = textColor)
                Text("$animatedMin kcal", style = MaterialTheme.typography.bodySmall, color = textColor)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Promedio", style = MaterialTheme.typography.labelMedium, color = textColor)
                Text("$animatedProm kcal", style = MaterialTheme.typography.bodySmall, color = textColor)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Máximo", style = MaterialTheme.typography.labelMedium, color = textColor)
                Text("$animatedMax kcal", style = MaterialTheme.typography.bodySmall, color = textColor)
            }
        }
    }
}

@Composable
fun Tooltip(
    tooltipText: String?,
    tooltipOffset: Offset
) {
    val colorScheme = MaterialTheme.colorScheme

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val maxWidthPx = constraints.maxWidth.toFloat()
        val density = LocalDensity.current

        tooltipText?.let { text ->
            with(density) {
                val horizontalPaddingPx = 12.dp.toPx()
                val approxCharWidthPx = 7.dp.toPx()
                val tooltipWidthPx = (text.length * approxCharWidthPx) + horizontalPaddingPx * 2
                val tooltipHeightPx = 32.dp.toPx()
                val extraMarginPx = 12.dp.toPx()

                val maxAllowedX = (maxWidthPx - tooltipWidthPx).coerceAtLeast(0f)
                val clampedXPx = (tooltipOffset.x - tooltipWidthPx / 2f).coerceIn(0f, maxAllowedX)
                val clampedYPx = (tooltipOffset.y - tooltipHeightPx - extraMarginPx).coerceAtLeast(0f)

                val tooltipWidthDp = tooltipWidthPx.toDp()

                Box(
                    modifier = Modifier
                        .offset { IntOffset(clampedXPx.roundToInt(), clampedYPx.roundToInt()) }
                        .shadow(6.dp, shape = RoundedCornerShape(12.dp))
                        .background(colorScheme.surface, shape = RoundedCornerShape(12.dp)) // 🎨 Fondo tema
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = text,
                        color = colorScheme.onSurface, // 🎨 Texto del tema
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(tooltipWidthDp)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistroVent7ScreenPreview(viewModel: UsuarioViewModel = viewModel()) {
    RegistroVent7ScreenContent(viewModel = viewModel)
}
