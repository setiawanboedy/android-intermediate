package com.example.ourstory.core

import com.example.ourstory.response.GenericResponse
import com.example.ourstory.utils.Constants.TIMEOUT_ERROR
import com.example.ourstory.utils.Constants.UNKNOWN_ERROR
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.SocketTimeoutException

class SafeResponse {
    suspend fun <T, R> enqueue(
        req: T,
        converter: (ResponseBody) -> GenericResponse?,
        call: suspend (T) -> Response<R>
    ): Sealed<R> =
        try {
            val res = call(req)
            val body = res.body()
            val errorBody = res.errorBody()

            if (res.isSuccessful && body != null) {
                Sealed.success(body)
            } else if (errorBody != null) {
                val parsedError = converter(errorBody)
                Sealed.error(parsedError?.message.toString(), null)
            } else {
                Sealed.error(UNKNOWN_ERROR, null)
            }
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException -> Sealed.error(TIMEOUT_ERROR, null)
                else -> Sealed.error(UNKNOWN_ERROR, null)
            }
        }

    suspend fun <T, U, R> enqueue(
        req1: T,
        req2: U,
        converter: (ResponseBody) -> GenericResponse?,
        call: suspend (T, U) -> Response<R>
    ): Sealed<R> =
        try {
            val res = call(req1, req2)
            val body = res.body()
            val errorBody = res.errorBody()

            if (res.isSuccessful && body != null) {
                Sealed.success(body)
            } else if (errorBody != null) {
                val parsedError = converter(errorBody)
                Sealed.error(parsedError?.message.toString(), null)
            } else {
                Sealed.error(UNKNOWN_ERROR, null)
            }
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException -> Sealed.error(TIMEOUT_ERROR, null)
                else -> Sealed.error(UNKNOWN_ERROR, null)
            }
        }

    suspend fun <T, U, S, V, R> enqueue(
        req1: T,
        req2: U,
        req3: S,
        req4: V,
        converter: (ResponseBody) -> GenericResponse?,
        call: suspend (T, U, S, V) -> Response<R>
    ): Sealed<R> =
        try {
            val res = call(req1, req2, req3, req4)
            val body = res.body()
            val errorBody = res.errorBody()

            if (res.isSuccessful && body != null) {
                Sealed.success(body)
            } else if (errorBody != null) {
                val parsedError = converter(errorBody)
                Sealed.error(parsedError?.message.toString(), null)
            } else {
                Sealed.error(UNKNOWN_ERROR, null)
            }
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException -> Sealed.error(TIMEOUT_ERROR, null)
                else -> Sealed.error(UNKNOWN_ERROR, null)
            }
        }
}