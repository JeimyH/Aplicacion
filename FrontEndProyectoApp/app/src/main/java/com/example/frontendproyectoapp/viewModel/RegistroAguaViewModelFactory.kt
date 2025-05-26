package com.example.frontendproyectoapp.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RegistroAguaViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistroAguaViewModel::class.java)) {
            return RegistroAguaViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}





