package com.example.matule.ui.di

import com.example.domain.ui.presentation.feature.arrivals.interactor.ArrivalsInteractor
import com.example.domain.ui.presentation.feature.arrivals.repository.ArrivalsRepository
import com.example.domain.ui.presentation.feature.auth.interactor.AuthInteractor
import com.example.domain.ui.presentation.feature.auth.repository.AuthRepository
import com.example.domain.ui.presentation.feature.cart.interactor.CartInteractor
import com.example.domain.ui.presentation.feature.cart.repository.CartRepository
import com.example.domain.ui.presentation.feature.catalog.interactor.CatalogInteractor
import com.example.domain.ui.presentation.feature.catalog.repository.CatalogRepository
import com.example.domain.ui.presentation.feature.favorite.interactor.FavoriteInteractor
import com.example.domain.ui.presentation.feature.favorite.repository.FavoriteRepository
import com.example.domain.ui.presentation.feature.forgot_password.interactor.ForgotPasswordInteractor
import com.example.domain.ui.presentation.feature.forgot_password.repository.ForgotPasswordRepository
import com.example.domain.ui.presentation.feature.main.interactor.MainInteractor
import com.example.domain.ui.presentation.feature.notification.interactor.NotificationInteractor
import com.example.domain.ui.presentation.feature.notification.repository.NotificationRepository
import com.example.domain.ui.presentation.feature.orders.interactor.OrdersInteractor
import com.example.domain.ui.presentation.feature.orders.repository.OrdersRepository
import com.example.domain.ui.presentation.feature.popular.interactor.PopularInteractor
import com.example.domain.ui.presentation.feature.popular.repository.PopularRepository
import com.example.domain.ui.presentation.feature.profile.interactor.ProfileInteractor
import com.example.domain.ui.presentation.feature.profile.repository.ProfileRepository
import com.example.domain.ui.presentation.feature.register.interactor.RegisterInteractor
import com.example.domain.ui.presentation.feature.register.repository.RegisterRepository
import com.example.domain.ui.presentation.feature.sidemenu.interactor.SideMenuInteractor
import com.example.domain.ui.presentation.feature.sidemenu.repository.SideMenuRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DomainModule {

    @Provides
    @Singleton
    fun provideForgotPasswordInteractor(
        forgotPasswordRepository: ForgotPasswordRepository
    ): ForgotPasswordInteractor{
        return ForgotPasswordInteractor(forgotPasswordRepository)
    }

    @Provides
    @Singleton
    fun provideRegisterInteractor(
        registerRepository: RegisterRepository
    ): RegisterInteractor{
        return RegisterInteractor(registerRepository)
    }

    @Provides
    @Singleton
    fun provideArrivalsInteractor(
        arrivalsRepository: ArrivalsRepository
    ): ArrivalsInteractor{
        return ArrivalsInteractor(arrivalsRepository)
    }

    @Provides
    @Singleton
    fun provideCatalogInteractor(
        catalogRepository: CatalogRepository
    ): CatalogInteractor{
        return CatalogInteractor(catalogRepository)
    }

    @Provides
    @Singleton
    fun provideFavoriteInteractor(
        favoriteRepository: FavoriteRepository
    ): FavoriteInteractor{
        return FavoriteInteractor(favoriteRepository)
    }

    @Provides
    @Singleton
    fun provideSideMenuInteractor(
        sideMenuRepository: SideMenuRepository,
        authInteractor: AuthInteractor
    ): SideMenuInteractor{
        return SideMenuInteractor(sideMenuRepository, authInteractor)
    }

    @Provides
    @Singleton
    fun provideProfileInteractor(
        profileRepository: ProfileRepository
    ): ProfileInteractor{
        return ProfileInteractor(profileRepository)
    }

    @Provides
    @Singleton
    fun providePopularInteractor(
        popularRepository: PopularRepository
    ): PopularInteractor{
        return PopularInteractor(popularRepository)
    }

    @Provides
    @Singleton
    fun provideNotificationInteractor(
        notificationRepository: NotificationRepository
    ): NotificationInteractor{
        return NotificationInteractor(notificationRepository)
    }

    @Provides
    @Singleton
    fun provideOrdersInteractor(
        ordersRepository: OrdersRepository
    ): OrdersInteractor{
        return OrdersInteractor()
    }

    @Provides
    @Singleton
    fun provideMainInteractor(
        catalogRepository: CatalogRepository,
        arrivalsRepository: ArrivalsRepository,
        popularRepository: PopularRepository,
    ): MainInteractor{
        return MainInteractor(catalogRepository, arrivalsRepository, popularRepository)
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