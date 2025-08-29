package com.example.frontendproyectoapp.screen

import android.app.Application
import android.widget.TextView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import com.example.frontendproyectoapp.viewModel.EstadisticasViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.withSave
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.frontendproyectoapp.model.NutrientesRecomendados
import com.example.frontendproyectoapp.viewModel.EstadisticasViewModelFactory
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.time.format.TextStyle as JavaTextStyle
import androidx.compose.ui.text.TextStyle
import kotlin.math.max
import java.time.Month
import java.util.Locale

@Composable
fun EstadisticasScreen(navController: NavHostController) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: EstadisticasViewModel = viewModel(
        factory = EstadisticasViewModelFactory(application)
    )

    EstadisticasScreenContent(viewModel = viewModel, navController = navController)
}

@Composable
fun EstadisticasScreenContent(
    viewModel: EstadisticasViewModel,
    navController: NavHostController
) {
    val tipoVista = viewModel.tipoVista
    val nutriente by viewModel.nutrienteSeleccionado
    val recomendados = viewModel.nutrientesRecomendados
    val recomendadosMensuales = viewModel.nutrientesRecomendadosMensuales
    val datosGrafica = viewModel.obtenerDatosParaGrafica()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                "Estadisticas",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Divider(
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botones Día/Mes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                SegmentedButton(
                    selected = tipoVista == EstadisticasViewModel.TipoVista.DIA,
                    onClick = { viewModel.cambiarTipoVista(EstadisticasViewModel.TipoVista.DIA) },
                    text = "Día"
                )

                Spacer(modifier = Modifier.width(8.dp))

                SegmentedButton(
                    selected = tipoVista == EstadisticasViewModel.TipoVista.MES,
                    onClick = { viewModel.cambiarTipoVista(EstadisticasViewModel.TipoVista.MES) },
                    text = "Mes"
                )
            }

            // Selector de nutriente
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(EstadisticasViewModel.Nutriente.values()) { item ->
                    val seleccionado = item == nutriente
                    Text(
                        text = item.label,
                        fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
                        color = if (seleccionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (seleccionado)
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                else Color.Transparent
                            )
                            .clickable { viewModel.cambiarNutriente(item) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Flechas de navegación
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (tipoVista == EstadisticasViewModel.TipoVista.DIA) {
                        viewModel.cambiarMes(-1)
                    } else {
                        viewModel.cambiarAnio(-1)
                    }
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Anterior")
                }

                Text(
                    text = if (tipoVista == EstadisticasViewModel.TipoVista.DIA)
                        viewModel.obtenerTituloFecha()
                    else
                        viewModel.anioActual.toString(),
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(onClick = {
                    if (tipoVista == EstadisticasViewModel.TipoVista.DIA) {
                        viewModel.cambiarMes(1)
                    } else {
                        viewModel.cambiarAnio(1)
                    }
                }) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Siguiente")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (tipoVista == EstadisticasViewModel.TipoVista.DIA) {
                GraficaDiaria(
                    datos = datosGrafica,
                    nutriente = nutriente,
                    recomendados = recomendados
                )
            } else {
                GraficaMensual(
                    datos = datosGrafica,
                    nutriente = nutriente,
                    recomendados = recomendadosMensuales
                )
            }
        }
    }
}

@Composable
fun GraficaDiaria(
    datos: List<Pair<Int, Float>>,
    nutriente: EstadisticasViewModel.Nutriente,
    recomendados: NutrientesRecomendados?
) {
    val diasMes = 1..31
    val datosCompletos = diasMes.map { dia ->
        dia to (datos.find { it.first == dia }?.second ?: 0f)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Consumo diario de ${nutriente.label}")
            Spacer(modifier = Modifier.height(8.dp))

            val labels = datosCompletos.map { it.first.toString() }
            val values = datosCompletos.map { it.second }
            val recomendado = recomendados?.let { nutriente.extractorRecomendados(it) } ?: 0f

            NutrienteBarChart(
                labels = labels,
                values = values,
                recomendado = recomendado,
                unidad = nutriente.unidad,
                tipoVista = EstadisticasViewModel.TipoVista.DIA
            )
        }
    }
}

@Composable
fun GraficaMensual(
    datos: List<Pair<Int, Float>>,
    nutriente: EstadisticasViewModel.Nutriente,
    recomendados: NutrientesRecomendados?
) {
    val meses = 1..12
    val datosCompletos = meses.map { mes ->
        mes to (datos.find { it.first == mes }?.second ?: 0f)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Consumo mensual de ${nutriente.label}")
            Spacer(modifier = Modifier.height(8.dp))

            val labels = datosCompletos.map { (m, _) ->
                Month.of(m).getDisplayName(JavaTextStyle.SHORT, Locale("es"))
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("es")) else it.toString() }
            }
            val values = datosCompletos.map { it.second }
            val recomendado = recomendados?.let { nutriente.extractorRecomendados(it) } ?: 0f

            NutrienteBarChart(
                labels = labels,
                values = values,
                recomendado = recomendado,
                unidad = nutriente.unidad,
                tipoVista = EstadisticasViewModel.TipoVista.MES
            )
        }
    }
}

