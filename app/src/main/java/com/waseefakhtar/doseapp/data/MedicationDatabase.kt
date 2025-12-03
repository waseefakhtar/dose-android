package com.waseefakhtar.doseapp.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import com.waseefakhtar.doseapp.data.entity.MedicationEntity

@Database(
    entities = [MedicationEntity::class],
    version = 5,
    autoMigrations = [
        AutoMigration(from = 3, to = 4, spec = MedicationDatabase.AutoMigration3To4::class),
        AutoMigration(from = 4, to = 5)
    ]
)
@TypeConverters(Converters::class)
abstract class MedicationDatabase : RoomDatabase() {

    abstract val dao: MedicationDao
    @DeleteColumn(tableName = "MedicationEntity", columnName = "timesOfDay")
    @RenameColumn(tableName = "MedicationEntity", fromColumnName = "date", toColumnName = "medicationTime")
    class AutoMigration3To4 : AutoMigrationSpec
}
