package view

import android.content.Intent
import android.os.SystemClock
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.diary.app.demo.BaseActivity
import com.diary.app.demo.adapter.AdapterDiaries
import com.diary.app.demo.adapter.DiaryCard
import com.diary.app.demo.databinding.ActivityCalendarBinding
import com.diary.app.demo.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class CalendarActivity : BaseActivity<ActivityCalendarBinding>() {

    override val themeindex: Int = 1

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var diariesAdapter: AdapterDiaries

    private var blockClickUntil = 0L
    private var queryJob: Job? = null

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityCalendarBinding {
        return ActivityCalendarBinding.inflate(layoutInflater)
    }

    override fun initViews() {

        blockClickUntil = SystemClock.elapsedRealtime() + 500

        setupRecycler()


        val firstDay = intent.getStringExtra("diary_day").orEmpty()
        android.util.Log.d("DB", "day-in-db=${firstDay}")

        val dayToQuery = firstDay.ifBlank {
            val millis = mBinding.calendar.date
            val cal = java.util.Calendar.getInstance().apply { timeInMillis = millis }
            formatDayString(
                cal.get(java.util.Calendar.DAY_OF_MONTH),
                cal.get(java.util.Calendar.MONTH) + 1,
                cal.get(java.util.Calendar.YEAR)
            )
        }

        queryByDay(dayToQuery)



        mBinding.calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDay = formatDayString(dayOfMonth, month + 1, year)
            android.util.Log.d("CalendarActivity", "selectedDay=$selectedDay")
            queryByDay(selectedDay)
        }
        mBinding.btnBack.setOnClickListener {
            finish()
        }


        mBinding.btnSelectMonthYear.setOnClickListener {
            val today = Calendar.getInstance()
            mBinding.calendar.date = today.timeInMillis

            val todayString = formatDayString(
                today.get(Calendar.DAY_OF_MONTH),
                today.get(Calendar.MONTH) + 1,
                today.get(Calendar.YEAR)
            )
            queryByDay(todayString)
        }

    }

    private fun setupRecycler() {
        diariesAdapter = AdapterDiaries { diaryEntity ->
            val now = SystemClock.elapsedRealtime()
            if (now < blockClickUntil) return@AdapterDiaries
            blockClickUntil = now + 500

            startActivity(Intent(this, AddDiary::class.java).apply {
                putExtra("diary_id", diaryEntity.id)
                putExtra("show", false)
            })
        }

        mBinding.rvDiariesCalendar.layoutManager = LinearLayoutManager(this)
        mBinding.rvDiariesCalendar.adapter = diariesAdapter
        mBinding.rvDiariesCalendar.setHasFixedSize(true)
    }

    private fun queryByDay(day: String) {

        queryJob?.cancel()


        diariesAdapter.submitList(emptyList())

        queryJob = lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.getDiarybyDay(day).collect { list ->
                    val cards = listOf(
                        DiaryCard(diaries = list)
                    )
                    diariesAdapter.submitList(cards)
                    val isEmpty = list.isNullOrEmpty()
                    mBinding.noDiary.visibility = if (isEmpty) android.view.View.VISIBLE else android.view.View.GONE
                    mBinding.rvDiariesCalendar.visibility = if (isEmpty) android.view.View.GONE else android.view.View.VISIBLE
                }
            }
        }
    }

//    private fun showMonthYearBottomSheet() {
//        val sheet = BottomSheetSelectDay { month, year ->
//            val cal = Calendar.getInstance().apply {
//                set(Calendar.YEAR, year)
//                set(Calendar.MONTH, month - 1)
//
//                set(Calendar.DAY_OF_MONTH, 1)
//            }
//
//
//            mBinding.calendar.date = cal.timeInMillis
//
//
//            val day1 = formatDayString(1, month, year)
//            queryByDay(day1)
//
//
//        }
//
//        sheet.show(supportFragmentManager, "BottomSheetSelectDay")
//    }


    private fun formatDayString(day: Int, month: Int, year: Int): String {
        val dd = day.toString().padStart(2, '0')
        val mm = month.toString().padStart(2, '0')
        return "$year-$mm-$dd"
    }



    override fun onDestroy() {
        queryJob?.cancel()
        super.onDestroy()
    }
}
