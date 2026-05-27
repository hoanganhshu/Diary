package com.diary.app.demo.ui.calendar
import com.diary.app.demo.R

import android.content.Intent
import android.os.SystemClock
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.diary.app.demo.adapter.AdapterDiaries
import com.diary.app.demo.adapter.CalendarDay
import com.diary.app.demo.adapter.CalendarDayAdapter
import com.diary.app.demo.adapter.DiaryCard
import com.diary.app.demo.databinding.ActivityCalendarBinding
import com.diary.app.demo.ui.BaseActivity
import com.diary.app.demo.ui.diary.AddDiary
import com.diary.app.demo.ui.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class CalendarActivity : BaseActivity<ActivityCalendarBinding>() {

    override val themeindex: Int = 1

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var diariesAdapter: AdapterDiaries
    private lateinit var calendarDayAdapter: CalendarDayAdapter

    private var blockClickUntil = 0L
    private var queryJob: Job? = null
    private var calendarLoadJob: Job? = null

    // Trạng thái tháng đang hiển thị
    private val currentCal = Calendar.getInstance()
    private var selectedDateKey: String = todayKey()


    override fun getLayoutActivity(): Int = R.layout.activity_calendar

    override fun initViews() {
        blockClickUntil = SystemClock.elapsedRealtime() + 500

        setupDiariesRecycler()
        setupCalendarGrid()

        // Nếu được truyền ngày từ màn trước, chọn ngày đó
        val firstDay = intent.getStringExtra("diary_day").orEmpty()
        if (firstDay.isNotBlank()) {
            selectedDateKey = firstDay
            // Cập nhật currentCal về tháng của ngày được truyền
            val parts = firstDay.split("-")
            if (parts.size == 3) {
                currentCal.set(Calendar.YEAR, parts[0].toInt())
                currentCal.set(Calendar.MONTH, parts[1].toInt() - 1)
                currentCal.set(Calendar.DAY_OF_MONTH, parts[2].toInt())
            }
        }

        loadCalendarMonth()
        queryByDay(selectedDateKey)

        // Nút tháng trước
        mBinding.btnPrevMonth.setOnClickListener {
            currentCal.add(Calendar.MONTH, -1)
            loadCalendarMonth()
        }

        // Nút tháng sau
        mBinding.btnNextMonth.setOnClickListener {
            currentCal.add(Calendar.MONTH, 1)
            loadCalendarMonth()
        }

        // Nút "Hôm nay"
        mBinding.btnSelectMonthYear.setOnClickListener {
            currentCal.time = Calendar.getInstance().time
            selectedDateKey = todayKey()
            calendarDayAdapter.updateSelection(selectedDateKey)
            loadCalendarMonth()
            queryByDay(selectedDateKey)
        }

        mBinding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupDiariesRecycler() {
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

    private fun setupCalendarGrid() {
        calendarDayAdapter = CalendarDayAdapter { day ->
            selectedDateKey = day.dateKey
            calendarDayAdapter.updateSelection(selectedDateKey)
            queryByDay(selectedDateKey)
        }
        mBinding.rvCalendarDays.layoutManager = GridLayoutManager(this, 7)
        mBinding.rvCalendarDays.adapter = calendarDayAdapter
    }

    /**
     * Tải danh sách ngày của tháng hiện tại và đánh dấu những ngày có nhật ký
     */
    private fun loadCalendarMonth() {
        // Cập nhật tiêu đề tháng/năm
        val month = currentCal.get(Calendar.MONTH) + 1
        val year = currentCal.get(Calendar.YEAR)
        mBinding.tvMonthYear.text = "Tháng $month, $year"

        calendarLoadJob?.cancel()
        calendarLoadJob = lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Lấy tất cả nhật ký trong tháng (dạng set các dateKey)
                homeViewModel.getAllDiaries().collect { allDiaries ->
                    val monthPrefix = String.format("%04d-%02d", year, month)
                    val daysWithDiary = allDiaries
                        .filter { it.day.startsWith(monthPrefix) }
                        .map { it.day }
                        .toSet()

                    val days = buildCalendarDays(year, month, daysWithDiary)
                    calendarDayAdapter.submitDays(days, selectedDateKey)
                }
            }
        }
    }

    /**
     * Tạo danh sách CalendarDay cho 1 tháng:
     * - Thêm ô trống đầu tháng (để căn thứ)
     * - Đánh dấu ngày hôm nay
     * - Đánh dấu ngày có nhật ký
     */
    private fun buildCalendarDays(
        year: Int,
        month: Int,
        daysWithDiary: Set<String>
    ): List<CalendarDay> {
        val today = Calendar.getInstance()
        val todayKey = todayKey()

        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        // Thứ bắt đầu tháng (0=CN, 1=T2, ..., 6=T7)
        // Calendar.SUNDAY=1, MONDAY=2 → ta đổi sang 0-indexed với CN=0
        val firstDayOfWeek = (cal.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY + 7) % 7
        val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val result = mutableListOf<CalendarDay>()

        // Thêm ô trống đầu tháng
        repeat(firstDayOfWeek) {
            result.add(CalendarDay(dayNumber = 0, dateKey = ""))
        }

        // Thêm các ngày thực
        for (d in 1..daysInMonth) {
            val key = String.format("%04d-%02d-%02d", year, month, d)
            result.add(
                CalendarDay(
                    dayNumber = d,
                    dateKey = key,
                    isToday = key == todayKey,
                    hasDiary = daysWithDiary.contains(key)
                )
            )
        }

        return result
    }

    private fun queryByDay(day: String) {
        queryJob?.cancel()
        diariesAdapter.submitList(emptyList())

        queryJob = lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.getDiarybyDay(day).collect { list ->
                    val cards = listOf(DiaryCard(diaries = list))
                    diariesAdapter.submitList(cards)
                    val isEmpty = list.isNullOrEmpty()
                    mBinding.noDiary.visibility =
                        if (isEmpty) View.VISIBLE else View.GONE
                    mBinding.rvDiariesCalendar.visibility =
                        if (isEmpty) View.GONE else View.VISIBLE
                }
            }
        }
    }

    private fun todayKey(): String {
        val t = Calendar.getInstance()
        return String.format(
            "%04d-%02d-%02d",
            t.get(Calendar.YEAR),
            t.get(Calendar.MONTH) + 1,
            t.get(Calendar.DAY_OF_MONTH)
        )
    }

    override fun onDestroy() {
        queryJob?.cancel()
        calendarLoadJob?.cancel()
        super.onDestroy()
    }
}