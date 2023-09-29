package com.waseefakhtar.doseapp.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.extension.toFormattedString
import java.util.Date

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
        params.putString("medication_times_of_day", medication.timesOfDay.toString())
        params.putString("medication_notification_date", medication.date.toFormattedString())
        params.putString("medication_end_date", medication.endDate.toFormattedString())
        params.putString("notification_time", Date().toFormattedString())
        logEvent(AnalyticsEvents.MEDICATION_NOTIFICATION_SHOWN, params)
    }

    fun trackNotificationScheduled(medication: Medication) {
        val params = Bundle()
        params.putString("medication_times_of_day", medication.timesOfDay.toString())
        params.putString("medication_notification_date", medication.date.toFormattedString())
        params.putString("medication_end_date", medication.endDate.toFormattedString())
        params.putString("notification_time", Date().toFormattedString())
        logEvent(AnalyticsEvents.MEDICATION_NOTIFICATION_SCHEDULED, params)
    }
}

