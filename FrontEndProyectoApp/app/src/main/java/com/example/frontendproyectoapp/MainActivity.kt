package com.example.frontendproyectoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.frontendproyectoapp.navigation.AppNavigation
import com.example.frontendproyectoapp.screen.RegistroUsuarioScreen
import com.example.frontendproyectoapp.viewModel.RegistroViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // RegistroUsuarioScreen()

            // Crear un solo viewModel compartido para todo el flujo de registro
            val registroViewModel = remember { RegistroViewModel() }
            val navController = rememberNavController()
            AppNavigation(
                navController = navController,
                viewModel = registroViewModel
            )


        }
    }
}
