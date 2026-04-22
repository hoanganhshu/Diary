package view

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.diary.app.demo.BaseActivity
import com.diary.app.demo.R

import com.diary.app.demo.databinding.ActivitySetSecurityQuestionBinding
import com.diary.app.demo.model.SecurityModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SecurityQuestionActivity : BaseActivity<ActivitySetSecurityQuestionBinding>() {


    @Inject lateinit var securityModel: SecurityModel

    override fun inflateBinding(layoutInflater: LayoutInflater)
            = ActivitySetSecurityQuestionBinding.inflate(layoutInflater)

    override fun initViews() {
        super.initViews()
        mBinding.btnBack.setOnClickListener { finish() }


        val questions = listOf(
            getString(R.string.favorite_color),
            getString(R.string.favorite_food),
            getString(R.string.favorite_movie)
        )


        val adapter = ArrayAdapter(this, R.layout.item_dropdown, questions)
        val act =(mBinding.actvQuestion as MaterialAutoCompleteTextView)
        act.setAdapter(adapter)
        act.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        act.setTextColor(ContextCompat.getColor(this, R.color.black))


        val mode = intent.getStringExtra(EXTRA_MODE) ?: MODE_SETUP

        if (mode == MODE_SETUP) {
            mBinding.tvSubtitle.visibility=View.VISIBLE

            fun validate() {
                val q = mBinding.actvQuestion.text?.toString()?.trim().orEmpty()
                val a = mBinding.edtAnswer.text?.toString()?.trim().orEmpty()
                mBinding.btnConfirm.isEnabled = q.isNotEmpty() && a.length >= 2
            }
            mBinding.actvQuestion.doAfterTextChanged { validate() }
            mBinding.edtAnswer.doAfterTextChanged { validate() }
            validate()

            mBinding.btnConfirm.setOnClickListener {
                val q = mBinding.actvQuestion.text?.toString()?.trim().orEmpty()
                val a = mBinding.edtAnswer.text?.toString()?.trim().orEmpty()
                if (q.isNotEmpty() && a.isNotEmpty()) {
                    securityModel.saveSecurityQA(q, a)
                    MaterialAlertDialogBuilder(this)
                        .setMessage(getString(R.string.security_save))
                        .setPositiveButton(getString(R.string.ok)) { d, _ -> d.dismiss(); finish() }
                        .show()
                }
            }

        } else {
            mBinding.tvSubtitle.visibility=View.GONE



            val savedQ = securityModel.getSecurityQuestion()
            if (savedQ.isNullOrEmpty()) {

                mBinding.actvQuestion.isEnabled = false
                mBinding.edtAnswer.isEnabled = false
                mBinding.btnConfirm.isEnabled = false
                return
            } else {
                mBinding.actvQuestion.setText(savedQ, false)
                mBinding.actvQuestion.isEnabled = false
            }


            fun validateRecover() {
                val a = mBinding.edtAnswer.text?.toString()?.trim().orEmpty()
                mBinding.btnConfirm.isEnabled = a.length >= 2
            }
            mBinding.edtAnswer.doAfterTextChanged { validateRecover() }
            validateRecover()

            mBinding.btnConfirm.setOnClickListener {
                val answer = mBinding.edtAnswer.text?.toString()?.trim().orEmpty()
                val savedAnswer = securityModel.getSecurityAnswer()
                val normInput = answer.trim().lowercase()

                if (savedAnswer != null && savedAnswer == normInput) {
                    val pin = securityModel.getSavedPin()
                    if (pin != null) {
                        showPinDialog(pin)
                    } else {
                        MaterialAlertDialogBuilder(this)
                            .setMessage(getString(R.string.set_pin_security))
                            .setPositiveButton(getString(R.string.ok), null)
                            .show()
                    }
                } else {
                    mBinding.wronganswer.visibility = View.VISIBLE

                    lifecycleScope.launch {
                        delay(2000)
                        mBinding.wronganswer.visibility = View.GONE
                    }

                }
            }
        }
    }

    companion object {
        const val EXTRA_MODE = "extra_mode"
        const val MODE_SETUP = "mode_setup"
        const val MODE_RECOVER = "mode_recover"
    }
    private fun showPinDialog(pin: String?) {
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_show_pin, null)

        val tvPin1 = dialogView.findViewById<TextView>(R.id.tvPin1)
        val tvPin2 = dialogView.findViewById<TextView>(R.id.tvPin2)
        val tvPin3 = dialogView.findViewById<TextView>(R.id.tvPin3)
        val tvPin4 = dialogView.findViewById<TextView>(R.id.tvPin4)


        tvPin1.text = pin?.getOrNull(0)?.toString() ?: ""
        tvPin2.text = pin?.getOrNull(1)?.toString() ?: ""
        tvPin3.text = pin?.getOrNull(2)?.toString() ?: ""
        tvPin4.text = pin?.getOrNull(3)?.toString() ?: ""

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

}

