package com.example.ourstory.domain.model

data class UserModel(
    val userId: String,
    val name: String,
    val email: String? = null,
    val token: String,
)
