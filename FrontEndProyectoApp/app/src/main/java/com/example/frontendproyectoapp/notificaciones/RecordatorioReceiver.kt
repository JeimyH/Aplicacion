package com.example.frontendproyectoapp.notificaciones

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.frontendproyectoapp.MainActivity
import com.example.frontendproyectoapp.R

class RecordatorioReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val tipo = intent?.getStringExtra("tipo") ?: "recordatorio"
        val userId = intent?.getLongExtra("userId", 0L) ?: 0L
        val hour = intent?.getIntExtra("hour", 8) ?: 8
        val minute = intent?.getIntExtra("minute", 0) ?: 0

        val contentText = when(tipo) {
            "agua" -> "¡Hora de tomar agua!"
            "desayuno" -> "¡Registra tu desayuno!"
            "almuerzo" -> "¡Registra tu almuerzo!"
            "cena" -> "¡Registra tu cena!"
            "snack mañana" -> "¡Registra tu snack de la mañana!"
            "snack tarde" -> "¡Registra tu snack de la tarde!"
            else -> "¡Es hora de tu recordatorio!"
        }

        val tapIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("navigate_to", "recordatorios")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val tapPending = PendingIntent.getActivity(
            context,
            (userId.hashCode() + tipo.hashCode()),
            tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "recordatorios_channel")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Recordatorio")
            .setContentText(contentText)
            .setContentIntent(tapPending)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED) {

            NotificationManagerCompat.from(context).notify(
                ((System.currentTimeMillis() % Int.MAX_VALUE).toInt()), notification
            )
        }

        // 🔁 Si el dispositivo permite exactas, reprogramamos para el día siguiente
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()) {
            AlarmHelper.scheduleDailyReminder(context, userId, tipo, hour, minute)
        }
    }
}

