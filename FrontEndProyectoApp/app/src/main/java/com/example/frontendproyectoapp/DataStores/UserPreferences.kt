package com.example.frontendproyectoapp.DataStores

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// Declaración del DataStore a nivel de Context (recomendación oficial)
val Context.dataStore by preferencesDataStore(name = "user_prefs")

object UserPreferences {
    private val USER_ID = longPreferencesKey("idUsuario")
    private val USER_CORREO = stringPreferencesKey("correo")
    private val IS_LOGGED_IN = booleanPreferencesKey("isLoggedIn")

    private val IS_DARK_THEME = booleanPreferencesKey("isDarkTheme")

    suspend fun guardarTema(context: Context, isDark: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_DARK_THEME] = isDark
        }
    }

    fun obtenerTema(context: Context): Flow<Boolean> =
        context.dataStore.data.map { prefs ->
            prefs[IS_DARK_THEME] ?: false // false = tema claro por defecto
        }

    // ----------------- Métodos para guardar -----------------
        suspend fun guardarIdUsuario(context: Context, idUsuario: Long) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = idUsuario
        }
    }

    suspend fun guardarCorreoUsuario(context: Context, correo: String) {
        context.dataStore.edit { prefs -> prefs[USER_CORREO] = correo }
    }

    suspend fun guardarSesion(context: Context, loggedIn: Boolean) {
        context.dataStore.edit { prefs -> prefs[IS_LOGGED_IN] = loggedIn }
    }

    // ----------------- Métodos para obtener en Flow -----------------

    fun obtenerIdUsuario(context: Context): Flow<Long?> {
        return context.dataStore.data.map { prefs ->
            prefs[USER_ID]
        }
    }

    fun obtenerCorreoUsuario(context: Context): Flow<String?> =
        context.dataStore.data.map { it[USER_CORREO] }

    fun obtenerSesion(context: Context): Flow<Boolean> =
        context.dataStore.data.map { it[IS_LOGGED_IN] ?: false }

    // ----------------- Métodos suspendidos para obtener valores directos -----------------

    suspend fun obtenerIdUsuarioActual(context: Context): Long? {
        return context.dataStore.data
            .map { prefs -> prefs[USER_ID] }
            .first() // suspende hasta devolver el primer valor
    }

    suspend fun obtenerCorreoUsuarioActual(context: Context): String? =
        context.dataStore.data.map { it[USER_CORREO] }.first()

    suspend fun obtenerSesionActual(context: Context): Boolean =
        context.dataStore.data.map { it[IS_LOGGED_IN] ?: false }.first()

    // ----------------- Limpiar datos -----------------

    suspend fun limpiarDatos(context: Context) {
        context.dataStore.edit { it.clear() }
    }
}
