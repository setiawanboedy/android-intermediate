package com.example.ourstory.response

import com.example.ourstory.model.UserModel

data class LoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: UserModel
)
