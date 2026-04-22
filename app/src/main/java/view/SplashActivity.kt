package view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.diary.app.demo.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import viewmodel.SecurityViewModel

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val securityViewModel: SecurityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(2000)

            val next = if (securityViewModel.isPasscodeEnabled()) {
                Intent(this@SplashActivity, UnlockPattern::class.java)
            } else {
                Intent(this@SplashActivity, OnBoard1::class.java)
            }

            startActivity(next)
            finish()
        }
    }
}
