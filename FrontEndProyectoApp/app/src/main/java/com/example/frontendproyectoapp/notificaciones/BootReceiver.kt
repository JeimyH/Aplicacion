package com.example.frontendproyectoapp.notificaciones

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.frontendproyectoapp.DataStores.ReminderDataStore
import com.example.frontendproyectoapp.DataStores.UserPreferences
import com.example.frontendproyectoapp.DataStores.reminderDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action
        if (action == Intent.ACTION_BOOT_COMPLETED || action == Intent.ACTION_MY_PACKAGE_REPLACED) {
            reprogramAllReminders(context)
        }
    }

    private fun reprogramAllReminders(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Lista de tipos que manejas en tu app
                val tipos = listOf("agua", "desayuno", "almuerzo", "cena", "snack mañana", "snack tarde")

                // Obtiene el userId actual (si no hay usuario logueado no reprograma nada)
                val userId = UserPreferences.obtenerIdUsuarioActual(context) ?: return@launch

                // Obtén las preferencias actuales de recordatorios
                val prefs = context.reminderDataStore.data.first()

                tipos.forEach { tipo ->
                    val enabled = prefs[ReminderDataStore.keyEnabled(userId, tipo)] ?: false
                    val time = prefs[ReminderDataStore.keyTime(userId, tipo)] ?: "08:00"

                    if (enabled) {
                        val (h, m) = time.split(":").map { it.toInt() }
                        // Usa SIEMPRE la misma lógica de AlarmHelper
                        AlarmHelper.scheduleDailyReminder(context, userId, tipo, h, m)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}