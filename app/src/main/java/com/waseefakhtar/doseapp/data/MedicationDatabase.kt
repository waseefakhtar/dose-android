package com.waseefakhtar.doseapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.waseefakhtar.doseapp.data.entity.MedicationEntity

@Database(
    entities = [MedicationEntity::class],
    version = 1
)
abstract class MedicationDatabase: RoomDatabase() {

    abstract val dao: MedicationDao
}