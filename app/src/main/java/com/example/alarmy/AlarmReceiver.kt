package com.example.alarmy
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        // Affichage de Toast pour vérifier que l'alarme sonne
        Toast.makeText(context, "Alarm is ringing!", Toast.LENGTH_LONG).show()

        // Créer un identifiant unique pour chaque notification
        val notificationId = 1

        // Créer un NotificationManager
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Créer un canal de notification pour les versions Android 8.0 et supérieures
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "alarm_channel"
            val channelName = "Alarm Notifications"
            val channelDescription = "Channel for alarm notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Créer une notification
        val notification: Notification = NotificationCompat.Builder(context, "alarm_channel")
            .setContentTitle("Alarme")
            .setContentText("Votre alarme a sonné!")
            .setSmallIcon(R.drawable.baseline_alarm_24)  // Remplacez par une icône de votre choix
            .setPriority(NotificationCompat.PRIORITY_HIGH)  // Assurez-vous qu'elle est bien visible
            .build()

        // Afficher la notification
        notificationManager.notify(notificationId, notification)
    }
}
