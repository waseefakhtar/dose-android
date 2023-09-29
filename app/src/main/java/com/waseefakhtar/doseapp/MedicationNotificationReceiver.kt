package com.waseefakhtar.doseapp

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.waseefakhtar.doseapp.analytics.AnalyticsHelper
import com.waseefakhtar.doseapp.domain.model.Medication

const val MEDICATION_INTENT = "medication_intent"
class MedicationNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            intent?.getParcelableExtra<Medication>(MEDICATION_INTENT)?.let { medication ->
                showNotification(it, medication)
            }
        }
    }

    private fun showNotification(context: Context, medication: Medication) {
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val receiverIntent = Intent(context, NotificationActionReceiver::class.java)
        /*val takenPendingIntent = PendingIntent.getBroadcast(
            context,
            2,
            receiverIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )*/

        // TODO: Add action.
        val notification = NotificationCompat.Builder(
            context,
            MedicationNotificationService.MEDICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_dose)
            .setContentTitle("Medication Reminder")
            .setContentText("It is time to take your medication, ${medication.name}. Open the app to log it.")
            .setContentIntent(activityPendingIntent)
            /*.addAction(
                R.drawable.doctor,
                "Take now",
                takenPendingIntent)*/
            .build()

        // TODO: Use medication id as notification id.
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(medication.hashCode(), notification)

        AnalyticsHelper.getInstance(context).trackNotificationScheduled(medication)
    }
}
