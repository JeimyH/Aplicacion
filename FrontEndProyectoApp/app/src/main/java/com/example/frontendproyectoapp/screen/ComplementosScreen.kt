package com.example.frontendproyectoapp.screen

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.frontendproyectoapp.ui.theme.dotAgua
import com.example.frontendproyectoapp.ui.theme.dotAmbos
import com.example.frontendproyectoapp.ui.theme.dotComida
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
        Text(nombre)
        Text("$consumido / $recomendado g")
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
            NavigationBarItem(
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.label)
                },
                label = {
                    Text(text = item.label, style = MaterialTheme.typography.labelSmall)
                },
                selected = currentDestination == item.route,
                onClick = {
                    if (currentDestination != item.route) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    }
                },
                alwaysShowLabel = true
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
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.clickable { onExpandedChange(!expanded) }
                )
            },
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
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = { onItemSelected(option) }
                )
            }
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
                quadraticTo(peak.x, peak.y, end.x, end.y)
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

