package com.example.ourstory.data.network.interceptor

import com.example.ourstory.session.SessionManager
import com.example.ourstory.utils.test.TokenTest
import okhttp3.Interceptor
import okhttp3.Response

class InterceptorApi(private val session: SessionManager, private val tokenTest: TokenTest) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        if (tokenTest.isTest) {
            request.addHeader("Authorization", "Bearer ${tokenTest.token}")
        } else {
            session.getUser().token.let {
                request.addHeader("Authorization", "Bearer $it")
            }
        }

        return chain.proceed(request.build())
    }
}