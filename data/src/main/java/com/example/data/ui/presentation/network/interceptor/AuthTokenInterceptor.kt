package com.example.data.ui.presentation.network.interceptor

import com.example.data.ui.presentation.network.annotation.WithAuthorization
import com.example.data.ui.presentation.storage.tokenprovider.TokenProvider
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import javax.inject.Inject

class AuthTokenInterceptor @Inject constructor(
    private val tokenProvider: TokenProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val isWithAuthorization = originalRequest.tag(Invocation::class.java)
            ?.method()
            ?.annotations
            ?.any { it is WithAuthorization } == true

        val isAuthEndpoint = originalRequest.url.toString().contains("/api/auth/")

        val newRequest = if (isWithAuthorization && !isAuthEndpoint) {
            val token = tokenProvider.getToken()
            if (token != null) {
                originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
            } else {
                originalRequest
            }
        } else {
            originalRequest
        }

        return chain.proceed(newRequest)
    }
}