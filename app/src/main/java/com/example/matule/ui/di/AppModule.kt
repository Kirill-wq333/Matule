package com.example.matule.ui.di

import com.example.data.ui.presentation.storage.preferences.AppPreferencesImpl
import com.example.data.ui.presentation.storage.tokenprovider.TokenProvider
import com.example.matule.ui.manage.AppStartManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideAppStartManager(
        tokenProvider: TokenProvider,
        appPreferences: AppPreferencesImpl
    ): AppStartManager {
        return AppStartManager(tokenProvider, appPreferences)
    }

    @Provides
    @Singleton
    fun provideTokenProvider(appPreferences: AppPreferencesImpl): TokenProvider {
        return TokenProvider(appPreferences)
    }

}