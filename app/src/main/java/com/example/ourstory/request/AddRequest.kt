package com.example.ourstory.request

import java.io.File

data class AddRequest(
    val file: File,
    val description: String,
    val lat: Float? = null,
    val lon: Float? = null
)
