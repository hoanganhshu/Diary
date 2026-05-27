
package com.diary.app.demo.service

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.annotation.RequiresPermission

class AlarmReceiver : BroadcastReceiver() {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Alarm"
        val message = intent.getStringExtra("message") ?: "Đến giờ rồi!"


        Toast.makeText(context, "Alarm received!", Toast.LENGTH_SHORT).show()

        NotificationHelper(context).sendNotification(title,message)
    }
}
