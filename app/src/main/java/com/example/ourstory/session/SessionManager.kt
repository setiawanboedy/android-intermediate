package com.example.ourstory.session

import android.content.SharedPreferences
import com.example.ourstory.domain.model.UserModel

class SessionManager(private val prefs: SharedPreferences) {
    companion object {
        private const val ID = "id"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val TOKEN = "token"
        private const val LOGIN = "login"
    }


    fun setUser(data: UserModel) {
        val editor = prefs.edit()
        editor.putString(ID, data.userId)
        editor.putString(NAME, data.name)
        editor.putString(EMAIL, data.email)
        editor.putString(TOKEN, data.token)
        editor.apply()
    }

    fun getUser(): UserModel =
        UserModel(
            userId = prefs.getString(ID, "").toString(),
            name = prefs.getString(NAME, "").toString(),
            email = prefs.getString(EMAIL, "").toString(),
            token = prefs.getString(TOKEN, "").toString()
        )


    fun setLogin(value: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(LOGIN, value)
        editor.apply()
    }

    fun getLogin(): Boolean =
        prefs.getBoolean(LOGIN, false)

    fun logout() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}