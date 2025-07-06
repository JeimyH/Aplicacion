package com.example.frontendproyectoapp.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntAsState
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.frontendproyectoapp.ui.theme.TextOlive
import com.example.frontendproyectoapp.ui.theme.dotAgua
import com.example.frontendproyectoapp.ui.theme.dotAmbos
import com.example.frontendproyectoapp.ui.theme.dotComida
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun NutrientRow(nombre: String, consumido: Int, recomendado: Int) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            nombre,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            "$consumido / $recomendado g",
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val items = listOf(
        BottomNavItem("inicio", Icons.Default.Restaurant, "Inicio"),
        BottomNavItem("buscarAlimentos", Icons.Default.Search, "Buscar"),
        BottomNavItem("rutina", Icons.Default.Book, "Rutina"),
        BottomNavItem("estadisticas", Icons.Default.Assessment, "Estadísticas")
    )

    NavigationBar(
        tonalElevation = 8.dp,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        items.forEach { item ->
            val selected = currentDestination == item.route

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    }
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primary // ← Fondo del ítem activo
                )
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@Composable
fun DropdownSelector(
    label: String,
    selected: String,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var textFieldSize by remember { mutableStateOf(IntSize.Zero) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    text = label,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onExpandedChange(!expanded) }
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size
                }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                .background(MaterialTheme.colorScheme.surface)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            option,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = { onItemSelected(option) }
                )
            }
        }
    }
}

@Composable
fun CaloriasGraph(caloriasMin: Int, caloriasMax: Int) {
    val caloriasProm = (caloriasMin + caloriasMax) / 2
    val graphColor = MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onBackground

    // 🔹 Estados animados personalizados
    var animatedMin by remember { mutableIntStateOf(0) }
    var animatedProm by remember { mutableIntStateOf(0) }
    var animatedMax by remember { mutableIntStateOf(0) }

    val animatedProgress by rememberInfiniteTransition(label = "graphAnim")
        .animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1600, easing = LinearOutSlowInEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "animatedLine"
        )

    // 🔹 Iniciar animación una vez (de 0 hasta el valor final)
    LaunchedEffect(Unit) {
        val duration = 1000
        val steps = 60
        val delayPerFrame = duration / steps

        repeat(steps + 1) { frame ->
            val fraction = frame / steps.toFloat()
            animatedMin = (caloriasMin * fraction).toInt()
            animatedProm = (caloriasProm * fraction).toInt()
            animatedMax = (caloriasMax * fraction).toInt()
            delay(delayPerFrame.toLong())
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            val width = size.width
            val height = size.height

            val start = Offset(x = width * 0.2f, y = height * 0.8f)
            val end = Offset(x = width * 0.8f, y = height * 0.8f)
            val peak = Offset(x = width * 0.5f, y = height * 0.2f)

            val animatedX = start.x + (end.x - start.x) * animatedProgress

            val animatedPath = Path().apply {
                moveTo(start.x, start.y)

                if (animatedX <= peak.x) {
                    val t = (animatedX - start.x) / (peak.x - start.x)
                    val y = lerp(start.y, peak.y, t)
                    quadraticTo(
                        start.x + (peak.x - start.x) * 0.5f,
                        lerp(start.y, peak.y, 0.5f),
                        animatedX,
                        y
                    )
                } else {
                    quadraticTo(
                        peak.x,
                        peak.y,
                        animatedX,
                        lerp(peak.y, end.y, (animatedX - peak.x) / (end.x - peak.x))
                    )
                }
            }

            drawPath(
                path = animatedPath,
                color = graphColor,
                style = Stroke(width = 4f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            drawCircle(color = graphColor, radius = 6f, center = start)
            drawCircle(color = graphColor, radius = 6f, center = end)

            drawLine(
                color = graphColor,
                start = Offset(start.x, start.y),
                end = Offset(start.x, start.y - 20),
                strokeWidth = 3f
            )
            drawLine(
                color = graphColor,
                start = Offset(end.x, end.y),
                end = Offset(end.x, end.y - 20),
                strokeWidth = 3f
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Valores sincronizados con la animación
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Mínimo", style = MaterialTheme.typography.labelMedium, color = textColor)
                Text("${animatedMin} kcal", style = MaterialTheme.typography.bodySmall, color = textColor)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Promedio", style = MaterialTheme.typography.labelMedium, color = textColor)
                Text("${animatedProm} kcal", style = MaterialTheme.typography.bodySmall, color = textColor)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Máximo", style = MaterialTheme.typography.labelMedium, color = textColor)
                Text("${animatedMax} kcal", style = MaterialTheme.typography.bodySmall, color = textColor)
            }
        }
    }
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

@Composable
fun LeyendaActividadCalendario(expandido: Boolean, onDismiss: () -> Unit) {
    AnimatedVisibility(
        visible = expandido,
        enter = expandVertically(animationSpec = tween(300)) + fadeIn(),
        exit = shrinkVertically(animationSpec = tween(200)) + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(12.dp)
        ) {
            Text("Leyenda del Calendario", style = MaterialTheme.typography.titleSmall)

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(MaterialTheme.colorScheme.dotAgua, shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Día con registro de agua", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(MaterialTheme.colorScheme.dotComida, shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Día con registro de comida", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(MaterialTheme.colorScheme.dotAmbos, shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Día con ambos registros", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(8.dp))

            Text(
                "Toca el ícono nuevamente para cerrar.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

