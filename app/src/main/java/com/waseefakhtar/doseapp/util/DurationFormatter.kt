package com.waseefakhtar.doseapp.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import com.waseefakhtar.doseapp.extension.Duration

@Composable
fun formatDurationText(duration: Duration): String {
    val primary =
        pluralStringResource(
            duration.primaryType.pluralResId,
            duration.primary,
            duration.primary,
        )

    return if (duration.remainder != null && duration.remainderType != null) {
        val remainder =
            pluralStringResource(
                duration.remainderType.pluralResId,
                duration.remainder,
                duration.remainder,
            )
        "$primary, $remainder"
    } else {
        primary
    }
}
