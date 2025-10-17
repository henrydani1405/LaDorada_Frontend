package com.grupo2.ladorada.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class TokenManager(context: Context) {
    private val PREFS_NAME = "MyPrefs"
    private val KEY_TOKEN = "token"

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // ✅ Guardar token
    fun saveToken(token: String?) {
        prefs.edit { putString(KEY_TOKEN, token) }
    }

    // ✅ Obtener token
    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    // ✅ Verificar si hay sesión
    fun isLoggedIn(): Boolean {
        val token = getToken()
        return !token.isNullOrEmpty()
    }

    // ✅ Borrar token
    fun clearSession() {
        prefs.edit { remove(KEY_TOKEN) }
    }

    // ✅ (Extra) Guardar genéricamente cualquier valor
    fun saveString(key: String, value: String) {
        prefs.edit { putString(key, value) }
    }

    fun getString(key: String): String? {
        return prefs.getString(key, null)
    }

    fun removeKey(key: String) {
        prefs.edit { remove(key) }
    }
}