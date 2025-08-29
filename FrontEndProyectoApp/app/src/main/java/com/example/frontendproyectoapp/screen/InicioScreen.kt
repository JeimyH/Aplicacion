package com.example.frontendproyectoapp.screen

import android.app.Application
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.frontendproyectoapp.model.NutrientesRecomendados
import com.example.frontendproyectoapp.model.NutrientesTotales
import com.example.frontendproyectoapp.ui.theme.dotAgua
import com.example.frontendproyectoapp.ui.theme.dotAmbos
import com.example.frontendproyectoapp.ui.theme.dotComida
import com.example.frontendproyectoapp.viewModel.InicioViewModel
import com.example.frontendproyectoapp.viewModel.InicioViewModelFactory
import com.example.frontendproyectoapp.viewModel.UsuarioViewModel
import com.example.frontendproyectoapp.viewModel.UsuarioViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun InicioScreen(navController: NavHostController) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: InicioViewModel = viewModel(
        factory = InicioViewModelFactory(application)
    )

    InicioScreenContent(viewModel = viewModel, navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreenContent(
    viewModel: InicioViewModel,
    navController: NavHostController,
    usuarioViewModel: UsuarioViewModel = viewModel(factory = UsuarioViewModelFactory(LocalContext.current.applicationContext as Application))
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now(ZoneId.systemDefault())) }
    var mostrarLeyenda by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val cantidadVasos = 8
    val vasosConsumidos = viewModel.vasosConsumidosHoy
    val estadoCarga = viewModel.estadoCarga
    val diasConActividad = viewModel.diasConActividad

    val nutrientes = viewModel.nutrientesTotales
    val recomendados = viewModel.nutrientes.value

    val colorSeleccionado = MaterialTheme.colorScheme.primary
    val colorNoSeleccionado = MaterialTheme.colorScheme.surface
    val iconTintSeleccionado = MaterialTheme.colorScheme.onPrimary
    val iconTintNoSeleccionado = MaterialTheme.colorScheme.onSurface

    val snackbarHostState = remember { SnackbarHostState() }

    val diasConActividadMap = diasConActividad
        .groupBy { LocalDate.parse(it.fecha) }
        .mapValues { it.value.map { it.tipo }.toSet() }

    // Cargar usuario y datos
    LaunchedEffect(true) {
        viewModel.cargarIdUsuario(context)
    }

    val idUsuario by viewModel.idUsuario.collectAsState()

    LaunchedEffect(idUsuario) {
        idUsuario?.let {
            viewModel.establecerIdUsuario(it)
            viewModel.cargarDatosUsuarioActual(it)
            viewModel.cargarDiasConActividad()
            viewModel.cargarTotalesDelDia(context)
            viewModel.cargarRecomendaciones(it)
            usuarioViewModel.cargarNombreUsuario(it)
        }
    }

    // Mostrar snackbar de mensaje
    LaunchedEffect(Unit) {
        viewModel.mensajeUI.collect { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (usuarioViewModel.sexoUsuario == "Masculino"){
                            Text(
                                "¡Bienvenido,",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }else{
                            Text(
                                "¡Bienvenida,",
                                //style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "${usuarioViewModel.nombreUsuario}!",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("configuracion") }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Ir a configuración",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // Calendario
            CustomCalendar(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                diasConActividad = diasConActividadMap
            )

            // Botón de leyenda
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { mostrarLeyenda = !mostrarLeyenda }) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Mostrar leyenda",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            LeyendaActividadCalendario(
                expandido = mostrarLeyenda,
                onDismiss = { mostrarLeyenda = false }
            )

            // Calorías
            if (nutrientes != null && recomendados != null) {
                CaloriasGraphInicio(
                    caloriasTotales = nutrientes.calorias,
                    caloriasRecomendadas = recomendados.calorias
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Nutrientes
            ComparacionNutricionalCard(nutrientes = nutrientes, recomendados = recomendados)

            Spacer(modifier = Modifier.height(24.dp))

            // Agua
            Text(
                "¿Cuántos vasos de agua has tomado hoy?",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Grilla de vasos con estilo de tarjeta
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false
            ) {
                items((1..cantidadVasos).toList()) { vaso ->
                    val seleccionado = vaso <= vasosConsumidos
                    Card(
                        modifier = Modifier.size(44.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (seleccionado) colorSeleccionado else colorNoSeleccionado,
                            contentColor = if (seleccionado) iconTintSeleccionado else iconTintNoSeleccionado
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    val nuevaCantidad = if (vaso == vasosConsumidos) 0 else vaso
                                    viewModel.seleccionarCantidadVasos(nuevaCantidad)
                                    viewModel.registrarAgua()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalDrink,
                                contentDescription = null,
                                tint = if (seleccionado) iconTintSeleccionado else iconTintNoSeleccionado
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Total de agua
            Text(
                "Total consumido: ${vasosConsumidos * 250} ml",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            if (estadoCarga) {
                CircularProgressIndicator(
                    Modifier.padding(top = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun CaloriasGraphInicio(
    caloriasTotales: Float,
    caloriasRecomendadas: Float
) {
    val colorScheme = MaterialTheme.colorScheme
    val textColor = colorScheme.onBackground
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    val targetRatio = if (caloriasRecomendadas > 0f) (caloriasTotales / caloriasRecomendadas) else 0f

    // Animación de curva
    var startAnim by remember { mutableStateOf(false) }
    val animatedRatio by animateFloatAsState(
        targetValue = if (startAnim) targetRatio.coerceAtLeast(0f) else 0f,
        animationSpec = tween(durationMillis = 2000, easing = LinearOutSlowInEasing),
        label = "curveAnim"
    )
    LaunchedEffect(targetRatio) { startAnim = true }

    val indicadorSize = 20.dp
    val canvasSize = remember { mutableStateOf(IntSize.Zero) }

    // Tooltip
    var mostrarTooltip by remember { mutableStateOf(false) }
    var tooltipText by remember { mutableStateOf("") }

    // Pulso más fluido
    val infiniteTransition = rememberInfiniteTransition(label = "pulseAnim")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseScale"
    )
    val alphaPulse by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .onSizeChanged { canvasSize.value = it }
                .padding(horizontal = 8.dp)
        ) {
            // --- Canvas con animación ---
            Canvas(modifier = Modifier.matchParentSize()) {
                val w = size.width
                val h = size.height
                if (w <= 0 || h <= 0) return@Canvas

                val start = Offset(w * 0.1f, h * 0.75f)
                val end = Offset(w * 0.9f, h * 0.75f)
                val c1 = Offset(w * 0.3f, h * 0.2f)
                val c2 = Offset(w * 0.7f, h * 0.2f)

                val fullPath = android.graphics.Path().apply {
                    moveTo(start.x, start.y)
                    cubicTo(c1.x, c1.y, c2.x, c2.y, end.x, end.y)
                }

                val measure = android.graphics.PathMeasure(fullPath, false)
                val length = measure.length
                val animPath = android.graphics.Path()
                measure.getSegment(0f, length * animatedRatio.coerceIn(0f, 1f), animPath, true)

                // 🔹 Coordenada actual de la curva (posición del botón)
                val pos = FloatArray(2)
                measure.getPosTan(length * animatedRatio.coerceIn(0f, 1f), pos, null)
                val currentX = pos[0]
                val currentY = pos[1]

                // 🔹 Área bajo curva (solo hasta currentX)
                val areaPath = android.graphics.Path(animPath).apply {
                    lineTo(currentX, h)   // baja desde el punto actual
                    lineTo(start.x, h)    // cierra en la base inicial
                    close()
                }

                // grid ligero
                val stepY = h / 5
                for (i in 1..4) {
                    drawLine(
                        color = colorScheme.onBackground.copy(alpha = 0.08f),
                        start = Offset(0f, i * stepY),
                        end = Offset(w, i * stepY),
                        strokeWidth = 1f
                    )
                }

                // --- Línea base gris tenue ---
                drawIntoCanvas { canvas ->
                    val basePaint = android.graphics.Paint().apply {
                        color = colorScheme.onBackground.copy(alpha = 0.12f).toArgb()
                        style = android.graphics.Paint.Style.STROKE
                        strokeWidth = 6f
                        isAntiAlias = true
                        strokeCap = android.graphics.Paint.Cap.ROUND
                    }
                    canvas.nativeCanvas.drawPath(fullPath, basePaint)
                }

                // --- Área degradada + curva animada ---
                drawIntoCanvas { canvas ->
                    // 🔹 Área con degradado horizontal siguiendo la curva
                    val fillPaint = android.graphics.Paint().apply {
                        shader = android.graphics.LinearGradient(
                            0f, 0f, size.width, size.height, // 🔹 ocupatodo el area
                            intArrayOf(
                                colorScheme.primary.copy(alpha = 0.25f).toArgb(),
                                colorScheme.tertiary.copy(alpha = 0.25f).toArgb(),
                                colorScheme.error.copy(alpha = 0.25f).toArgb()
                            ),
                        floatArrayOf(0f, 0.6f, 1f),
                        android.graphics.Shader.TileMode.CLAMP
                        )

                        style = android.graphics.Paint.Style.FILL
                    }
                    canvas.nativeCanvas.drawPath(areaPath, fillPaint)

                    // 🔹 Curva con el mismo gradiente (sin cambios)
                    val strokePaint = android.graphics.Paint().apply {
                        shader = android.graphics.LinearGradient(
                            start.x, start.y, end.x, end.y,
                            intArrayOf(
                                colorScheme.primary.toArgb(),
                                colorScheme.tertiary.toArgb(),
                                colorScheme.error.toArgb()
                            ),
                            floatArrayOf(0f, 0.6f, 1f),
                            android.graphics.Shader.TileMode.CLAMP
                        )
                        style = android.graphics.Paint.Style.STROKE
                        strokeWidth = 6f
                        isAntiAlias = true
                        strokeCap = android.graphics.Paint.Cap.ROUND
                    }
                    canvas.nativeCanvas.drawPath(animPath, strokePaint)
                }

            }


            // --- Botón indicador con degradado dinámico ---
            val indicadorOffsetDp: Pair<Dp, Dp> = remember(canvasSize.value, animatedRatio) {
                val w = canvasSize.value.width.toFloat()
                val h = canvasSize.value.height.toFloat()
                if (w <= 0 || h <= 0) return@remember 0.dp to 0.dp

                val start = Offset(w * 0.1f, h * 0.75f)
                val end = Offset(w * 0.9f, h * 0.75f)
                val c1 = Offset(w * 0.3f, h * 0.2f)
                val c2 = Offset(w * 0.7f, h * 0.2f)

                val path = android.graphics.Path().apply {
                    moveTo(start.x, start.y)
                    cubicTo(c1.x, c1.y, c2.x, c2.y, end.x, end.y)
                }

                val measure = android.graphics.PathMeasure(path, false)
                val pos = FloatArray(2)
                measure.getPosTan(measure.length * animatedRatio.coerceIn(0f, 1f), pos, null)

                with(density) { pos[0].toDp() } to with(density) { pos[1].toDp() }
            }

            val posX = indicadorOffsetDp.first
            val posY = indicadorOffsetDp.second
            val halfSize = indicadorSize / 2

            // --- Color interpolado dinámico ---
            val indicadorColor = remember(animatedRatio) {
                val gradientColors = listOf(
                    colorScheme.primary,
                    colorScheme.tertiary,
                    colorScheme.error
                )
                val gradientStops = listOf(0f, 0.6f, 1f)

                val t = animatedRatio.coerceIn(0f, 1f)
                val idx = gradientStops.indexOfLast { it <= t }.coerceAtLeast(0)
                if (idx == gradientStops.lastIndex) gradientColors.last()
                else {
                    val localT = (t - gradientStops[idx]) / (gradientStops[idx + 1] - gradientStops[idx])
                    lerp(gradientColors[idx], gradientColors[idx + 1], localT)
                }
            }

            // Pulso detrás
            Box(
                modifier = Modifier
                    .offset(posX - halfSize, posY - halfSize)
                    .size(indicadorSize * pulse)
                    .background(indicadorColor.copy(alpha = alphaPulse), CircleShape)
            )

            // Indicador principal
            Box(
                modifier = Modifier
                    .offset(posX - halfSize, posY - halfSize)
                    .size(indicadorSize)
                    .background(indicadorColor, CircleShape)
                    .clickable {
                        tooltipText = "${caloriasTotales.toInt()} kcal"
                        mostrarTooltip = true
                        scope.launch {
                            delay(2000)
                            mostrarTooltip = false
                        }
                    }
            )

            if (mostrarTooltip) {
                Text(
                    text = tooltipText,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = textColor,
                    modifier = Modifier
                        .offset(posX - 20.dp, posY - 48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Labels
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text("Calorías Totales", style = MaterialTheme.typography.labelMedium, color = textColor)
                Text("${caloriasTotales.toInt()} kcal", style = MaterialTheme.typography.bodySmall, color = textColor)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Calorías Recomendadas", style = MaterialTheme.typography.labelMedium, color = colorScheme.primary)
                Text("${caloriasRecomendadas.toInt()} kcal", style = MaterialTheme.typography.bodySmall, color = colorScheme.primary)
            }
        }
    }
}

@Composable
fun ComparacionNutricionalCard(
    nutrientes: NutrientesTotales?,
    recomendados: NutrientesRecomendados?
) {
    if (nutrientes != null && recomendados != null) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface, // adaptable a claro/oscuro
                contentColor = MaterialTheme.colorScheme.onSurface // color del texto base
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Resumen Nutricional Diario",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                NutrientesRow("Proteínas", nutrientes.proteinas, recomendados.proteinas, "🥩")
                NutrientesRow("Carbohidratos", nutrientes.carbohidratos, recomendados.carbohidratos, "🍞"
                )
                NutrientesRow("Grasas", nutrientes.grasas, recomendados.grasas, "🧈")
                NutrientesRow("Azúcares", nutrientes.azucares, recomendados.azucares, "🍬")
                NutrientesRow("Fibra", nutrientes.fibra, recomendados.fibra, "🌾")
                NutrientesRow("Sodio", nutrientes.sodio, recomendados.sodio, "🧂")
                NutrientesRow("Grasas Saturadas", nutrientes.grasasSaturadas, recomendados.grasasSaturadas, "🥓"
                )
            }
        }
    }
}

@Composable
fun NutrientesRow(nombre: String, consumido: Float, recomendado: Float, emoji: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$emoji $nombre",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = String.format("%.1f / %.1f g", consumido, recomendado),
                style = MaterialTheme.typography.bodyMedium,
                color = when {
                    consumido > recomendado -> MaterialTheme.colorScheme.error
                    consumido < recomendado * 0.8f -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = (consumido / recomendado).coerceIn(0f, 1f),
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = when {
                consumido > recomendado -> MaterialTheme.colorScheme.error
                consumido < recomendado * 0.8f -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.tertiary
            },
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
fun LeyendaActividadCalendario(
    expandido: Boolean,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = expandido,
        enter = expandVertically(animationSpec = tween(300)) + fadeIn(),
        exit = shrinkVertically(animationSpec = tween(200)) + fadeOut()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface, // adaptable a claro/oscuro
                contentColor = MaterialTheme.colorScheme.onSurface // color del texto base
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Leyenda del Calendario",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(12.dp))

                LeyendaItem("Día con registro de agua", MaterialTheme.colorScheme.dotAgua)
                LeyendaItem("Día con registro de comida", MaterialTheme.colorScheme.dotComida)
                LeyendaItem("Día con ambos registros", MaterialTheme.colorScheme.dotAmbos)

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Toca el ícono nuevamente para cerrar.",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun LeyendaItem(text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
fun CustomCalendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    diasConActividad: Map<LocalDate, Set<String>>
) {
    val today = remember { LocalDate.now(ZoneId.systemDefault()) }
    val currentMonth = remember { mutableStateOf(YearMonth.now()) }
    val daysInMonth = currentMonth.value.lengthOfMonth()
    val firstDayOfWeek = currentMonth.value.atDay(1).dayOfWeek.value % 7

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Encabezado del mes
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                currentMonth.value = currentMonth.value.minusMonths(1)
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Mes anterior")
            }

            Text(
                text = currentMonth.value.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                    .replaceFirstChar { it.uppercase() } + " ${currentMonth.value.year}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            IconButton(onClick = {
                currentMonth.value = currentMonth.value.plusMonths(1)
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Mes siguiente")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Días de la semana
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf("D", "L", "M", "M", "J", "V", "S").forEach {
                Text(text = it, style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Celdas del calendario
        val totalCells = daysInMonth + firstDayOfWeek
        val weeks = (totalCells / 7) + if (totalCells % 7 != 0) 1 else 0

        Column {
            repeat(weeks) { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    (0..6).forEach { dayOfWeek ->
                        val dayIndex = week * 7 + dayOfWeek
                        if (dayIndex < firstDayOfWeek || dayIndex >= daysInMonth + firstDayOfWeek) {
                            Spacer(modifier = Modifier.size(40.dp))
                        } else {
                            val day = dayIndex - firstDayOfWeek + 1
                            val date = currentMonth.value.atDay(day)
                            val isSelected = date == selectedDate
                            val isToday = date == today

                            val tipoActividad = diasConActividad[date] ?: emptySet()
                            val color = when {
                                tipoActividad.contains("AMBOS") -> MaterialTheme.colorScheme.dotAmbos
                                tipoActividad.contains("AGUA") -> MaterialTheme.colorScheme.dotAgua
                                tipoActividad.contains("COMIDA") -> MaterialTheme.colorScheme.dotComida
                                else -> Color.Transparent
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                isSelected -> MaterialTheme.colorScheme.primary
                                                isToday -> Color.LightGray
                                                else -> Color.Transparent
                                            }
                                        )
                                        .clickable { onDateSelected(date) }
                                ) {
                                    Text(
                                        text = day.toString(),
                                        color = if (isSelected)
                                            MaterialTheme.colorScheme.onPrimary
                                        else
                                            MaterialTheme.colorScheme.onBackground
                                    )
                                }

                                if (color != Color.Transparent) {
                                    Canvas(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .padding(top = 2.dp)
                                    ) {
                                        drawCircle(color = color)
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewInicioScreen() {
    val navController = rememberNavController()
    InicioScreen(navController = navController)
}
