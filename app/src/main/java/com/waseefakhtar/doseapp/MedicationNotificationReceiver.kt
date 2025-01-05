package com.waseefakhtar.doseapp

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.waseefakhtar.doseapp.analytics.AnalyticsHelper
import com.waseefakhtar.doseapp.domain.model.Medication
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val MEDICATION_INTENT = "medication_intent"
const val MEDICATION_NOTIFICATION = "medication_notification"

@AndroidEntryPoint
class MedicationNotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            intent?.getParcelableExtra<Medication>(MEDICATION_INTENT)?.let { medication ->
                showNotification(it, medication)
            }
        }
    }

    private fun showNotification(context: Context, medication: Medication) {
        // Create deep link intent for notification click
        val activityIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            data = Uri.parse("doseapp://medication/${medication.id}")
        }

        val activityPendingIntent = PendingIntent.getActivity(
            context,
            medication.id.toInt(),
            activityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(
            context,
            MedicationNotificationService.MEDICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_dose)
            .setContentTitle(context.getString(R.string.medication_reminder))
            .setContentText(context.getString(R.string.medication_reminder_time, medication.name))
            .setAutoCancel(true)
            .setContentIntent(activityPendingIntent)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(medication.id.toInt(), notification)

        analyticsHelper.trackNotificationShown(medication)
    }
}
