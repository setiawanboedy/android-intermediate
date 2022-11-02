package com.example.ourstory.utils

import androidx.room.Room
import com.example.ourstory.R
import com.example.ourstory.core.ErrorParser
import com.example.ourstory.core.SafeResponse
import com.example.ourstory.data.local.db.StoryDatabase
import com.example.ourstory.data.network.get.GetService
import com.example.ourstory.data.network.interceptor.InterceptorApi
import com.example.ourstory.data.network.post.AuthService
import com.example.ourstory.data.network.post.PostService
import com.example.ourstory.session.SessionManager
import com.example.ourstory.utils.test.TokenTest
import okhttp3.OkHttpClient
import org.robolectric.RuntimeEnvironment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

object Helper {

    fun getDatabase(): StoryDatabase {
        return Room.inMemoryDatabaseBuilder(
            RuntimeEnvironment.getApplication(),
            StoryDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }


    fun getService(session: SessionManager): GetService {
        val tokenTest = TokenTest(
            true,
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVRvR0ItSmZ0Nm5SaHJ0eUIiLCJpYXQiOjE2NjczNzYwODN9.TDFVo5EzZUyqXZ9nBEpQLTn781SQ-HVNmpD2udacQlE"
        )
        val interceptorApi = OkHttpClient.Builder()
            .addInterceptor(InterceptorApi(session, tokenTest))
            .build()
        return Retrofit
            .Builder()
            .client(interceptorApi)
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(GetService::class.java)
    }

    fun postService(): PostService {
        return Retrofit
            .Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(PostService::class.java)
    }

    fun authService(): AuthService {
        return Retrofit
            .Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(AuthService::class.java)
    }

    fun safeResponse(): SafeResponse {
        return SafeResponse()
    }

    fun errorParser(): ErrorParser {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return ErrorParser(retrofit)
    }

    fun getPhoto(): File {
        val newFile = File("file-testing-lele")
        val inputStream: InputStream =
            RuntimeEnvironment.getApplication().resources.openRawResource(R.drawable.ic_action_image)
        val out: OutputStream = FileOutputStream(newFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) out.write(buf, 0, len)
        out.close()
        inputStream.close()
        return newFile
    }
}