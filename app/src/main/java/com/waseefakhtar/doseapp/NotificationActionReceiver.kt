package com.waseefakhtar.doseapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            // TODO: Update notification action from "Take now" to "Taken" and update DB.
        }
    }
}
