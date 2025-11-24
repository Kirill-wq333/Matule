package com.example.data.ui.presentation.storage.di

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.data.ui.presentation.storage.preferences.AppPreferencesImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
    
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAppPreferences(dataStore: DataStore<Preferences>): AppPreferencesImpl {
        return AppPreferencesImpl(dataStore)
    }

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            migrations = listOf()
        ) { context.preferencesDataStoreFile("app_preferences") }
    }
    
}