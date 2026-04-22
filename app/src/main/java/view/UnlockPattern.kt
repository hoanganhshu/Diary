package view


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import com.diary.app.demo.BaseActivity
import com.diary.app.demo.R
import com.diary.app.demo.databinding.ActivityUnlockPatternBinding
import dagger.hilt.android.AndroidEntryPoint
import viewmodel.SecurityViewModel


@AndroidEntryPoint

class UnlockPattern : BaseActivity<ActivityUnlockPatternBinding>() {

    private  val viewModel: SecurityViewModel by viewModels()

    private var isSetupPin: Boolean = false
    private var isChangePin: Boolean = false

    private var isConfirmPhase: Boolean = false
    private var firstPin: String? = null

    private var isOldPinVerified: Boolean = false

    private lateinit var pinBuilder : StringBuilder
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityUnlockPatternBinding {
        return ActivityUnlockPatternBinding.inflate(layoutInflater)
    }

    override fun onResume() {
        super.onResume()
        mBinding.main.setBackgroundResource(R.drawable.dot_security)
    }


    override fun initViews() {
        mBinding.main.setBackgroundResource(R.drawable.dot_security)
        pinBuilder = StringBuilder()




        isSetupPin = intent.getBooleanExtra(PassCodeActivity.EXTRA_IS_SETUP_PIN, false)
        isChangePin = intent.getBooleanExtra(PassCodeActivity.CHANGE_PIN_REQUEST, false)
        when {
            isSetupPin -> {
                mBinding.tvForgot.visibility = View.INVISIBLE
                mBinding.tvSkip.visibility = View.INVISIBLE
                mBinding.tvTitle.text = getString(R.string.set_pin)
            }
            isChangePin -> {

                mBinding.tvForgot.visibility = View.INVISIBLE
                mBinding.tvSkip.visibility = View.INVISIBLE
                mBinding.tvTitle.text = getString(R.string.enter_your_pin)
                mBinding.pinErrorText.visibility = View.INVISIBLE
            }
            else -> {

                mBinding.tvForgot.visibility = View.VISIBLE
                mBinding.tvSkip.visibility = View.VISIBLE
                mBinding.tvTitle.text = getString(R.string.input_pin)
                mBinding.pinErrorText.visibility = View.INVISIBLE
            }
        }


        mBinding.tvForgot.setOnClickListener {
            val intent = Intent(this, SecurityQuestionActivity::class.java)
            intent.putExtra(SecurityQuestionActivity.EXTRA_MODE, SecurityQuestionActivity.MODE_RECOVER)
            startActivity(intent)

        }


        mBinding.tvSkip.setOnClickListener {

        }
        setupKeypad()
        updateDots()




    }


