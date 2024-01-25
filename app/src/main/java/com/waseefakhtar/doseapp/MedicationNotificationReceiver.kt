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
        val activityIntent = Intent(context, MainActivity::class.java)
        activityIntent.putExtra(MEDICATION_NOTIFICATION, true)
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
            .setContentTitle(context.getString(R.string.medication_reminder))
            .setContentText(context.getString(R.string.medication_reminder_time, medication.name))
            .setContentIntent(activityPendingIntent)
            /*.addAction(
                R.drawable.doctor,
                "Take now",
                takenPendingIntent)*/
            .build()

        // TODO: Use medication id as notification id.
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(medication.hashCode(), notification)

        analyticsHelper.trackNotificationShown(medication)
    }
}
