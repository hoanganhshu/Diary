package com.diary.app.demo.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.diary.app.demo.R
import com.diary.app.demo.ui.onboarding.OnboardingActivity
import com.diary.app.demo.ui.unlock.UnlockPattern
import com.diary.app.demo.ui.home.HomeActivity
import com.diary.app.demo.ui.splash.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(2000)

            val next = when {
                splashViewModel.isPasscodeEnabled() -> {
                    Intent(this@SplashActivity, UnlockPattern::class.java)
                }
                splashViewModel.isFirstTime() -> {
                    splashViewModel.setFirstTimeCompleted()
                    Intent(this@SplashActivity, OnboardingActivity::class.java)
                }
                else -> {
                    Intent(this@SplashActivity, HomeActivity::class.java)
                }
            }

            startActivity(next)
            finish()
        }
    }
}