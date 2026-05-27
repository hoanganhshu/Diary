package com.diary.app.demo.ui.security.viewmodel

import androidx.lifecycle.ViewModel
import com.diary.app.demo.data.repository.SecurityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SecurityViewModel @Inject constructor(
    private val securityModel: SecurityRepository
) : ViewModel() {

    fun savePin(pin: String) {
        securityModel.savePin(pin)
        securityModel.setPasscodeEnabled(true)
    }

    fun verifyPin(pin: String): Boolean {
        val saved = securityModel.getSavedPin()
        return saved != null && saved == pin
    }

    fun isPasscodeEnabled(): Boolean = securityModel.isPasscodeEnabled()

    fun setPasscodeEnabled(enabled: Boolean) {
        securityModel.setPasscodeEnabled(enabled)
    }

    fun saveSecurityQA(question: String, answer: String) {
        securityModel.saveSecurityQA(question, answer)
    }

    fun getSecurityQuestion(): String? = securityModel.getSecurityQuestion()

    fun verifySecurityAnswer(answer: String): Boolean {
        val saved = securityModel.getSecurityAnswer()
        val normInput = answer.trim().lowercase()
        return saved != null && saved == normInput
    }

    fun getSavedPin(): String? = securityModel.getSavedPin()
}
