package com.example.ourstory.session

import android.content.SharedPreferences

class Preferences(private val prefs: SharedPreferences) {
    companion object {
        private const val PASSWORD = "password"
    }

    fun setPw(pw: String) {
        val editor = prefs.edit()
        editor.putString(PASSWORD, pw)
        editor.apply()
    }

    fun getPw(): String? =
        prefs.getString(PASSWORD, "")

    fun clearPw() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}