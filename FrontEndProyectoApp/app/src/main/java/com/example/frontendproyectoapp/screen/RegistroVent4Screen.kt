package com.example.frontendproyectoapp.screen

import android.app.Application
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendproyectoapp.ui.theme.FrontEndProyectoAppTheme
import com.example.frontendproyectoapp.viewModel.UsuarioViewModel


@Composable
fun RegistroVent4Screen(navController: NavController, viewModel: UsuarioViewModel) {
    RegistroVent4ScreenContent(
        viewModel = viewModel,
        onBackClick = { navController.popBackStack() },
        onClick = { navController.navigate("registro5") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroVent4ScreenContent(
    viewModel: UsuarioViewModel,
    onClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var pesoObjetivo by remember { mutableStateOf("") }
    val (pesoMin, pesoMax) = calcularRangoPesoNormal(viewModel.altura)

    val pesoFloat = pesoObjetivo.toFloatOrNull()
    val esPesoValido = pesoFloat != null && pesoFloat in pesoMin.toFloat()..pesoMax.toFloat()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // ✅ fondo adaptado al tema
            .padding(24.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Atrás",
                tint = MaterialTheme.colorScheme.onBackground // ✅ ícono adaptado al tema
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Establece tu peso objetivo",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground, // ✅ texto adaptado al tema
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Según tu altura, tu peso saludable está entre $pesoMin kg y $pesoMax kg",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground, // ✅ texto adaptado al tema
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = pesoObjetivo,
                onValueChange = { pesoObjetivo = it },
                label = { Text("Peso objetivo (kg)") },
                isError = pesoObjetivo.isNotBlank() && !esPesoValido,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    //placeholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            )

            if (pesoObjetivo.isNotBlank() && !esPesoValido) {
                Text(
                    text = "Ingresa un peso válido entre $pesoMin y $pesoMax kg",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Button(
            onClick = {
                viewModel.pesoObjetivo = pesoFloat ?: 0f
                onClick()
            },
            enabled = esPesoValido,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .fillMaxWidth(0.5f)
        ) {
            Text("Continuar")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegistroVent4ScreenPreview() {
    FrontEndProyectoAppTheme {
        val context = LocalContext.current.applicationContext as Application
        val viewModel: UsuarioViewModel = viewModel(
            factory = ViewModelProvider.AndroidViewModelFactory(context)
        )
        RegistroVent4ScreenContent(viewModel = viewModel)
    }
}

fun calcularRangoPesoNormal(alturaCm: Float): Pair<Int, Int> {
    val alturaM = alturaCm / 100f
    val pesoMin = 18.5 * (alturaM * alturaM)
    val pesoMax = 24.9 * (alturaM * alturaM)
    return Pair(pesoMin.toInt(), pesoMax.toInt())
}