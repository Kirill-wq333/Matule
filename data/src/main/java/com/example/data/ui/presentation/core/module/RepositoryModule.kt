package com.example.data.ui.presentation.core.module

import android.content.SharedPreferences
import com.example.data.ui.presentation.feature.auth.AuthRepositoryImpl
import com.example.data.ui.presentation.feature.auth.datasource.AuthApiService
import com.example.data.ui.presentation.storage.tokenprovider.TokenProvider
import com.example.domain.ui.presentation.feature.auth.repository.AuthRepository
import android.content.Context
import com.example.data.ui.presentation.feature.cart.CartRepositoryImpl
import com.example.data.ui.presentation.feature.cart.datasource.CartApiService
import com.example.data.ui.presentation.feature.main.MainRepositoryImpl
import com.example.data.ui.presentation.feature.main.datasourse.MainApiService
import com.example.data.ui.presentation.storage.preferences.AppPreferencesImpl
import com.example.domain.ui.presentation.feature.cart.repository.CartRepository
import com.example.domain.ui.presentation.feature.main.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        apiService: AuthApiService,
        sharedPreferences: SharedPreferences,
        @ApplicationContext context: Context,
        tokenProvider: TokenProvider
    ): AuthRepository = AuthRepositoryImpl(apiService,tokenProvider,sharedPreferences,context)

    @Provides
    @Singleton
    fun provideCartRepository(
        apiService: CartApiService,
        mainRepository: MainRepository,
        tokenProvider: TokenProvider,
        appPreferences: AppPreferencesImpl
    ): CartRepository = CartRepositoryImpl(apiService,mainRepository, tokenProvider,appPreferences)

    @Provides
    @Singleton
    fun provideMainRepository(
        apiService: MainApiService
    ): MainRepository = MainRepositoryImpl(apiService)
}