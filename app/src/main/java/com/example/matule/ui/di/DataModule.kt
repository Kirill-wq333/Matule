package com.example.matule.ui.di

import com.example.data.ui.presentation.feature.arrivals.datasource.ArrivalsApiService
import com.example.data.ui.presentation.feature.auth.datasource.AuthApiService
import com.example.data.ui.presentation.feature.cart.datasource.CartApiService
import com.example.data.ui.presentation.feature.favorite.datasource.FavoriteApiService
import com.example.data.ui.presentation.feature.forgot_password.datasource.ForgotPasswordApiService
import com.example.data.ui.presentation.feature.notification.datasource.NotificationApiService
import com.example.data.ui.presentation.feature.orders.datasource.OrdersApiService
import com.example.data.ui.presentation.feature.popular.datasource.PopularApiService
import com.example.data.ui.presentation.feature.profile.datasource.ProfileApiService
import com.example.data.ui.presentation.feature.register.datasource.RegisterApiService
import com.example.data.ui.presentation.feature.sidemenu.datasource.SideMenuApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Provides
    @Singleton
    fun provideForgotPasswordApiService(retrofit: Retrofit): ForgotPasswordApiService{
        return retrofit.create(ForgotPasswordApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRegisterApiService(retrofit: Retrofit): RegisterApiService{
        return retrofit.create(RegisterApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideArrivalsApiService(retrofit: Retrofit): ArrivalsApiService{
        return retrofit.create(ArrivalsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOrdersApiService(retrofit: Retrofit): OrdersApiService{
        return retrofit.create(OrdersApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePopularApiService(retrofit: Retrofit): PopularApiService{
        return retrofit.create(PopularApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideFavoriteApiService(retrofit: Retrofit): FavoriteApiService{
        return retrofit.create(FavoriteApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileApiService(retrofit: Retrofit): ProfileApiService{
        return retrofit.create(ProfileApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSideMenuApiService(retrofit: Retrofit): SideMenuApiService{
        return retrofit.create(SideMenuApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNotificationApiService(retrofit: Retrofit): NotificationApiService{
        return retrofit.create(NotificationApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService{
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCartApiService(retrofit: Retrofit): CartApiService{
        return retrofit.create(CartApiService::class.java)
    }

}