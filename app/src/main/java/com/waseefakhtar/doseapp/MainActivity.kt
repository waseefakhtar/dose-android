package com.waseefakhtar.doseapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.waseefakhtar.doseapp.analytics.AnalyticsEvents
import com.waseefakhtar.doseapp.analytics.AnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DoseApp(analyticsHelper = analyticsHelper)
        }
        parseIntent(intent)
    }

    private fun parseIntent(intent: Intent?) {
        val isMedicationNotification = intent?.getBooleanExtra(MEDICATION_NOTIFICATION, false) ?: false
        if (isMedicationNotification) {
            analyticsHelper.logEvent(AnalyticsEvents.REMINDER_NOTIFICATION_CLICKED)
        }
    }
}
