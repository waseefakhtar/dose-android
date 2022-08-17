package com.waseefakhtar.doseapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.waseefakhtar.doseapp.data.entity.MedicationEntity

@Database(entities = [MedicationEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class MedicationDatabase : RoomDatabase() {

    abstract val dao: MedicationDao
}
