package com.example.ourstory.ui.utils

import com.example.ourstory.domain.model.UserModel
import com.example.ourstory.domain.response.LoginResponse

object DataDummyTest {
    fun loginResponseSuccessTest(): LoginResponse {
        val userModel = UserModel(
            userId = "1",
            name = "Reina",
            email = "reina@dicoding.org",
            token = "kldjfalksjfkldasjfkldhsalfjkasdfhkjdhslkf"
        )
        return LoginResponse(
            error = false,
            message = "success",
            loginResult = userModel
        )
    }
}