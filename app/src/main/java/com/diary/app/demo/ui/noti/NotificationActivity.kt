package com.diary.app.demo.ui.noti

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.diary.app.demo.R
import com.diary.app.demo.bottomsheet.BottomSheetSetAlarm
import com.diary.app.demo.databinding.ActivityNotificationBinding
import com.diary.app.demo.service.AlarmReceiver
import com.diary.app.demo.ui.BaseActivity
import com.diary.app.demo.ui.noti.viewmodel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class NotificationActivity : BaseActivity<ActivityNotificationBinding>() {
    private lateinit var al : AlarmManager
    override val themeindex: Int =1

    private  var  hours : Int =12
    private var amPm: String = "AM"
    private var alarmsInitialized = false


    private  var minutes : Int =0
    private var selectDay = mutableSetOf<Int>()
    private val viewModel: NotificationViewModel by viewModels()



    override fun getLayoutActivity(): Int = R.layout.activity_notification

    override fun initViews() {
        super.initViews()

        al = getSystemService(ALARM_SERVICE) as AlarmManager


        lifecycleScope.launchWhenStarted {
            viewModel.selectedDays.collect { days ->
                selectDay = days
                restoreSelectedDayUI()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.isNotificationOn.collect { isOn ->
                mBinding.switchNotification.isChecked = isOn
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.alarmTime.collect { triple ->
                triple?.let { (h, m, ampm) ->
                    mBinding.tvNotificationTime.text = String.format("%02d:%02d %s", h, m, ampm)
                    hours = h
                    minutes = m
                    amPm = ampm
                }
            }
        }
        mBinding.btnSave.setOnClickListener {
            viewModel.updateAlarmTime(hours, minutes, amPm)
            if (mBinding.switchNotification.isChecked && selectDay.isNotEmpty()) {
                selectDay.forEach { day ->
                    setAlarm(day)
                }
                Toast.makeText(this, "Đã cập nhật lại báo thức", Toast.LENGTH_SHORT).show()
            }
            finish()

        }


        mBinding.tvNotificationTime.setOnClickListener {
            BottomSheetSetAlarm { hour, minute, amorpm ->
                hours = hour
                minutes = minute
                amPm = amorpm
                mBinding.tvNotificationTime.text =
                    String.format("%02d:%02d %s", hour, minute, amorpm)


            }.show(supportFragmentManager,"BottomSheetSetAlarm")
        }


        mBinding.btnBack.setOnClickListener {
            finish()
        }



        mBinding.daySun.setOnClickListener { onDayClicked(mBinding.daySun, Calendar.SUNDAY) }
        mBinding.dayMon.setOnClickListener { onDayClicked(mBinding.dayMon, Calendar.MONDAY) }
        mBinding.dayTue.setOnClickListener { onDayClicked(mBinding.dayTue, Calendar.TUESDAY) }
        mBinding.dayWed.setOnClickListener { onDayClicked(mBinding.dayWed, Calendar.WEDNESDAY) }
        mBinding.dayThu.setOnClickListener { onDayClicked(mBinding.dayThu, Calendar.THURSDAY) }
        mBinding.dayFri.setOnClickListener { onDayClicked(mBinding.dayFri, Calendar.FRIDAY) }
        mBinding.daySat.setOnClickListener { onDayClicked(mBinding.daySat, Calendar.SATURDAY) }

        mBinding.switchNotification.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

                  ensurePostNotificationsPermission()

                viewModel.setNotificationState(true)
                selectDay.forEach { day -> setAlarm(day)}
                restoreSelectedDayUI()
                Toast.makeText(this, "Đã bật thông báo", Toast.LENGTH_SHORT).show()

            } else {

                    cancelAllSelectedAlarms()

                viewModel.setNotificationState(false)
                Toast.makeText(this, "Đã tắt thông báo", Toast.LENGTH_SHORT).show()
            }
        }


    }
    override fun onResume() {
        super.onResume()

        val am = getSystemService(AlarmManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            !am.canScheduleExactAlarms()
        ) return

        if (!alarmsInitialized) {
            if (mBinding.switchNotification.isChecked) {
                selectDay.forEach { day -> setAlarm(day) }
            }
            alarmsInitialized = true
        }
    }


    private fun ensurePostNotificationsPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }




    private fun setAlarm(dayOfWeek: Int) {
        val intent = Intent(this, AlarmReceiver::class.java).apply {
            putExtra("title", "Nhắc nhở")
            putExtra("message", "Bạn có lịch hẹn")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            dayOfWeek,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = getNextAlarmTime(dayOfWeek,hours,minutes)
        val am = getSystemService(AlarmManager::class.java)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!am.canScheduleExactAlarms()) {

                startActivity(
                    Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                        data = Uri.parse("package:$packageName")
                    }
                )
                return
            }
        }



        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                al.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            } else {
                al.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            }
        } catch (se: SecurityException) {

            al.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            Toast.makeText(this, "Cần cấp quyền exact alarm để báo thức đúng giờ.", Toast.LENGTH_SHORT).show()
        }


    }
    fun getNextAlarmTime(targetDayOfWeek: Int, hour: Int, minute: Int): Long {
        val now = Calendar.getInstance()

        val correctHour = if (amPm == "PM" && hour != 12) hour + 12
        else if (amPm == "AM" && hour == 12) 0
        else hour

        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, targetDayOfWeek)
            set(Calendar.HOUR_OF_DAY, correctHour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }


        if (calendar.timeInMillis <= now.timeInMillis) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
        }

        return calendar.timeInMillis
    }

    private fun onDayClicked(tv: TextView, dayOfWeek: Int) {

        if (selectDay.contains(dayOfWeek)) {
            selectDay.remove(dayOfWeek)
            setDayUnselected(tv)
            cancelAlarmForDay(dayOfWeek)
        } else {
            selectDay.add(dayOfWeek)
            setDaySelected(tv)

            if (mBinding.switchNotification.isChecked) {
                setAlarm(dayOfWeek)
            }
        }

        viewModel.updateSelectedDays(selectDay)
    }

    private fun setDaySelected(tv: TextView) {
        tv.setBackgroundResource(R.drawable.day_selected)
        tv.setTextColor(Color.WHITE)
    }
    private fun restoreSelectedDayUI() {

        setDayUnselected(mBinding.daySun)
        setDayUnselected(mBinding.dayMon)
        setDayUnselected(mBinding.dayTue)
        setDayUnselected(mBinding.dayWed)
        setDayUnselected(mBinding.dayThu)
        setDayUnselected(mBinding.dayFri)
        setDayUnselected(mBinding.daySat)


        selectDay.forEach { day ->
            when (day) {
                Calendar.SUNDAY -> setDaySelected(mBinding.daySun)
                Calendar.MONDAY -> setDaySelected(mBinding.dayMon)
                Calendar.TUESDAY -> setDaySelected(mBinding.dayTue)
                Calendar.WEDNESDAY -> setDaySelected(mBinding.dayWed)
                Calendar.THURSDAY -> setDaySelected(mBinding.dayThu)
                Calendar.FRIDAY -> setDaySelected(mBinding.dayFri)
                Calendar.SATURDAY -> setDaySelected(mBinding.daySat)
            }
        }
    }


    private fun setDayUnselected(tv: TextView) {
        tv.setBackgroundResource(R.drawable.day_unselected)
        tv.setTextColor(Color.parseColor("#BDBDBD"))
    }
    private fun cancelAlarmForDay(dayOfWeek: Int) {
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            dayOfWeek,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        al.cancel(pendingIntent)
    }private fun cancelAllSelectedAlarms() {
        selectDay.forEach { day ->
            cancelAlarmForDay(day)
        }

    }






}