package com.diary.app.demo.view





import BackgroundAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.diary.app.demo.R
import com.diary.app.demo.databinding.BottomSheetBackgroundBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout

class BottomSheetBackGround(private val onSelected: (Int) -> Unit) : BottomSheetDialogFragment(){



    private lateinit var binding: BottomSheetBackgroundBinding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetBackgroundBinding.inflate(inflater, container, false)
        binding.rvBackgrounds.layoutManager = GridLayoutManager(requireContext(), 4)
        dialog?.setCanceledOnTouchOutside(true)




        val backgroundList = listOf(
            R.drawable.background_basic,
            R.drawable.simple1,
            R.drawable.simple2,
            R.drawable.simple3,
            R.drawable.simple4_1,
            R.drawable.simple4_2,
            R.drawable.simple4_3,
            R.drawable.simple4_4,
            R.drawable.simple4_5,
            R.drawable.animal1,
            R.drawable.animal2,
            R.drawable.animal3,
            R.drawable.animal4,
            R.drawable.animal5,
            R.drawable.animal6,
            R.drawable.animal7,
            R.drawable.food1,
            R.drawable.food2,
            R.drawable.food3,
            R.drawable.food4,
            R.drawable.food5,
            R.drawable.holiday1,
            R.drawable.holiday_2,
            R.drawable.holiday_3,
            R.drawable.holiday_4,
        )
        val backgroundSimple =listOf(

            R.drawable.simple1,
            R.drawable.simple2,
            R.drawable.simple3,
            R.drawable.simple4_1,
            R.drawable.simple4_2,
            R.drawable.simple4_3,
            R.drawable.simple4_4,
            R.drawable.simple4_5,
        )
        val backgroundAnimal=listOf(

            R.drawable.animal1,
            R.drawable.animal2,
            R.drawable.animal3,
            R.drawable.animal4,
            R.drawable.animal5,
            R.drawable.animal6,
            R.drawable.animal7,
        )
        val backgroundFood=listOf(

            R.drawable.food1,
            R.drawable.food2,
            R.drawable.food3,
            R.drawable.food4,
            R.drawable.food5,

        )
        val backgroundTravel=listOf(
            R.drawable.holiday1,
            R.drawable.holiday_2,
            R.drawable.holiday_3,
            R.drawable.holiday_4,

        )



        var currentList = backgroundList


        val adapter = BackgroundAdapter(currentList) { localPosition ->



            val drawableId = currentList[localPosition]
            if(drawableId==R.drawable.background_basic){
                dismiss()
            }
            else{
                onSelected(drawableId)

            }






        }
        binding.rvBackgrounds.adapter = adapter

        binding.tabBackground.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentList = when (tab?.position) {
                    0 -> backgroundList
                    1 -> backgroundSimple
                    2 -> backgroundAnimal
                    3 -> backgroundFood
                    4 -> backgroundTravel
                    else -> backgroundList
                }
                adapter.submitList(currentList)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })




        return binding.root
    }

}
