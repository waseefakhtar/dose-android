package com.waseefakhtar.doseapp.analytics

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.waseefakhtar.doseapp.domain.model.Medication

/**
 * Analytics helper for personal use.
 * Logs events to logcat instead of sending to external services.
 * All analytics are local-only for privacy.
 */
class AnalyticsHelper(
    context: Context
) {
    private val crashLogger = LocalCrashLogger(context)

    /**
     * Track when a medication notification is shown
     * This is now a no-op or logs locally
     */
    fun trackNotificationShown(medication: Medication) {
        logEvent(AnalyticsEvents.MEDICATION_NOTIFICATION_SHOWN, null)
    }

    /**
     * Track when a medication notification is scheduled
     * This is now a no-op or logs locally
     */
    fun trackNotificationScheduled(medication: Medication) {
        logEvent(AnalyticsEvents.MEDICATION_NOTIFICATION_SCHEDULED, null)
    }

    /**
     * Log an event locally (no external tracking)
     * @param eventName Name of the event
     * @param params Optional parameters (ignored for privacy)
     */
    fun logEvent(eventName: String, params: Bundle? = null) {
        // Local logging only - no external analytics
        Log.d(TAG, "Event: $eventName")
    }

    /**
     * Get the local crash logger for recording exceptions
     */
    fun getCrashLogger(): LocalCrashLogger = crashLogger

    companion object {
        private const val TAG = "AnalyticsHelper"
    }
}
