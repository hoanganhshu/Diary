package view

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import com.diary.app.demo.BaseActivity
import com.diary.app.demo.R
import com.diary.app.demo.databinding.ActivityPolicyBinding

class PolicyActivity : BaseActivity<ActivityPolicyBinding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityPolicyBinding {
        return ActivityPolicyBinding.inflate(layoutInflater)
    }
    override val themeindex: Int = 1

    override fun initViews() {
        super.initViews()

        val policyUrl = getString(R.string.privacy_policy_url)

        mBinding.linkpolicy.text = policyUrl

        mBinding.linkpolicy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(policyUrl)
            }
            startActivity(intent)
        }

        mBinding.btnBack.setOnClickListener {
            finish()
        }
    }



}