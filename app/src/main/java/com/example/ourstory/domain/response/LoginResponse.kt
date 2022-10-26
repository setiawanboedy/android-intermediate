package com.example.ourstory.domain.response

import com.example.ourstory.domain.model.UserModel

data class LoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: UserModel
)