    private fun addDigit(d: Char) {
        if (pinBuilder.length < 4) {
            pinBuilder.append(d)
            updateDots()

            if (pinBuilder.length == 4) {
                val pin = pinBuilder.toString()

                when {

                    isSetupPin -> {
                        if (!isConfirmPhase) {

                            firstPin = pin
                            isConfirmPhase = true

                            pinBuilder.clear()
                            updateDots()

                            mBinding.pinErrorText.visibility = View.INVISIBLE
                            mBinding.tvTitle.text = getString(R.string.confirm_pin)
                        } else {

                            if (pin == firstPin) {
                                viewModel.savePin(pin)
                                mBinding.pinErrorText.visibility = View.INVISIBLE
                                finish()
                            } else {
                                mBinding.pinErrorText.text = getString(R.string.pin_dont_match)
                                mBinding.pinErrorText.visibility = View.VISIBLE
                                mBinding.pinErrorText.postDelayed({
                                    mBinding.pinErrorText.visibility = View.INVISIBLE
                                }, 2000)

                                firstPin = null
                                isConfirmPhase = false
                                pinBuilder.clear()
                                updateDots()
                                mBinding.tvTitle.text = getString(R.string.set_pin)
                            }
                        }
                    }


                    isChangePin -> {
                        if (!isOldPinVerified) {

                            val ok = viewModel.verifyPin(pin)
                            if (ok) {
                                isOldPinVerified = true
                                isConfirmPhase = false
                                firstPin = null

                                pinBuilder.clear()
                                updateDots()

                                mBinding.pinErrorText.visibility = View.INVISIBLE
                                mBinding.tvTitle.text = getString(R.string.enter_new_pin)
                            } else {
                                mBinding.pinErrorText.text = getString(R.string.incorrect_pin)
                                mBinding.pinErrorText.visibility = View.VISIBLE
                                mBinding.pinErrorText.postDelayed({
                                    mBinding.pinErrorText.visibility = View.INVISIBLE
                                }, 2000)

                                pinBuilder.clear()
                                updateDots()
                            }
                        } else {

                            if (!isConfirmPhase) {

                                firstPin = pin
                                isConfirmPhase = true

                                pinBuilder.clear()
                                updateDots()

                                mBinding.pinErrorText.visibility = View.INVISIBLE
                                mBinding.tvTitle.text = getString(R.string.confirm_pin)
                            } else {

                                if (pin == firstPin) {
                                    viewModel.savePin(pin)
                                    mBinding.pinErrorText.visibility = View.INVISIBLE
                                    finish()
                                } else {
                                    mBinding.pinErrorText.text = getString(R.string.pin_dont_match)
                                    mBinding.pinErrorText.visibility = View.VISIBLE
                                    mBinding.pinErrorText.postDelayed({
                                        mBinding.pinErrorText.visibility = View.INVISIBLE
                                    }, 2000)

                                    firstPin = null
                                    isConfirmPhase = false
                                    pinBuilder.clear()
                                    updateDots()
                                    mBinding.tvTitle.text = getString(R.string.enter_your_pin)
                                }
                            }
                        }
                    }


                    else -> {
                        val ok = viewModel.verifyPin(pin)
                        if (ok) {
                            mBinding.pinErrorText.visibility = View.INVISIBLE
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        } else {
                            mBinding.pinErrorText.text = getString(R.string.pin_dont_match)
                            mBinding.pinErrorText.visibility = View.VISIBLE
                            mBinding.pinErrorText.postDelayed({
                                mBinding.pinErrorText.visibility = View.INVISIBLE
                            }, 2000)
                            pinBuilder.clear()
                            updateDots()
                        }
                    }
                }
            }
        }
    }



    private fun updateDots() {
        val dots = listOf(mBinding.dot1, mBinding.dot2, mBinding.dot3, mBinding.dot4)

        dots.forEachIndexed { index, v ->
            val filled = index < pinBuilder.length
            v.setBackgroundResource(if (filled) R.drawable.dot_filled else R.drawable.dot_empty)


            val target = if (filled) 1.15f else 1.0f
            v.animate().scaleX(target).scaleY(target).setDuration(120).start()
        }
    }

    private fun setupKeypad() {

        mBinding.key0.setOnClickListener { addDigit('0') }
        mBinding.key1.setOnClickListener { addDigit('1') }
        mBinding.key2.setOnClickListener { addDigit('2') }
        mBinding.key3.setOnClickListener { addDigit('3') }
        mBinding.key4.setOnClickListener { addDigit('4') }
        mBinding.key5.setOnClickListener { addDigit('5') }
        mBinding.key6.setOnClickListener { addDigit('6') }
        mBinding.key7.setOnClickListener { addDigit('7') }
        mBinding.key8.setOnClickListener { addDigit('8') }
        mBinding.key9.setOnClickListener { addDigit('9') }


        mBinding.keyDel.setOnClickListener {
            deleteDigit()
        }
}
    private fun deleteDigit() {
        if (pinBuilder.isNotEmpty()) {
            pinBuilder.deleteAt(pinBuilder.length - 1)
            updateDots()
        }
    }








}