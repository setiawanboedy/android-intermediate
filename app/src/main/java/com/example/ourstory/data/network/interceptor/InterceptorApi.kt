package com.example.ourstory.data.network.interceptor

import com.example.ourstory.session.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class InterceptorApi(private val session: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        session.getUser().token.let {
            request.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(request.build())
    }
}