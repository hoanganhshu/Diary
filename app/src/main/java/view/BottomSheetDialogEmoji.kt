package com.diary.app.demo.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.diary.app.demo.R
import com.diary.app.demo.databinding.BottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import view.MoodGallery
import viewmodel.MoodGalleryViewModels
@AndroidEntryPoint
class BottomSheetDialogEmoji(
    private val onEmojiSelected: (Int) -> Unit
) : BottomSheetDialogFragment()
 {
    private lateinit var binding: BottomSheetDialogBinding


    private  val moodviewmodel : MoodGalleryViewModels by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetDialogBinding.inflate(inflater, container, false)
        dialog?.setCanceledOnTouchOutside(true)



        binding.seeMoreRow.setOnClickListener {
            val intent = Intent(requireContext(), MoodGallery::class.java)
            startActivity(intent)
            dismiss()
        }

        val emojiViews = listOf(
            binding.mood1 ,
            binding.mood2 ,
            binding.mood3 ,
            binding.mood4 ,
            binding.mood5 ,
            binding.mood6 ,
            binding.mood7 ,
            binding.mood8
        )
        val emojiList: List<Int> = moodviewmodel.getMoods().emojiList


        emojiViews.forEach { it.visibility = View.GONE }
        binding.btnClose.setOnClickListener {
            dismiss()
        }
        emojiViews.zip(emojiList).forEach { (img, resId) ->
            img.setImageResource(resId)

            img.visibility = View.VISIBLE
            img.setOnClickListener {
                onEmojiSelected(resId)
                emojiViews.forEach { it.background = null }

                img.setBackgroundResource(R.drawable.bg_selected_border)


            }
        }
        return binding.root
    }

}