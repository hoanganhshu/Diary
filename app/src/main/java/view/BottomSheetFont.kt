package com.diary.app.demo.view

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.diary.app.demo.R
import com.diary.app.demo.com.diary.app.demo.adapter.ColorsAdapter
import com.diary.app.demo.com.diary.app.demo.adapter.FontAdapter
import com.diary.app.demo.databinding.BottomSheetFontBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFont(
    private val currentAlign: Int,
    private val currentStyle: Int,
    private val onFontSelected: (Int) -> Unit,
    private val onStyleSelected: (Int) -> Unit,
    private val onselectAlign: (Int) -> Unit,
    private val onColorSelected: (Int) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetFontBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: android.os.Bundle?
    ): View? {
        binding = BottomSheetFontBinding.inflate(inflater, container, false)
        dialog?.setCanceledOnTouchOutside(true)


        val drawables = listOf(
            R.drawable.nunito,
            R.drawable.amiri,
            R.drawable.merriweather,
            R.drawable.badscript,
            R.drawable.cardo,
            R.drawable.leaguegothic,
            R.drawable.greatvibes,
            R.drawable.pacifico,
            R.drawable.comingsoon,
            R.drawable.amaticbold,
            R.drawable.lobster,
            R.drawable.reemkufi,
            R.drawable.dancingscript,
            R.drawable.cutivemono
        )

        binding.fontstyle.layoutManager =
            GridLayoutManager(requireContext(),2)
        binding.fontstyle.adapter = FontAdapter(drawables) { index ->
            onFontSelected(index)
        }

        binding.colorRow.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val colorsHex = listOf(
            "#403B36",
            "#4A5568",
            "#A0AEC0",
            "#E2E8F0",
            "#F7FAFC",
            "#434190",
            "#4C51BF",
            "#667EEA",
            "#7F9CF5",
            "#C1DDFF",
            "#553C9A",
            "#805AD5",
            "#9F7AEA",
            "#D6BCFA",
            "#E9D8FD",
            "#97266D",
            "#DD6B20",
            "#ED8936",
            "#F6AD55",
            "#FBD38D",
            "#FEEBC8",
            "#B7791F",
            "#ECC94B",
            "#FAF089",
            "#FFFFD4",
            "#276749",
            "#2F855A",
            "#48BB78",
            "#9AE6B4",
            "#D6FFE1",
            "#2C5282",
            "#2B6CB0",
            "#4299E1",
            "#90CDF4"
        )

        binding.colorRow.adapter = ColorsAdapter(colorsHex) { hexColor ->
            val colorInt = Color.parseColor(hexColor)
            onColorSelected(colorInt)
        }




        val checkedId = when (currentAlign) {
            Gravity.START -> R.id.btnAlignLeft
            Gravity.CENTER_HORIZONTAL -> R.id.btnAlignCenter
            Gravity.END -> R.id.btnAlignRight
            else -> R.id.btnAlignLeft
        }
        binding.Align.check(checkedId)

        binding.btnAlignLeft.setOnClickListener {
            binding.Align.check(R.id.btnAlignLeft)
            onselectAlign(Gravity.START)
        }

        binding.btnAlignCenter.setOnClickListener {
            binding.Align.check(R.id.btnAlignCenter)
            onselectAlign(Gravity.CENTER_HORIZONTAL)
        }

        binding.btnAlignRight.setOnClickListener {
            binding.Align.check(R.id.btnAlignRight)
            onselectAlign(Gravity.END)
        }


        val checkedStyleId = when (currentStyle) {
            Typeface.ITALIC -> R.id.btnStyleItalic
            Typeface.BOLD -> R.id.btnStyleBold
            else -> R.id.btnStyleNormal
        }
        binding.textStyle.check(checkedStyleId)

        binding.btnStyleNormal.setOnClickListener {
            binding.textStyle.check(R.id.btnStyleNormal)
            onStyleSelected(Typeface.NORMAL)
        }

        binding.btnStyleItalic.setOnClickListener {
            binding.textStyle.check(R.id.btnStyleItalic)
            onStyleSelected(Typeface.ITALIC)
        }

        binding.btnStyleBold.setOnClickListener {
            binding.textStyle.check(R.id.btnStyleBold)
            onStyleSelected(Typeface.BOLD)
        }

        return binding.root
    }
}
