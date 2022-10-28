package com.example.ourstory.utils

import com.example.ourstory.domain.model.StoryModel
import com.example.ourstory.domain.model.UserModel
import com.example.ourstory.domain.response.GenericResponse
import com.example.ourstory.domain.response.LoginResponse
import com.example.ourstory.domain.response.RegisterResponse
import com.example.ourstory.domain.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object DataDummy {

    fun loginResponseDummy(): LoginResponse {
        val user = UserModel(
            userId = "NCwjLkdjfQGLrdaH2sdf",
            name = "Sinaga",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLU5Dd2pMenZRR0xyZGFIMnMiLCJpYXQiOjE2NjE0NzEyNzR9.B3u5kf8nYrYwx2Tftn5G8jDM4U5BR9HvNiDT9fZludA"
        )

        return LoginResponse(
            error = false,
            message = "Success",
            user
        )
    }

    fun storiesResponseDummy(): StoriesResponse {
        val error = false
        val message = "Stories fetched successfully"
        val listStory = mutableListOf<StoryModel>()

        for (i in 0 until 5) {
            val data = StoryModel(
                id = "story-Nhq11P9eN5fnqjQ8",
                name = "Sinaga di lombok",
                description = "Test with location",
                photoUrl = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/academy/dos:2979e516a20f11ebf05f0d735a5b288120220221083443.jpeg",
                createdAt = "2022-08-25T23:15:43.036Z",
                lat = -7.07769,
                lon = 112.85524
            )
            listStory.add(data)
        }
        return StoriesResponse(error, message, listStory)
    }

    fun listStoriesDummy(): List<StoryModel> {
        val listStory = arrayListOf<StoryModel>()

        for (i in 0 until 5) {
            val data = StoryModel(
                id = "story-Nhq11P9eN5fnqjQ8",
                name = "Sinaga di lombok",
                description = "Test with location",
                photoUrl = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/academy/dos:2979e516a20f11ebf05f0d735a5b288120220221083443.jpeg",
                createdAt = "2022-08-25T23:15:43.036Z",
                lat = -7.07769,
                lon = 112.85524
            )
            listStory.add(data)
        }
        return listStory
    }


    fun requestBodyDummy(): RequestBody = "Test".toRequestBody()
    fun registerResponseDummy(): RegisterResponse = RegisterResponse(false, "success")
    fun multipartFileDummy(): MultipartBody.Part = MultipartBody.Part.create("Test".toRequestBody())
    fun addStoryResponseDummy(): GenericResponse = GenericResponse(false, "success")
    fun registerErrorDummy(): RegisterResponse = RegisterResponse(true, "invalid email")
    fun loginErrorDummy(): GenericResponse = GenericResponse(true, "user not found")
    fun uploadErrorDummy(): GenericResponse = GenericResponse(true, "UNKNOWN ERROR")
    fun storiesErrorDummy(): StoriesResponse =
        StoriesResponse(true, "UNKNOWN ERROR", listOf<StoryModel>())
}