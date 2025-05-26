package com.example.frontendproyectoapp.screen

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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
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
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Restaurant, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = false,
            onClick = { navController.navigate("inicio") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Buscar Alimentos") },
            label = { Text("Buscar Alimentos") },
            selected = false,
            onClick = { navController.navigate("buscarAlimentos") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Book, contentDescription = "Rutina") },
            label = { Text("Rutina") },
            selected = false,
            onClick = { navController.navigate("rutina") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Assessment, contentDescription = "Estadísticas") },
            label = { Text("Estadísticas") },
            selected = false,
            onClick = { navController.navigate("estadisticas") }
        )
    }
}

@Composable
fun DropdownSelector(
    label: String,
    selected: String,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onItemSelected: (String) -> Unit
) {
    Box {
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
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
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
    onDateSelected: (LocalDate) -> Unit
) {
    //val today = LocalDate.now()
    val today = remember { LocalDate.now(ZoneId.systemDefault()) }
    val currentMonth = remember { mutableStateOf(YearMonth.now()) }
    val daysInMonth = currentMonth.value.lengthOfMonth()
    val firstDayOfWeek = currentMonth.value.atDay(1).dayOfWeek.value % 7

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf("D", "L", "M", "M", "J", "V", "S").forEach {
                Text(text = it, style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

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

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isSelected -> Color(0xFF2196F3)
                                            isToday -> Color.LightGray
                                            else -> Color.Transparent
                                        }
                                    )
                                    .clickable {
                                        onDateSelected(date)
                                    }
                            ) {
                                Text(
                                    text = day.toString(),
                                    color = if (isSelected) Color.White else Color.Black
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
