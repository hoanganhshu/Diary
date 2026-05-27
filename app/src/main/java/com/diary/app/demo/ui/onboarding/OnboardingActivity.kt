package com.diary.app.demo.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.diary.app.demo.R
import com.diary.app.demo.databinding.ActivityOnboardingBinding
import com.diary.app.demo.ui.unlock.UnlockPattern
import com.diary.app.demo.ui.onboarding.OnboardingAdapter
import com.diary.app.demo.ui.home.HomeActivity
import com.diary.app.demo.ui.splash.viewmodel.SplashViewModel
import com.diary.app.demo.ui.onboarding.viewmodel.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private val onboardingViewModel: OnboardingViewModel by viewModels()
    private val splashViewModel: SplashViewModel by viewModels() // Reuse for isPasscodeEnabled

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val adapter = OnboardingAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false // Prevent swiping if we want to enforce selection?
        // Let's allow swiping but only if the current page has a selection. Actually, the easiest is to just disable swiping and force using the Continue button.
        // The original app didn't have swiping, it just had next button. Let's keep isUserInputEnabled = false.

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDots(position)
                // The fragment's onResume will update the viewmodel's state
            }
        })

        lifecycleScope.launch {
            onboardingViewModel.isOptionSelected.collectLatest { isSelected ->
                binding.btnContinue.isEnabled = isSelected
            }
        }

        binding.btnContinue.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            if (currentItem < adapter.itemCount - 1) {
                binding.viewPager.currentItem = currentItem + 1
            } else {
                finishOnboarding()
            }
        }

        binding.tvSkip.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            if (currentItem < adapter.itemCount - 1) {
                binding.viewPager.currentItem = currentItem + 1
            } else {
                finishOnboarding()
            }
        }
    }

    private fun updateDots(position: Int) {
        val dot1 = binding.dot1
        val dot2 = binding.dot2
        val dot3 = binding.dot3

        dot1.setBackgroundResource(if (position == 0) R.drawable.dotfilled else R.drawable.dot)
        dot2.setBackgroundResource(if (position == 1) R.drawable.dotfilled else R.drawable.dot)
        dot3.setBackgroundResource(if (position == 2) R.drawable.dotfilled else R.drawable.dot)

        val params1 = dot1.layoutParams
        params1.width = resources.getDimensionPixelSize(if (position == 0) com.intuit.sdp.R.dimen._16sdp else com.intuit.sdp.R.dimen._6sdp)
        dot1.layoutParams = params1

        val params2 = dot2.layoutParams
        params2.width = resources.getDimensionPixelSize(if (position == 1) com.intuit.sdp.R.dimen._16sdp else com.intuit.sdp.R.dimen._6sdp)
        dot2.layoutParams = params2

        val params3 = dot3.layoutParams
        params3.width = resources.getDimensionPixelSize(if (position == 2) com.intuit.sdp.R.dimen._16sdp else com.intuit.sdp.R.dimen._6sdp)
        dot3.layoutParams = params3
    }

    private fun finishOnboarding() {
        splashViewModel.setFirstTimeCompleted()
        val next = if (splashViewModel.isPasscodeEnabled()) {
            Intent(this, UnlockPattern::class.java)
        } else {
            Intent(this, HomeActivity::class.java)
        }
        startActivity(next)
        finish()
    }
}