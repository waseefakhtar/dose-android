package com.waseefakhtar.doseapp.analytics

object AnalyticsEvents {

    const val NOTIFICATION_PERMISSION_DIALOG_SHOWN = "notification_permission_dialog_shown"
    const val NOTIFICATION_PERMISSION_DIALOG_DISMISSED = "notification_permission_dialog_shown"
    const val NOTIFICATION_PERMISSION_DIALOG_ALLOW_CLICKED = "notification_permission_dialog_allow_clicked"
    const val NOTIFICATION_PERMISSION_GRANTED = "notification_permission_granted"
    const val NOTIFICATION_PERMISSION_REFUSED = "notification_permission_refused"

    const val EMPTY_CARD_SHOWN = "empty_card_shown"

    const val ADD_MEDICATION_CLICKED_EMPTY_CARD = "add_medication_clicked_empty_card"
    const val ADD_MEDICATION_CLICKED_DAILY_OVERVIEW = "add_medication_clicked_daily_overview"
    const val ADD_MEDICATION_CLICKED_FAB = "add_medication_clicked_fab"

    const val TAKE_MEDICATION_CLICKED = "take_medication_clicked"

    const val MEDICATION_NOTIFICATION_SHOWN = "medication_notification_shown"
    const val MEDICATION_NOTIFICATION_SCHEDULED = "medication_notification_scheduled"

    const val MEDICATIONS_SAVED = "medications_saved"

    const val MEDICATION_CONFIRM_ON_BACK_CLICKED = "medications_confirm_on_back_clicked"
    const val MEDICATION_CONFIRM_ON_CONFIRM_CLICKED = "medications_confirm_on_confirm_clicked"

    const val ADD_MEDICATION_ON_BACK_CLICKED = "add_medication_on_back_clicked"
    const val ADD_MEDICATION_MEDICATION_VALUE_INVALIDATED = "add_medication_medication_%s_invalidated"
    const val ADD_MEDICATION_NAVIGATING_TO_MEDICATION_CONFIRM = "add_medication_navigating_to_medication_confirm"

    const val REMINDER_NOTIFICATION_CLICKED = "reminder_notification_clicked"

    const val MEDICATION_DETAIL_ON_BACK_CLICKED = "medication_detail_on_back_clicked"
    const val MEDICATION_DETAIL_TAKEN_CLICKED = "medication_detail_taken_clicked"
    const val MEDICATION_DETAIL_SKIPPED_CLICKED = "medication_detail_taken_clicked"
    const val MEDICATION_DETAIL_DONE_CLICKED = "medication_detail_done_clicked"
}
