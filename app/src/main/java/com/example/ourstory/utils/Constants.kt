package com.example.ourstory.utils

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

object Constants {
    const val PREFS_NAME = "prefs_story"
    const val PREFS = "prefs"
    const val UNKNOWN_ERROR = "Unknown error occurred"
    const val TIMEOUT_ERROR = "The server took too long to respond"
    const val STORY = "Story"
    const val SPLASH_DURATION: Long = 300
    const val INIT_PAGE_INDEX = 1

    const val PAGE = 5
    const val SIZE_DATA = 20

    const val FILE_SIZE_MB = 1024

    var BASE_URL_MOCK: String? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    val LOCATION_PERMISSION = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
}