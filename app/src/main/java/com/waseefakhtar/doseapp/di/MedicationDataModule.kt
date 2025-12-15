package com.waseefakhtar.doseapp.di

import android.app.Application
import androidx.room.Room
import com.waseefakhtar.doseapp.data.MedicationDatabase
import com.waseefakhtar.doseapp.data.repository.MedicationRepositoryImpl
import com.waseefakhtar.doseapp.domain.repository.MedicationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependency injection module for medication data layer.
 *
 * This app is fully offline - all data stored locally in Room database.
 * No network dependencies required.
 */
@Module
@InstallIn(SingletonComponent::class)
object MedicationDataModule {

    @Provides
    @Singleton
    fun provideMedicationDatabase(app: Application): MedicationDatabase {
        return Room.databaseBuilder(
            app,
            MedicationDatabase::class.java,
            "medication_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMedicationRepository(
        db: MedicationDatabase
    ): MedicationRepository {
        return MedicationRepositoryImpl(
            dao = db.dao
        )
    }
}
