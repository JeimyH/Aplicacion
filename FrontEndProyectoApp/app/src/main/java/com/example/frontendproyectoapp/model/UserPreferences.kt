package com.example.frontendproyectoapp.model

import android.content.Context
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

object UserPreferences {
    private val USER_ID = longPreferencesKey("idUsuario")

    suspend fun guardarIdUsuario(context: Context, idUsuario: Long) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = idUsuario
        }
    }

    fun obtenerIdUsuario(context: Context): Flow<Long?> {
        return context.dataStore.data.map { prefs ->
            prefs[USER_ID]
        }
    }

    suspend fun limpiarDatos(context: Context) {
        context.dataStore.edit { it.clear() }
    }

    suspend fun obtenerIdUsuarioActual(context: Context): Long? {
        return UserPreferences.obtenerIdUsuario(context).firstOrNull()
    }
}
