package com.waseefakhtar.doseapp.data

import androidx.room.AutoMigration
import androidx.room.ColumnInfo
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import com.waseefakhtar.doseapp.data.entity.MedicationEntity
import java.util.Date

@Database(
    entities = [MedicationEntity::class],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 2, to = 3, spec = MedicationDatabase.AutoMigration::class)
    ]
)
@TypeConverters(Converters::class)
abstract class MedicationDatabase : RoomDatabase() {

    abstract val dao: MedicationDao
    @DeleteColumn(tableName = "MedicationEntity", columnName = "timesOfDay")
    @RenameColumn(tableName = "MedicationEntity", fromColumnName = "date", toColumnName = "medicationTime")
    class AutoMigration : AutoMigrationSpec
}
