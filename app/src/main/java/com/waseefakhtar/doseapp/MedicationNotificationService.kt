package com.waseefakhtar.doseapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.waseefakhtar.doseapp.analytics.AnalyticsHelper
import com.waseefakhtar.doseapp.domain.model.Medication

class MedicationNotificationService(
    private val context: Context
) {

    fun scheduleNotification(medication: Medication, analyticsHelper: AnalyticsHelper) {
        val intent = Intent(context, MedicationNotificationReceiver::class.java)
        intent.putExtra(MEDICATION_INTENT, medication)

        // TODO: Replace medication.hashCode() with medication.id when medication.id is fixed.
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            medication.hashCode(),
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val alarmService = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = medication.medicationTime.time

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                alarmService.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    pendingIntent
                )
            } catch (exception: SecurityException) {
                FirebaseCrashlytics.getInstance().recordException(exception)
            }
        }

        analyticsHelper.trackNotificationScheduled(medication)
    }

    companion object {
        const val MEDICATION_CHANNEL_ID = "medication_channel"
    }
}
