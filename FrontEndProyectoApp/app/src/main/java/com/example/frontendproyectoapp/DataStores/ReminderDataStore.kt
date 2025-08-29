package com.example.frontendproyectoapp.DataStores

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.frontendproyectoapp.notificaciones.AlarmHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// Top-level
val Context.reminderDataStore by preferencesDataStore("reminder_prefs")

object ReminderDataStore {

    fun keyEnabled(userId: Long, tipo: String) = booleanPreferencesKey("reminder_${userId}_${tipo}_enabled")
    fun keyTime(userId: Long, tipo: String) = stringPreferencesKey("reminder_${userId}_${tipo}_time")

    suspend fun saveReminder(context: Context, userId: Long, tipo: String, enabled: Boolean, time: String) {
        context.reminderDataStore.edit { prefs ->
            prefs[keyEnabled(userId, tipo)] = enabled
            prefs[keyTime(userId, tipo)] = time
        }
    }

    fun getReminderFlow(context: Context, userId: Long, tipo: String): Flow<Pair<Boolean, String>> {
        return context.reminderDataStore.data.map { prefs ->
            val enabled = prefs[keyEnabled(userId, tipo)] ?: false
            val time = prefs[keyTime(userId, tipo)] ?: "08:00"
            Pair(enabled, time)
        }
    }

    // 🔹 Reprograma todas las alarmas activas al iniciar o al reiniciar el dispositivo
    fun reprogramAllReminders(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            // Lista de tipos de recordatorios
            val tipos = listOf("agua", "desayuno", "almuerzo", "cena", "snack mañana", "snack tarde")

            // 🚀 Obtener ID del usuario logueado
            val userId = UserPreferences.obtenerIdUsuarioActual(context) ?: return@launch

            val prefs = context.reminderDataStore.data.first() // leer último estado

            tipos.forEach { tipo ->
                val enabled = prefs[keyEnabled(userId, tipo)] ?: false
                val time = prefs[keyTime(userId, tipo)] ?: "08:00"

                if (enabled) {
                    val (hour, minute) = time.split(":").map { it.toInt() }
                    AlarmHelper.scheduleDailyReminder(context, userId, tipo, hour, minute)
                } else {
                    AlarmHelper.cancelReminder(context, userId, tipo)
                }
            }
        }
    }
}
