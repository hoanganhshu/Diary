package com.diary.app.demo.ui.passcode
import com.diary.app.demo.R

import android.content.Intent
import androidx.activity.viewModels
import com.diary.app.demo.databinding.ActivityPassCodeBinding
import com.diary.app.demo.ui.BaseActivity
import com.diary.app.demo.ui.security.SecurityQuestionActivity
import com.diary.app.demo.ui.unlock.UnlockPattern
import com.diary.app.demo.ui.security.viewmodel.SecurityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PassCodeActivity : BaseActivity<ActivityPassCodeBinding>() {
    private val securityViewModel: SecurityViewModel by viewModels()


    override val themeindex: Int=1

    override fun getLayoutActivity(): Int = R.layout.activity_pass_code

    override fun initViews() {
        super.initViews()


        mBinding.switchPasscodeProtect.isChecked =
            securityViewModel.isPasscodeEnabled()

        mBinding.switchPasscodeProtect.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val intent = Intent(this, UnlockPattern::class.java)
                intent.putExtra(EXTRA_IS_SETUP_PIN, true)
                startActivity(intent)
            } else {
                securityViewModel.setPasscodeEnabled(false)
            }
        }
        mBinding.btnBack.setOnClickListener {
            finish()
        }
        mBinding.tvChangePasscode.setOnClickListener {
            val intent = Intent(this, UnlockPattern::class.java)
            intent.putExtra(CHANGE_PIN_REQUEST, true)
            startActivity(intent)

        }
        mBinding.tvChangeQuestion.setOnClickListener {
            val intent = Intent(this, SecurityQuestionActivity::class.java)
            intent.putExtra(SecurityQuestionActivity.Companion.EXTRA_MODE, SecurityQuestionActivity.Companion.MODE_SETUP)
            startActivity(intent)
        }
    }

    companion object {
        const val EXTRA_IS_SETUP_PIN = "EXTRA_IS_SETUP_PIN"
        const val CHANGE_PIN_REQUEST = "CHANGE_PIN_REQUEST"
    }

}