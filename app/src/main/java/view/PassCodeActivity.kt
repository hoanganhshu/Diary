package view

import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.diary.app.demo.BaseActivity
import com.diary.app.demo.databinding.ActivityPassCodeBinding
import dagger.hilt.android.AndroidEntryPoint
import viewmodel.SecurityViewModel
@AndroidEntryPoint
class PassCodeActivity : BaseActivity<ActivityPassCodeBinding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityPassCodeBinding {
        return ActivityPassCodeBinding.inflate(layoutInflater)
    }
    private val securityViewModel: SecurityViewModel by viewModels()


    override val themeindex: Int=1

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
            intent.putExtra(SecurityQuestionActivity.EXTRA_MODE, SecurityQuestionActivity.MODE_SETUP)
            startActivity(intent)
        }
    }

    companion object {
        const val EXTRA_IS_SETUP_PIN = "EXTRA_IS_SETUP_PIN"
        const val CHANGE_PIN_REQUEST = "CHANGE_PIN_REQUEST"
    }

}
