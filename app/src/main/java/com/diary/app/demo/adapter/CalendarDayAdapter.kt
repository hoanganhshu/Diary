package com.diary.app.demo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.diary.app.demo.R

data class CalendarDay(
    val dayNumber: Int,   // 0 = ô trống đầu tháng
    val dateKey: String,  // format "yyyy-MM-dd", rỗng nếu ô trống
    val isToday: Boolean = false,
    val hasDiary: Boolean = false,
    val isSelected: Boolean = false
)

class CalendarDayAdapter(
    private val onDayClick: (CalendarDay) -> Unit
) : RecyclerView.Adapter<CalendarDayAdapter.DayVH>() {

    private val days = mutableListOf<CalendarDay>()
    private var selectedDateKey: String = ""

    fun submitDays(list: List<CalendarDay>, selectedKey: String) {
        selectedDateKey = selectedKey
        days.clear()
        days.addAll(list)
        notifyDataSetChanged()
    }

    fun updateSelection(newSelectedKey: String) {
        val oldIndex = days.indexOfFirst { it.dateKey == selectedDateKey }
        selectedDateKey = newSelectedKey
        val newIndex = days.indexOfFirst { it.dateKey == selectedDateKey }
        if (oldIndex >= 0) notifyItemChanged(oldIndex)
        if (newIndex >= 0) notifyItemChanged(newIndex)
    }

    override fun getItemCount() = days.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayVH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_day, parent, false)
        return DayVH(v)
    }

    override fun onBindViewHolder(holder: DayVH, position: Int) {
        holder.bind(days[position])
    }

    inner class DayVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDay: TextView = itemView.findViewById(R.id.tvDay)
        private val viewCircle: View = itemView.findViewById(R.id.viewDayCircle)
        private val ivStar: ImageView = itemView.findViewById(R.id.ivStar)

        fun bind(day: CalendarDay) {
            if (day.dayNumber == 0) {
                // Ô trống
                tvDay.text = ""
                viewCircle.visibility = View.INVISIBLE
                ivStar.visibility = View.GONE
                itemView.setOnClickListener(null)
                return
            }

            tvDay.text = day.dayNumber.toString()
            ivStar.visibility = if (day.hasDiary) View.VISIBLE else View.GONE

            val isSelected = day.dateKey == selectedDateKey

            when {
                day.isToday -> {
                    viewCircle.visibility = View.VISIBLE
                    viewCircle.alpha = 1f
                    viewCircle.setBackgroundResource(R.drawable.bg_calendar_today)
                    tvDay.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                }
                isSelected -> {
                    viewCircle.visibility = View.VISIBLE
                    viewCircle.alpha = 0.55f
                    viewCircle.setBackgroundResource(R.drawable.bg_calendar_today)
                    tvDay.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                }
                else -> {
                    viewCircle.visibility = View.INVISIBLE
                    viewCircle.alpha = 1f
                    tvDay.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.black))
                }
            }

            itemView.setOnClickListener {
                if (day.dateKey.isNotEmpty()) {
                    onDayClick(day)
                }
            }
        }
    }
}
