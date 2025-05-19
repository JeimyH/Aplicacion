package com.example.frontendproyectoapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.frontendproyectoapp.screen.RegistroVent1Screen
import com.example.frontendproyectoapp.screen.RegistroVent2Screen
import com.example.frontendproyectoapp.screen.RegistroVent3Screen
import com.example.frontendproyectoapp.screen.RegistroVent4Screen
import com.example.frontendproyectoapp.screen.RegistroVent5Screen
import com.example.frontendproyectoapp.screen.RegistroVent6Screen
import com.example.frontendproyectoapp.screen.RegistroVent7Screen
import com.example.frontendproyectoapp.screen.RegistroVent8Screen
import com.example.frontendproyectoapp.viewModel.RegistroViewModel

@Composable
fun AppNavigation(navController: NavHostController, viewModel: RegistroViewModel) {
    NavHost(navController = navController, startDestination = "registro1") {
        composable("registro1") { RegistroVent1Screen(navController) }
        composable("registro2") { RegistroVent2Screen(navController, viewModel) }
        composable("registro3") { RegistroVent3Screen(navController, viewModel) }
        composable("registro4") { RegistroVent4Screen(navController, viewModel) }
        composable("registro5") { RegistroVent5Screen(navController) }
        composable("registro6") { RegistroVent6Screen(navController) }
        composable("registro7") { RegistroVent7Screen(navController) }
        composable("registro8") { RegistroVent8Screen(navController, viewModel) }
        // composable("inicio") { InicioScreen(navController) }
    }
}