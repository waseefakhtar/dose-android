package com.waseefakhtar.doseapp.di

import android.content.Context
import com.waseefakhtar.doseapp.analytics.AnalyticsHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsHelperModule {

    @Provides
    @Singleton
    fun provideAnalyticsHelper(
        @ApplicationContext context: Context,
    ): AnalyticsHelper {
        return AnalyticsHelper(context = context)
    }
}
