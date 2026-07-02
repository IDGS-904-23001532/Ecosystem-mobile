package com.example.ecosystem

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

fun crearCanalNotificacion(context: Context, idCanal: String, nombre: String, descripcion: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(idCanal, nombre, importance).apply {
            description = descripcion
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

fun notificacionDetalle(context: Context, idNotificacion: Int, idCanal: String, title: String, message: String, messageDetail: String) {
    val builder = NotificationCompat.Builder(context, idCanal)
        .setSmallIcon(R.drawable.logo_ecosystem) // Asegúrate que este logo existe en tu carpeta res/drawable
        .setContentTitle(title)
        .setContentText(message)
        .setStyle(NotificationCompat.BigTextStyle().bigText(messageDetail))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        notify(idNotificacion, builder.build())
    }
}