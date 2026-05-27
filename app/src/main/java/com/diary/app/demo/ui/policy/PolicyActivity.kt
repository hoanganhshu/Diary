package com.diary.app.demo.ui.policy
import com.diary.app.demo.R

import android.content.Intent
import android.net.Uri
import com.diary.app.demo.databinding.ActivityPolicyBinding
import com.diary.app.demo.ui.BaseActivity

class PolicyActivity : BaseActivity<ActivityPolicyBinding>() {
    override val themeindex: Int = 1

    override fun getLayoutActivity(): Int = R.layout.activity_policy

    override fun initViews() {
        super.initViews()

        val policyUrl = "https://www.facebook.com/chinchin891?locale=vi_VN"

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