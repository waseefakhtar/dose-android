package com.waseefakhtar.doseapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.waseefakhtar.doseapp.data.entity.MedicationEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface MedicationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medicationEntity: MedicationEntity)

    @Delete
    suspend fun deleteMedication(medicationEntity: MedicationEntity)

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
            WHERE endDate > :date
        """
    )
    fun getMedicationsForDate(date: Date): Flow<List<MedicationEntity>>
}
