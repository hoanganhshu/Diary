package com.diary.app.demo.ui.noti.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diary.app.demo.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationModel: NotificationRepository
) : ViewModel() {


    private val _selectedDays = MutableStateFlow<MutableSet<Int>>(mutableSetOf())
    val selectedDays: StateFlow<MutableSet<Int>> = _selectedDays.asStateFlow()

    private val _isNotificationOn = MutableStateFlow(false)
    val isNotificationOn: StateFlow<Boolean> = _isNotificationOn.asStateFlow()

    private val _alarmTime =
        MutableStateFlow<Triple<Int, Int, String>?>(null)
    val alarmTime: StateFlow<Triple<Int, Int, String>?> = _alarmTime.asStateFlow()

    init {

        loadInitialData()
    }

     fun loadInitialData() {
        viewModelScope.launch {

            val days = notificationModel.loadSelectedDays(context)
            val state = notificationModel.loadSelectedState(context)

            _selectedDays.value = days
            _isNotificationOn.value = state

            val prefs = context.getSharedPreferences("alarm_time_prefs", Context.MODE_PRIVATE)
            if (prefs.contains("hour")) {
                val h = prefs.getInt("hour", 8)
                val m = prefs.getInt("minute", 0)
                val ampm = prefs.getString("ampm", "AM") ?: "AM"
                _alarmTime.value = Triple(h, m, ampm)
            }
        }
    }


    fun setNotificationState(isOn: Boolean) {
        _isNotificationOn.value = isOn
        viewModelScope.launch {
            notificationModel.saveSelectedState(context, isOn)
        }
    }


    fun updateSelectedDays(days: MutableSet<Int>) {
        _selectedDays.value = days
        viewModelScope.launch {
            notificationModel.saveSelectedDays(context, days)
        }
    }


    fun updateAlarmTime(hour: Int, minute: Int, ampm: String) {
        _alarmTime.value = Triple(hour, minute, ampm)

        viewModelScope.launch {
            val prefs = context.getSharedPreferences("alarm_time_prefs", Context.MODE_PRIVATE)
            prefs.edit()
                .putInt("hour", hour)
                .putInt("minute", minute)
                .putString("ampm", ampm)
                .apply()
        }
    }
}