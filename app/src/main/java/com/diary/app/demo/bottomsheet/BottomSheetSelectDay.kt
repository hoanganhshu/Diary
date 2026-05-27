package com.diary.app.demo.bottomsheet

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.diary.app.demo.databinding.BottomSheetSelectDayBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetSelectDay(private val callback : (Int , Int) -> Unit) : BottomSheetDialogFragment() {
    var months = listOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding : BottomSheetSelectDayBinding = BottomSheetSelectDayBinding.inflate(inflater,container,false)


        setupmonths(binding)
        setupyears(binding)
        styleWWheel(binding)
        dialog?.setCanceledOnTouchOutside(true)
        setupListeners(binding)
        binding.btnDone.setOnClickListener {

            dismiss()
        }



        return binding.root
    }
    fun setupmonths(binding: BottomSheetSelectDayBinding) {
         months = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )
        binding.monthWheel.data=months
        binding.monthWheel.selectedItemPosition=0
    }

    fun setupyears(binding: BottomSheetSelectDayBinding) {
        val years = (2000..2030).map { it.toString() }.toList()
        binding.yearWheel.data=years
        binding.yearWheel.selectedItemPosition=26
    }
    fun styleWWheel(binding: BottomSheetSelectDayBinding){
        val normalTextSp = 26f
        val normalTextPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            normalTextSp,
            resources.displayMetrics
        ).toInt()

        val selectedTextSp = 30f
        val selectedTextPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            selectedTextSp,
            resources.displayMetrics
        ).toInt()


        val itemSpaceDp = 32f
        val itemSpacePx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            itemSpaceDp,
            resources.displayMetrics
        ).toInt()

        listOf(binding.monthWheel, binding.yearWheel).forEach { item ->
            item.visibleItemCount = 3
            item.itemTextSize = normalTextPx

            item.itemSpace = itemSpacePx
            item.itemTextColor = Color.GRAY
            item.selectedItemTextColor = Color.parseColor("#C57CF6")
        }
    }
    fun setupListeners(binding: BottomSheetSelectDayBinding) {

        fun emitTime() {
            val yeartr = binding.yearWheel.data[binding.yearWheel.currentItemPosition] as String
            val monthtr = binding.monthWheel.data[binding.monthWheel.currentItemPosition] as String
            val month=months.indexOf(monthtr) +1
            callback(month,yeartr.toInt())



        }

        binding.yearWheel.setOnItemSelectedListener { _, _, _ ->
            emitTime()
        }

        binding.monthWheel.setOnItemSelectedListener { _, _, _ ->
            emitTime()
        }


    }

}