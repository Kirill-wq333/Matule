package com.example.data.ui.presentation.core.module

import com.example.data.ui.presentation.feature.auth.AuthRepositoryImpl
import com.example.domain.ui.presentation.feature.auth.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}