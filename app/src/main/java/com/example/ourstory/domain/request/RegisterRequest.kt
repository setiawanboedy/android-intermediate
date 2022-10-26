package com.example.ourstory.domain.request

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)
