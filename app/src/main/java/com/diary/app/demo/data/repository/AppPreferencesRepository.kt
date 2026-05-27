package com.diary.app.demo.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun isFirstTime(): Boolean {
        return prefs.getBoolean("is_first_time", true)
    }

    fun setFirstTime(isFirstTime: Boolean) {
        prefs.edit().putBoolean("is_first_time", isFirstTime).apply()
    }
}
