package com.example.frontendproyectoapp.screen

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.frontendproyectoapp.model.TarjetaData
import com.example.frontendproyectoapp.DataStores.UserPreferences
import com.example.frontendproyectoapp.viewModel.UsuarioViewModel
import com.example.frontendproyectoapp.viewModel.UsuarioViewModelFactory
import kotlinx.coroutines.flow.map

@Composable
fun ConfiguracionScreen(
    navController: NavHostController,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val usuarioViewModel: UsuarioViewModel = viewModel(
        factory = UsuarioViewModelFactory(application)
    )

    // Recupera idUsuario desde DataStore como Flow y conviértelo a Long
    val idUsuario by UserPreferences.obtenerIdUsuario(context)
        .map { it ?: 0L } // si es null, por defecto 0
        .collectAsState(initial = 0L)

    LaunchedEffect(idUsuario) {
        if (idUsuario != 0L) {
            usuarioViewModel.cargarNombreUsuario(idUsuario)
        }
    }

    ConfiguracionScreenContent(
        navController = navController,
        isDarkTheme = isDarkTheme,
        onThemeChange = onThemeChange,
        viewModel = usuarioViewModel
    )
}

@Composable
fun ConfiguracionScreenContent(
    navController: NavHostController,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    viewModel: UsuarioViewModel
) {
    val opcionesAjustes = listOf(
        TarjetaData(
            "Perfil",
            "Edita tu información personal.",
            "https://cdn-icons-png.flaticon.com/512/847/847969.png"
        ),
        TarjetaData(
            "Recordatorios",
            "Activa notificaciones para tus hábitos.",
            "https://cdn-icons-png.flaticon.com/128/4285/4285578.png"
        )
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                "Configuración",
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

            ConfiguracionHeader(navController, nombre = viewModel.nombreUsuario)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Aplicación", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            // Cards de Perfil y Recordatorios
            opcionesAjustes.forEach { opcion ->
                ConfiguracionCard(opcion.titulo, opcion.descripcion, opcion.imagenUrl) {
                    when (opcion.titulo) {
                        "Recordatorios" -> navController.navigate("recordatorios")
                        "Perfil" -> navController.navigate("perfil")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Card de Tema Oscuro
            TemaOscuroCard(
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Otros", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            OtrosCard(titulo = "Políticas de Privacidad",
                imagenUrl = "https://cdn-icons-png.flaticon.com/128/7790/7790150.png",
                pdfFileName = "ee_formato_ieee.pdf")
            OtrosCard(titulo = "Términos y Condiciones",
                imagenUrl = "https://cdn-icons-png.flaticon.com/128/4436/4436515.png",
                pdfFileName = "ee_formato_ieee.pdf")

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Versión 1.0.0",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ConfiguracionHeader(navController: NavHostController, nombre: String = "Usuario") {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.22f)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = "https://cdn-icons-png.flaticon.com/128/8709/8709730.png",
                contentDescription = "Avatar usuario",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Hola, $nombre!",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Ver perfil",
                style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.Underline),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clickable { navController.navigate("perfil") }
            )
        }
    }
}

@Composable
fun ConfiguracionCard(
    titulo: String,
    descripcion: String,
    imagenUrl: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imagenUrl,
                contentDescription = titulo,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp)),
                    //.background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Ir a $titulo",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// Card personalizada para Tema Oscuro
@Composable
fun TemaOscuroCard(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "https://cdn-icons-png.flaticon.com/128/6803/6803223.png",
                contentDescription = "Tema oscuro",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp)),
                    //.background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Tema oscuro",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = isDarkTheme,
                onCheckedChange = onThemeChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                    uncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                )
            )
        }
    }
}

@Composable
fun OtrosCard(
    titulo: String,
    imagenUrl: String,
    pdfFileName: String // 👈 le pasamos el nombre del PDF en assets
) {
    var mostrarDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { mostrarDialog = true }, // 👈 al hacer clic abre el PDF
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imagenUrl,
                contentDescription = titulo,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp)),
                //.background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }

    // 👉 Mostramos el diálogo con el PDF
    if (mostrarDialog) {
        AlertDialog(
            onDismissRequest = { mostrarDialog = false },
            title = { Text(titulo) },
            text = { PDFViewerFromAssets(pdfFileName) },
            confirmButton = {
                TextButton(onClick = { mostrarDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

