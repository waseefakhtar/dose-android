package com.waseefakhtar.doseapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.waseefakhtar.doseapp.data.entity.MedicationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMedication(medicationEntity: MedicationEntity): Long

    @Delete
    suspend fun deleteMedication(medicationEntity: MedicationEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMedication(medicationEntity: MedicationEntity)

    @Query(
        """
            SELECT *
            FROM medicationentity
        """
    )
    fun getAllMedications(): Flow<List<MedicationEntity>>

    @Query(
        """
            SELECT *
            FROM medicationentity
            WHERE strftime('%Y-%m-%d', medicationTime / 1000, 'unixepoch', 'localtime') = :date
            ORDER BY medicationTime ASC
        """
    )
    fun getMedicationsForDate(date: String): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medicationentity WHERE id = :id")
    suspend fun getMedicationById(id: Long): MedicationEntity?
}
