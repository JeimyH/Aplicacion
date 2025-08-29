package com.example.frontendproyectoapp.notificaciones

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.*

object AlarmHelper {

    private fun getRequestCode(userId: Long, tipo: String): Int {
        val idPart = (userId xor (userId shr 32)).toInt()
        return idPart + tipo.hashCode()
    }

    private fun supportsExact(context: Context): Boolean {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()
    }

    fun scheduleDailyReminder(context: Context, userId: Long, tipo: String, hour: Int, minute: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, RecordatorioReceiver::class.java).apply {
            putExtra("tipo", tipo)
            putExtra("userId", userId)
            putExtra("hour", hour)
            putExtra("minute", minute)
        }

        val requestCode = getRequestCode(userId, tipo)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Calcula la próxima hora
        val now = Calendar.getInstance()
        val next = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(now)) add(Calendar.DAY_OF_MONTH, 1)
        }

        // Cancelar antes de reprogramar para evitar duplicados
        alarmManager.cancel(pendingIntent)

        if (supportsExact(context)) {
            // Exacta: nosotros nos encargamos de reprogramar cada día desde el Receiver
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    next.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, next.timeInMillis, pendingIntent)
            }
        } else {
            // Inexacta repetitiva: el sistema la repite solo
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                next.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }

    fun cancelReminder(context: Context, userId: Long, tipo: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val requestCode = getRequestCode(userId, tipo)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            Intent(context, RecordatorioReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }
}
