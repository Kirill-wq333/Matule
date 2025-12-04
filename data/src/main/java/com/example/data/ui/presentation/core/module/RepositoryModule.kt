package com.example.data.ui.presentation.core.module

import android.content.SharedPreferences
import com.example.data.ui.presentation.feature.auth.AuthRepositoryImpl
import com.example.data.ui.presentation.feature.auth.datasource.AuthApiService
import com.example.data.ui.presentation.storage.tokenprovider.TokenProvider
import com.example.domain.ui.presentation.feature.auth.repository.AuthRepository
import com.example.data.ui.presentation.feature.arrivals.ArrivalsRepositoryImpl
import com.example.data.ui.presentation.feature.arrivals.datasource.ArrivalsApiService
import com.example.data.ui.presentation.feature.cart.CartRepositoryImpl
import com.example.data.ui.presentation.feature.cart.datasource.CartApiService
import com.example.data.ui.presentation.feature.catalog.CatalogRepositoryImpl
import com.example.data.ui.presentation.feature.catalog.datasource.CatalogApiService
import com.example.data.ui.presentation.feature.favorite.FavoriteRepositoryImpl
import com.example.data.ui.presentation.feature.favorite.datasource.FavoriteApiService
import com.example.data.ui.presentation.feature.forgot_password.ForgotPasswordRepositoryImpl
import com.example.data.ui.presentation.feature.forgot_password.datasource.ForgotPasswordApiService
import com.example.data.ui.presentation.feature.notification.NotificationRepositoryImpl
import com.example.data.ui.presentation.feature.notification.datasource.NotificationApiService
import com.example.data.ui.presentation.feature.orders.OrdersRepositoryImpl
import com.example.data.ui.presentation.feature.orders.datasource.OrdersApiService
import com.example.data.ui.presentation.feature.popular.PopularRepositoryImpl
import com.example.data.ui.presentation.feature.popular.datasource.PopularApiService
import com.example.data.ui.presentation.feature.profile.ProfileRepositoryImpl
import com.example.data.ui.presentation.feature.profile.datasource.ProfileApiService
import com.example.data.ui.presentation.feature.register.RegisterRepositoryImpl
import com.example.data.ui.presentation.feature.register.datasource.RegisterApiService
import com.example.data.ui.presentation.feature.sidemenu.SideMenuRepositoryImpl
import com.example.data.ui.presentation.feature.sidemenu.datasource.SideMenuApiService
import com.example.data.ui.presentation.storage.preferences.AppPreferencesImpl
import com.example.domain.ui.presentation.feature.arrivals.repository.ArrivalsRepository
import com.example.domain.ui.presentation.feature.cart.repository.CartRepository
import com.example.domain.ui.presentation.feature.catalog.repository.CatalogRepository
import com.example.domain.ui.presentation.feature.favorite.repository.FavoriteRepository
import com.example.domain.ui.presentation.feature.forgot_password.repository.ForgotPasswordRepository
import com.example.domain.ui.presentation.feature.notification.repository.NotificationRepository
import com.example.domain.ui.presentation.feature.orders.repository.OrdersRepository
import com.example.domain.ui.presentation.feature.popular.repository.PopularRepository
import com.example.domain.ui.presentation.feature.profile.repository.ProfileRepository
import com.example.domain.ui.presentation.feature.register.repository.RegisterRepository
import com.example.domain.ui.presentation.feature.sidemenu.repository.SideMenuRepository
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
    fun provideForgotPasswordRepository(
        apiService: ForgotPasswordApiService
    ): ForgotPasswordRepository = ForgotPasswordRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun provideRegisterRepository(
        apiService: RegisterApiService,
        tokenProvider: TokenProvider,
        sharedPreferences: SharedPreferences
    ) : RegisterRepository = RegisterRepositoryImpl(apiService, tokenProvider, sharedPreferences)

    @Provides
    @Singleton
    fun provideAuthRepository(
        apiService: AuthApiService,
        sharedPreferences: SharedPreferences,
        cartRepository: CartRepository,
        tokenProvider: TokenProvider,
        appPreferencesImpl: AppPreferencesImpl
    ): AuthRepository = AuthRepositoryImpl(apiService,tokenProvider,sharedPreferences,appPreferencesImpl)

    @Provides
    @Singleton
    fun provideCartRepository(
        popularRepository: PopularRepository,
        apiService: CartApiService,
        tokenProvider: TokenProvider,
        appPreferences: AppPreferencesImpl
    ): CartRepository = CartRepositoryImpl(popularRepository, apiService, tokenProvider, appPreferences)

    @Provides
    @Singleton
    fun provideArrivalsRepository(
        apiService: ArrivalsApiService
    ): ArrivalsRepository = ArrivalsRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun provideCatalogRepository(
        apiService: CatalogApiService
    ): CatalogRepository = CatalogRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun provideFavoriteRepository(
        apiService: FavoriteApiService
    ): FavoriteRepository = FavoriteRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun provideNotificationRepository(
        apiService: NotificationApiService
    ): NotificationRepository = NotificationRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun provideOrdersRepository(
        apiService: OrdersApiService
    ): OrdersRepository = OrdersRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun providePopularRepository(
        apiService: PopularApiService
    ): PopularRepository = PopularRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun provideProfileRepository(
        apiService: ProfileApiService
    ): ProfileRepository = ProfileRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun provideSideMenuRepository(
        apiService: SideMenuApiService,
        appPreferences: AppPreferencesImpl
    ): SideMenuRepository = SideMenuRepositoryImpl(apiService)
}