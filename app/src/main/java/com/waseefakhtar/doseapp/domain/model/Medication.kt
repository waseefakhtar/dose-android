package com.waseefakhtar.doseapp.domain.model

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import com.waseefakhtar.doseapp.util.TimesOfDay
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Medication(
    val id: Int,
    val name: String,
    val dosage: Int,
    val recurrence: String,
    val endDate: Date,
    val timesOfDay: List<TimesOfDay>,
) : Parcelable

class AssetParamType : NavType<Medication>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Medication? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): Medication {
        return Gson().fromJson(value, Medication::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: Medication) {
        bundle.putParcelable(key, value)
    }
}
