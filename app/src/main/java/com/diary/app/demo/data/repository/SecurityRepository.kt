package com.diary.app.demo.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SecurityRepository @Inject constructor(@ApplicationContext context : Context) {




    private val prefs = context.getSharedPreferences("app_security", Context.MODE_PRIVATE)

    fun savePin(pin: String) = prefs.edit().putString("pin_code", pin).apply()

    fun getSavedPin(): String? = prefs.getString("pin_code", null)

    fun setPasscodeEnabled(enabled: Boolean) =
        prefs.edit().putBoolean("passcode_enabled", enabled).apply()

    fun isPasscodeEnabled(): Boolean =
        prefs.getBoolean("passcode_enabled", false)

    fun saveSecurityQA(question: String, answer: String) {

        val norm = answer.trim().lowercase()
        prefs.edit()
            .putString("security_question", question)
            .putString("security_answer", norm)
            .apply()
    }

    fun getSecurityQuestion(): String? = prefs.getString("security_question", null)
    fun getSecurityAnswer(): String? = prefs.getString("security_answer", null)
}