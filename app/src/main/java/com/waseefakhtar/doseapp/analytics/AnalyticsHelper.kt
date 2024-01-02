package com.waseefakhtar.doseapp.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.extension.toFormattedDateString
import java.time.LocalDateTime

class AnalyticsHelper private constructor(context: Context) {

    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    companion object {
        @Volatile
        private var instance: AnalyticsHelper? = null

        fun getInstance(context: Context): AnalyticsHelper {
            return instance ?: synchronized(this) {
                instance ?: AnalyticsHelper(context).also { instance = it }
            }
        }
    }

    fun logEvent(eventName: String, params: Bundle? = null) {
        firebaseAnalytics.logEvent(eventName, params)
    }

    fun trackNotificationShown(medication: Medication) {
        val params = Bundle()
        params.putString("medication_time", medication.medicationTime.toFormattedDateString())
        params.putString("medication_end_date", medication.endDate.toFormattedDateString())
        params.putString("notification_time", LocalDateTime.now().toFormattedDateString())
        logEvent(AnalyticsEvents.MEDICATION_NOTIFICATION_SHOWN, params)
    }

    fun trackNotificationScheduled(medication: Medication) {
        val params = Bundle()
        params.putString("medication_time", medication.medicationTime.toFormattedDateString())
        params.putString("medication_end_date", medication.endDate.toFormattedDateString())
        params.putString("notification_time", LocalDateTime.now().toFormattedDateString())
        logEvent(AnalyticsEvents.MEDICATION_NOTIFICATION_SCHEDULED, params)
    }
}
