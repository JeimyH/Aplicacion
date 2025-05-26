package com.example.frontendproyectoapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@Composable
fun RegistroVent7Screen(navController: NavController) {
    RegistroVent7ScreenContent(
        onBackClick = { navController.popBackStack() },
        onContinueClick = { navController.navigate("registro8") } // Ajusta la ruta destino
    )
}

@Composable
fun RegistroVent7ScreenContent(
    onBackClick: () -> Unit = {},
    onContinueClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Selecciona tus alimentos más consumidos",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "y que nunca te faltan\nTu rutina se ajustará a los alimentos que selecciones",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        GrupoAlimentos("Bebidas y Lácteos", listOf("Bebida", "Leche", "Queso"))
        GrupoAlimentos("Frutas\nElige al menos 2 frutas del grupo", listOf("Manzana", "Durazno", "Pera"))
        GrupoAlimentos("Verduras y tubérculos", listOf("Zanahoria", "Pimentón", "Espinaca"))
        GrupoAlimentos("Salsas y condimentos", listOf("Mostaza", "Canela", "Salsa"))
        GrupoAlimentos("Proteínas\nElige al menos 2 proteínas del grupo", listOf("Pollo", "Pescado"))
        GrupoAlimentos("Carbohidratos\nElige al menos 3 carbohidratos del grupo", listOf("Pan", "Arroz", "Avena", "Papa", "Lentejas", "Cereal"))
        GrupoAlimentos("Grasas\nElige al menos 2 grasas del grupo", listOf("Pan", "Arroz", "Papa", "Lentejas", "Cereal"))

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onContinueClick,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Continuar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroVent7ScreenPreview() {
    RegistroVent7ScreenContent()
}

@Composable
fun GrupoAlimentos(titulo: String, alimentos: List<String>) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))

        val filas = alimentos.chunked(3)
        for (fila in filas) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                fila.forEach { alimento ->
                    AlimentoItem(nombre = alimento)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AlimentoItem(nombre: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = rememberAsyncImagePainter(model = obtenerUrlIcono(nombre)),
            contentDescription = nombre,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )
        Text(text = nombre, fontSize = 12.sp)
    }
}


fun obtenerUrlIcono(nombre: String): String {
    return when (nombre.lowercase()) {
        "manzana" -> "https://drive.google.com/uc?export=view&id=1nh7V0Po50DhPxM5Lgi_43kfvPR2SHoI1"
        "durazno" -> "https://drive.google.com/uc?export=view&id=1TxbfObd1CGjInzWhi-g0WHM9TshbOkaL"
        "pollo" -> "https://drive.google.com/uc?export=view&id=1tdoqNeHTHuRcpBRXgpIv7FhYnqe28XVC"
        // Agrega todos los demás...
        else -> "https://drive.google.com/uc?export=view&id=1K1eVJHxzc_DKEmXBv3gHk71UdWeGEfGL"
    }
}