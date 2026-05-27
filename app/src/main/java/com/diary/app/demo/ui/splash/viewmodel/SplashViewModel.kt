package com.diary.app.demo.ui.splash.viewmodel

import androidx.lifecycle.ViewModel
import com.diary.app.demo.data.repository.AppPreferencesRepository
import com.diary.app.demo.data.repository.SecurityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appPrefsRepo: AppPreferencesRepository,
    private val securityRepo: SecurityRepository
) : ViewModel() {

    fun isPasscodeEnabled(): Boolean {
        return securityRepo.isPasscodeEnabled()
    }

    fun isFirstTime(): Boolean {
        return appPrefsRepo.isFirstTime()
    }

    fun setFirstTimeCompleted() {
        appPrefsRepo.setFirstTime(false)
    }
}