package com.example.data.ui.presentation.network.di

import com.example.data.ui.presentation.network.interceptor.AuthTokenInterceptor
import com.example.data.ui.presentation.storage.tokenprovider.TokenProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideTokenInterceptor(tokenProvider: TokenProvider): AuthTokenInterceptor {
        return AuthTokenInterceptor(tokenProvider)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthTokenInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(createLoggingInterceptor())
            .addInterceptor(createDebugInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private fun createLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private fun createDebugInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()

            request.headers.forEach { header ->
                println("   ${header.first}: ${header.second}")
            }

            request.body?.let { body ->
                if (body.contentType()?.toString()?.contains("application/json") == true) {
                    val buffer = Buffer()
                    body.writeTo(buffer)
                }
            }

            val response = chain.proceed(request)

            response.headers.forEach { header ->
                println("   ${header.first}: ${header.second}")
            }

            val responseBody = response.body
            val source = responseBody?.source()
            source?.request(Long.MAX_VALUE)
            val buffer = source?.buffer?.clone()
            val responseBodyString = buffer?.readUtf8()


            response.newBuilder()
                .body(ResponseBody.create(responseBody?.contentType(), responseBodyString ?: ""))
                .build()
        }
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://103.90.75.40:3005/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}