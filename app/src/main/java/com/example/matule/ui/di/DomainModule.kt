package com.example.matule.ui.di

import com.example.data.ui.presentation.feature.auth.datasource.AuthApiService
import com.example.data.ui.presentation.feature.cart.datasource.CartApiService
import com.example.data.ui.presentation.feature.main.datasourse.MainApiService
import com.example.domain.ui.presentation.feature.auth.interactor.AuthInteractor
import com.example.domain.ui.presentation.feature.auth.repository.AuthRepository
import com.example.domain.ui.presentation.feature.cart.interactor.CartInteractor
import com.example.domain.ui.presentation.feature.cart.repository.CartRepository
import com.example.domain.ui.presentation.feature.main.interactor.MainInteractor
import com.example.domain.ui.presentation.feature.main.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DomainModule {

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService{
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMainApiService(retrofit: Retrofit): MainApiService {
        return retrofit.create(MainApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCartApiService(retrofit: Retrofit): CartApiService{
        return retrofit.create(CartApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMainInteractor(repository: MainRepository): MainInteractor{
        return MainInteractor(repository)
    }

    @Provides
    @Singleton
    fun provideAuthInteractor(
        repository: AuthRepository
    ): AuthInteractor{
        return AuthInteractor(repository)
    }

    @Provides
    @Singleton
    fun provideCartInteractor(
        repository: CartRepository
    ): CartInteractor{
        return CartInteractor(repository)
    }

}