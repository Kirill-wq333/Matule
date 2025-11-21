package com.example.matule.ui.di

import android.content.Context
import com.example.data.ui.presentation.feature.auth.AuthRepositoryImpl
import com.example.data.ui.presentation.feature.auth.datasource.AuthApiService
import com.example.domain.ui.presentation.feature.auth.interactor.AuthInteractor
import com.example.domain.ui.presentation.feature.auth.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DomainModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://localhost:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService{
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthInteractor(
        repository: AuthRepository
    ): AuthInteractor{
        return AuthInteractor(repository)
    }

}