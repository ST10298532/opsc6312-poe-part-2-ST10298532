package com.example.eventstrack.utils

import android.content.Context
import androidx.core.content.edit

// 🔹 Simple object to hold key names used in SharedPreferences
object PrefKeys {
    const val TOKEN = "jwt_token"
    const val LANGUAGE = "language"         // "en" or "zu"
    const val NOTIFICATIONS = "notifications" // boolean
    const val NOTIF_LEAD_HOURS = "notif_lead_hours" // int
    const val DISCOVERY_RADIUS = "discovery_radius" // int (km)
    const val THEME = "theme"               // "light" or "dark"
}

// 🔹 Helper object to read and write SharedPreferences easily
object Prefs {
    private const val PREF_NAME = "user_prefs"

    fun putString(ctx: Context, key: String, value: String?) {
        ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit {
            putString(key, value)
            apply()
        }
    }

    fun getString(ctx: Context, key: String, default: String? = null): String? {
        return ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(key, default)
    }

    fun putInt(ctx: Context, key: String, value: Int) {
        ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit {
            putInt(key, value)
            apply()
        }
    }

    fun getInt(ctx: Context, key: String, default: Int = 0): Int {
        return ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getInt(key, default)
    }

    fun putBool(ctx: Context, key: String, value: Boolean) {
        ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit {
            putBoolean(key, value)
            apply()
        }
    }

    fun getBool(ctx: Context, key: String, default: Boolean = false): Boolean {
        return ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(key, default)
    }

    fun clearToken(ctx: Context) {
        ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit {
            remove(PrefKeys.TOKEN)
            apply()
        }
    }
}
