package com.waseefakhtar.doseapp

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.waseefakhtar.doseapp.domain.model.Medication

class MedicationNotificationService(
    private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun scheduleNotification(medication: Medication) {
        val intent = Intent(context, MedicationNotificationReceiver::class.java)
        intent.putExtra(MEDICATION_INTENT, medication)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            medication.id?.toInt() ?: medication.hashCode(),
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val alarmService = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = medication.date.time

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                alarmService.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    pendingIntent
                )
            } catch (exception: SecurityException) {
                // TODO: Log exception in Crashlytics.
            }
        }
    }

    fun showNotification() {
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val receiverIntent = Intent(context, NotificationActionReceiver::class.java)
        val takenPendingIntent = PendingIntent.getBroadcast(
            context,
            2,
            receiverIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        // TODO: Add action.
        val notification = NotificationCompat.Builder(context, MEDICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_dose)
            .setContentTitle("Medication Reminder")
            .setContentText("It is time to take your medication. Open the app to log it.")
            .setContentIntent(activityPendingIntent)
            /*.addAction(
                R.drawable.doctor,
                "Take now",
                takenPendingIntent)*/
            .build()

        // TODO: Use medication id as notification id.
        notificationManager.notify(1, notification)
    }

    companion object {
        const val MEDICATION_CHANNEL_ID = "medication_channel"
    }
}