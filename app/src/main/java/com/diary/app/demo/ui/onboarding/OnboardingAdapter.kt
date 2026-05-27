package com.diary.app.demo.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.diary.app.demo.ui.onboarding.fragment.Onboarding1Fragment
import com.diary.app.demo.ui.onboarding.fragment.Onboarding2Fragment
import com.diary.app.demo.ui.onboarding.fragment.Onboarding3Fragment

class OnboardingAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Onboarding1Fragment()
            1 -> Onboarding2Fragment()
            2 -> Onboarding3Fragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}