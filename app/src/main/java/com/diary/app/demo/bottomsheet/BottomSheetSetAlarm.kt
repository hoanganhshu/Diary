package com.diary.app.demo.bottomsheet

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.R
import com.diary.app.demo.databinding.BottomSheetSetAlarmBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetSetAlarm(private val callback : (Int,Int,String) -> Unit) : BottomSheetDialogFragment() {





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val binding : BottomSheetSetAlarmBinding = BottomSheetSetAlarmBinding.inflate(inflater,container,false)

       setuphours(binding)
        dialog?.setCanceledOnTouchOutside(true)
       setupminutes(binding)
       setupamorpm(binding)
        styleWWheel(binding)
       setupListeners(binding)
        binding.btnDone.setOnClickListener {

            val hour = (binding.hours.data[
                binding.hours.currentItemPosition
            ] as String).toInt()

            val minute = (binding.minutes.data[
                binding.minutes.currentItemPosition
            ] as String).toInt()

            val ampm = binding.amorpm.data[
                binding.amorpm.currentItemPosition
            ] as String

            callback(hour, minute, ampm)
            dismiss()
        }



        return binding.root
    }
    fun setuphours(binding: BottomSheetSetAlarmBinding) {
        val hours = (1..12).map { it.toString().padStart(2,'0')}
        binding.hours.data=hours
        binding.hours.selectedItemPosition=7
    }
    fun setupminutes(binding: BottomSheetSetAlarmBinding) {
        val minutes = (0..59).map { it.toString().padStart(2, '0') } // 00..59
        binding.minutes.data = minutes
        binding.minutes.selectedItemPosition = 0
    }
    fun setupamorpm(binding: BottomSheetSetAlarmBinding) {
        val amPm= listOf("AM","PM")
        binding.amorpm.data=amPm
        binding.amorpm.selectedItemPosition=0
    }
    fun styleWWheel(binding: BottomSheetSetAlarmBinding) {


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

        listOf(binding.hours, binding.minutes, binding.amorpm).forEach { item ->
            item.visibleItemCount = 3
            item.itemTextSize = normalTextPx

            item.itemSpace = itemSpacePx
            item.itemTextColor = Color.GRAY
            val typedValue = TypedValue()
            requireContext().theme.resolveAttribute(
                R.attr.colorPrimary,
                typedValue,
                true
            )
            val colorPrimary = typedValue.data
            item.selectedItemTextColor =colorPrimary

        }
    }

    fun setupListeners(binding: BottomSheetSetAlarmBinding) {

        fun emitTime() {
            val hourStr = binding.hours.data[binding.hours.currentItemPosition] as String
            val minuteStr = binding.minutes.data[binding.minutes.currentItemPosition] as String
            val ampmStr = binding.amorpm.data[binding.amorpm.currentItemPosition] as String

            callback(hourStr.toInt(), minuteStr.toInt(), ampmStr)
        }

        binding.hours.setOnItemSelectedListener { _, _, _ ->
            emitTime()
        }

        binding.minutes.setOnItemSelectedListener { _, _, _ ->
            emitTime()
        }

        binding.amorpm.setOnItemSelectedListener { _, _, _ ->
            emitTime()
        }
    }





}