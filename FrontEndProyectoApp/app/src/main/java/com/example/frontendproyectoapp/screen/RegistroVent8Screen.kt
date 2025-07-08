package com.example.frontendproyectoapp.screen

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.frontendproyectoapp.model.Alimento
import com.example.frontendproyectoapp.viewModel.BuscarAlimentoViewModel
import com.example.frontendproyectoapp.viewModel.BuscarAlimentoViewModelFactory
import com.example.frontendproyectoapp.viewModel.UsuarioViewModel

@Composable
fun RegistroVent8Screen(navController: NavController, usuarioViewModel: UsuarioViewModel) {
    val context = LocalContext.current
    val viewModel: BuscarAlimentoViewModel = viewModel(
        factory = BuscarAlimentoViewModelFactory(context.applicationContext as Application)
    )

    LaunchedEffect(Unit) {
        viewModel.cargarAlimentosAgrupados()
    }

    RegistroVent8ScreenContent(
        buscarViewModel = viewModel,
        usuarioViewModel = usuarioViewModel,
        onBackClick = { navController.popBackStack() },
        onContinueClick = { navController.navigate("registro9") }
    )
}

@Composable
fun RegistroVent8ScreenContent(
    buscarViewModel: BuscarAlimentoViewModel,
    usuarioViewModel: UsuarioViewModel,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    val alimentosAgrupados = buscarViewModel.alimentosAgrupados
    val favoritosPorCategoria = usuarioViewModel.favoritosPorCategoria

    var categoriaAdvertencia by remember { mutableStateOf<String?>(null) }
    var mostrarErrorGlobal by remember { mutableStateOf(false) }

    val todasLasCategoriasCumplen = alimentosAgrupados.all { (categoria, _) ->
        favoritosPorCategoria[categoria]?.isNotEmpty() == true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        // Barra de progreso
        LinearProgressIndicator(
            progress = 5 / 6f,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        // Botón de retroceso fuera del scroll
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Atrás",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable { onBackClick() }
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Selecciona tus alimentos más consumidos",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        if (categoriaAdvertencia != null) {
            Text(
                text = "Máximo 3 alimentos para ${categoriaAdvertencia}",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (mostrarErrorGlobal && !todasLasCategoriasCumplen) {
            Text(
                text = "Selecciona al menos un alimento por categoría para continuar.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            alimentosAgrupados.forEach { (categoria, alimentos) ->
                val seleccionadosCat = favoritosPorCategoria[categoria].orEmpty()
                val idsSeleccionados: Set<Long> = seleccionadosCat.map { it.idAlimento }.toSet()

                CategoriaAlimentosComposable(
                    titulo = categoria,
                    alimentos = alimentos,
                    idsSeleccionados = idsSeleccionados,
                    onToggle = { alimento ->
                        val yaSeleccionado = idsSeleccionados.contains(alimento.idAlimento)
                        if (!yaSeleccionado && seleccionadosCat.size >= 3) {
                            categoriaAdvertencia = categoria
                        } else {
                            categoriaAdvertencia = null
                            usuarioViewModel.toggleFavoritoConLimite(alimento, categoria)
                        }
                    },
                    contador = "${idsSeleccionados.size} / 3"
                )
            }

            Spacer(modifier = Modifier.height(48.dp))
        }

        // Botón continuar
        Button(
            onClick = {
                if (todasLasCategoriasCumplen) {
                    onContinueClick()
                } else {
                    mostrarErrorGlobal = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = todasLasCategoriasCumplen,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
            )
        ) {
            Text("Continuar", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun CategoriaAlimentosComposable(
    titulo: String,
    alimentos: List<Alimento>,
    idsSeleccionados: Set<Long>,
    onToggle: (Alimento) -> Unit,
    contador: String = ""
) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = contador,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        val filas = alimentos.chunked(3)
        filas.forEach { fila ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                fila.forEach { alimento ->
                    AlimentoItemUrl(
                        alimento = alimento,
                        esSeleccionado = idsSeleccionados.contains(alimento.idAlimento),
                        onToggle = { onToggle(alimento) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AlimentoItemUrl(
    alimento: Alimento,
    esSeleccionado: Boolean,
    onToggle: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .clickable { onToggle() }
                .background(
                    if (esSeleccionado)
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
                .border(
                    width = 2.dp,
                    color = if (esSeleccionado)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = alimento.urlImagen ?: "https://via.placeholder.com/100"
                ),
                contentDescription = alimento.nombreAlimento,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp)
            )

            if (esSeleccionado) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Seleccionado",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(20.dp)
                        .padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = alimento.nombreAlimento,
            fontSize = 12.sp,
            fontWeight = if (esSeleccionado) FontWeight.Bold else FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

/*
@Preview(showBackground = true)
@Composable
fun RegistroVent8ScreenPreview() {
    RegistroVent8ScreenContent()
}

 */
