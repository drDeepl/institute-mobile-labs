package com.example.lab5v2.service

import android.content.Context
import android.content.SharedPreferences

class TokenService(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("TOKEN_PREFS", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()

    fun saveAccessToken(token: String) {
        editor.putString("ACCESS_TOKEN", token)
        editor.apply()
    }

    fun getAccessToken(): String? {
        return prefs.getString("ACCESS_TOKEN", null)
    }

    fun clearAccessToken() {
        editor.remove("ACCESS_TOKEN")
        editor.apply()
    }
}