@Composable
fun NutrienteBarChart(
    labels: List<String>,
    values: List<Float>,
    recomendado: Float,
    unidad: String,
    tipoVista: EstadisticasViewModel.TipoVista
) {
    val colores = MaterialTheme.colorScheme
    val consumoColor = colores.primary
    val recomendadoColor = colores.error

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // ✅ Etiqueta vertical compacta para el eje Y
        Box(
            modifier = Modifier.padding(end = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            VerticalAxisLabel(
                text = "Consumo (${unidad})",
                color = colores.onSurface
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp),
                factory = { context ->
                    BarChart(context).apply {
                        description.isEnabled = false
                        setFitBars(true)
                        axisRight.isEnabled = false
                        setTouchEnabled(true)
                        setPinchZoom(false)
                        isDragEnabled = true
                        setScaleEnabled(false)

                        axisLeft.apply {
                            axisMinimum = 0f
                            setDrawGridLines(true)
                            textColor = colores.onSurface.toArgb()
                            gridColor = colores.onSurface.copy(alpha = 0.2f).toArgb()
                        }

                        xAxis.apply {
                            position = XAxis.XAxisPosition.BOTTOM
                            setDrawGridLines(false)
                            granularity = 1f
                            labelRotationAngle = -45f
                            valueFormatter = IndexAxisValueFormatter(labels)
                            textColor = colores.onSurface.toArgb()
                        }

                        legend.isEnabled = false // ❌ ocultamos la de MPAndroidChart
                    }
                },
                update = { chart ->
                    chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                    chart.xAxis.setLabelCount(labels.size, false)

                    val axisMax = (max(values.maxOrNull() ?: 0f, recomendado) * 1.1f).coerceAtLeast(1f)
                    chart.axisLeft.axisMaximum = axisMax
                    chart.axisLeft.granularity = axisMax / 5f

                    chart.axisLeft.valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return "${value.toInt()} $unidad"
                        }
                    }

                    // Línea de recomendado
                    chart.axisLeft.removeAllLimitLines()
                    val ll = LimitLine(recomendado, "${recomendado.toInt()} $unidad").apply {
                        lineWidth = 2f
                        textSize = 11f
                        lineColor = recomendadoColor.toArgb()
                        textColor = recomendadoColor.toArgb()
                        enableDashedLine(10f, 6f, 0f)
                        labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
                    }
                    chart.axisLeft.addLimitLine(ll)

                    // Barras de consumo
                    val consumoEntries = values.mapIndexed { i, v -> BarEntry(i.toFloat(), v) }
                    val dataSet = BarDataSet(consumoEntries, "Consumido").apply {
                        color = consumoColor.toArgb()
                        setDrawValues(false)
                    }

                    chart.data = BarData(dataSet).apply { barWidth = 0.6f }

                    chart.marker = object : MarkerView(chart.context, android.R.layout.simple_list_item_1) {
                        private val textView: TextView = findViewById(android.R.id.text1)
                        override fun refreshContent(e: Entry?, highlight: Highlight?) {
                            textView.setTextColor(colores.onSurface.toArgb())
                            textView.text = "${e?.y?.toInt()} $unidad"
                            super.refreshContent(e, highlight)
                        }
                        override fun getOffset(): MPPointF = MPPointF(-(width / 2f), -height.toFloat())
                    }

                    chart.setVisibleXRangeMaximum(7f)
                    chart.moveViewToX(0f)
                    chart.invalidate()
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ✅ Etiqueta dinámica para el eje X
            Text(
                text = if (tipoVista == EstadisticasViewModel.TipoVista.DIA) "Día" else "Mes",
                style = MaterialTheme.typography.bodyMedium,
                color = colores.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ✅ Leyenda manual
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LeyendaEstadisticas("Consumido", consumoColor)
                LeyendaEstadisticas("Recomendado", recomendadoColor)
            }
        }
    }
}

@Composable
fun LeyendaEstadisticas(text: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun SegmentedButton(selected: Boolean, onClick: () -> Unit, text: String) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(text)
    }
}

@Composable
fun VerticalAxisLabel(
    text: String,
    color: Color,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Canvas(
        modifier = Modifier
            .wrapContentSize()
            .padding(end = 8.dp) // separa un poco del gráfico
    ) {
        val paint = android.graphics.Paint().apply {
            this.color = color.toArgb()
            textSize = style.fontSize.toPx()
            textAlign = android.graphics.Paint.Align.CENTER
            isAntiAlias = true
        }

        // medir el texto
        val x = 8f
        val y = size.height / 2

        // rotar canvas y dibujar
        drawContext.canvas.nativeCanvas.withSave {
            rotate(-90f, x, y)
            drawText(text, x, y, paint)
        }
    }
}
