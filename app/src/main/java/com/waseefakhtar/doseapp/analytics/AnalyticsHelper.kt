package com.waseefakhtar.doseapp.analytics

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.extension.toFormattedDateString
import java.util.Date

private const val MEDICATION_TIME = "medication_time"
private const val MEDICATION_END_DATE = "medication_end_date"
private const val NOTIFICATION_TIME = "notification_time"

class AnalyticsHelper(
    context: Context
) {
    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    fun trackNotificationShown(medication: Medication) {
        val params = bundleOf(
            MEDICATION_TIME to medication.medicationTime.toFormattedDateString(),
            MEDICATION_END_DATE to medication.endDate.toFormattedDateString(),
            NOTIFICATION_TIME to Date().toFormattedDateString()
        )
        logEvent(AnalyticsEvents.MEDICATION_NOTIFICATION_SHOWN, params)
    }

    fun trackNotificationScheduled(medication: Medication) {
        val params = bundleOf(
            MEDICATION_TIME to medication.medicationTime.toFormattedDateString(),
            MEDICATION_END_DATE to medication.endDate.toFormattedDateString(),
            NOTIFICATION_TIME to Date().toFormattedDateString()
        )
        logEvent(AnalyticsEvents.MEDICATION_NOTIFICATION_SCHEDULED, params)
    }

    fun logEvent(eventName: String, params: Bundle? = null) {
        firebaseAnalytics.logEvent(eventName, params)
    }
}
