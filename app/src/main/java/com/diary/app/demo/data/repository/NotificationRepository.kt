package com.diary.app.demo.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository@Inject constructor(@ApplicationContext private  val context : Context){
    fun saveSelectedDays(context: Context, days: Set<Int>) {
        val prefs = context.getSharedPreferences("alarm_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putStringSet("selected_days", days.map { it.toString() }.toSet())
            .apply()
    }
    fun loadSelectedDays(context: Context): MutableSet<Int> {
        val prefs = context.getSharedPreferences("alarm_prefs", Context.MODE_PRIVATE)
        val saved = prefs.getStringSet("selected_days", emptySet()) ?: emptySet()
        return saved.map { it.toInt() }.toMutableSet()
    }
    fun saveSelectedState(context: Context, isOn: Boolean) {
        val prefs = context.getSharedPreferences("alarmstate_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean("state", isOn)
            .apply()
    }

    fun loadSelectedState(context: Context): Boolean {
        val prefs = context.getSharedPreferences("alarmstate_prefs", Context.MODE_PRIVATE)
        val saved = prefs.getBoolean("state", false)
        return saved
    }
    private fun saveAlarmTime(context: Context, hour: Int, minute: Int, ampm: String) {
        val prefs = context.getSharedPreferences("alarm_time_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putInt("hour", hour)
            .putInt("minute", minute)
            .putString("ampm", ampm)
            .apply()
    }

    private fun loadAlarmTime(context: Context): Triple<Int, Int, String>? {
        val prefs = context.getSharedPreferences("alarm_time_prefs", Context.MODE_PRIVATE)
        if (!prefs.contains("hour")) return null
        val h = prefs.getInt("hour", 8)
        val m = prefs.getInt("minute", 0)
        val ampm = prefs.getString("ampm", "AM") ?: "AM"
        return Triple(h, m, ampm)
    }

}