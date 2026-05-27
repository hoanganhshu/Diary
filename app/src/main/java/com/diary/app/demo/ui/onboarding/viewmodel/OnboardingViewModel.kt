package com.diary.app.demo.ui.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OnboardingViewModel : ViewModel() {
    // Keeps track of whether the current page has a valid selection
    private val _isOptionSelected = MutableStateFlow(false)
    val isOptionSelected: StateFlow<Boolean> = _isOptionSelected

    fun setOptionSelected(selected: Boolean) {
        _isOptionSelected.value = selected
    }

    // Reset when changing pages
    fun resetSelection() {
        _isOptionSelected.value = false
    }
}