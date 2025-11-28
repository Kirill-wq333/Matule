package com.example.data.ui.presentation.feature.sidemenu.datasource

import com.example.data.ui.presentation.feature.sidemenu.dto.response.LogoutResponse
import com.example.data.ui.presentation.network.annotation.WithAuthorization
import retrofit2.http.POST

interface SideMenuApiService {

    @WithAuthorization
    @POST("auth/logout")
    suspend fun logout(): LogoutResponse

}