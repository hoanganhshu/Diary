package com.diary.app.demo.ui.language

import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.diary.app.demo.databinding.ActivityLanguageBinding
import com.diary.app.demo.ui.BaseActivity
import com.diary.app.demo.util.LocaleHelper
import com.diary.app.demo.R

class LanguageActivity : BaseActivity<ActivityLanguageBinding>() {
    private lateinit var radios : List<RadioButton>
    private  var apply : String="en"


    override fun getLayoutActivity(): Int = R.layout.activity_language

    override fun initViews() {
        super.initViews()

        radios =listOf(
            mBinding.rbDeDE,
            mBinding.rbEnUS,
            mBinding.rbEnGB,
            mBinding.rbEnIN,
            mBinding.rbEsES,
            mBinding.rbEsMX,
            mBinding.rbFrFR,
            mBinding.rbHiIN,
            mBinding.rbJaJP,
            mBinding.rbPtBR,
            mBinding.rbPtEU,
            mBinding.rbPtPT,
            mBinding.rbViVN
        )
        bind(mBinding.rbEnUS, "en")
        bind(mBinding.rbEnGB, "en")
        bind(mBinding.rbEnIN, "en")

        bind(mBinding.rbEsES, "es-ES")
        bind(mBinding.rbEsMX, "es-ES")

        bind(mBinding.rbPtPT, "pt-PT")
        bind(mBinding.rbPtEU, "pt-PT")
        bind(mBinding.rbPtBR, "pt-PT")

        bind(mBinding.rbHiIN, "hi-IN")
        bind(mBinding.rbFrFR, "fr-FR")
        bind(mBinding.rbJaJP, "ja-JP")
        bind(mBinding.rbDeDE, "de-DE")

        bind(mBinding.rbViVN, "vi")

        mBinding.btnDone.setOnClickListener {
            applyLanguage(apply)
            finish()
        }
        mBinding.cardEnglish.setOnClickListener { toggle(mBinding.containerEnglish) }
        mBinding.cardSpanish.setOnClickListener { toggle(mBinding.containerSpanish) }
        mBinding.cardPortuguese.setOnClickListener { toggle(mBinding.containerPortuguese) }
        syncUIWithCurrentLocale()
    }
    private fun toggle(container: View) {
        container.visibility = if (container.visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }

    private fun bind(rb : RadioButton, tag : String){
        rb.setOnClickListener {
            radios.forEach {
                it.isChecked = (it == rb)
            }
            apply=tag
        }

    }
    private fun syncUIWithCurrentLocale() {
        val current = LocaleHelper.getPersistedLanguage(this)

        fun check(tag: String, rbId: Int) {
            if (current.equals(tag, ignoreCase = true)) {
                radios.forEach { it.isChecked = false }
                findViewById<RadioButton>(rbId).isChecked = true
                apply = tag
            }
        }
        
        // Default check if needed
        check("en", R.id.rbEnUS)
        check("es-ES", R.id.rbEsES)
        check("pt-PT", R.id.rbPtPT)
        check("hi-IN", R.id.rbHiIN)
        check("fr-FR", R.id.rbFrFR)
        check("ja-JP", R.id.rbJaJP)
        check("de-DE", R.id.rbDeDE)
        check("vi", R.id.rbViVN)
        
        // Set the actual tag checked
        val rb = radios.find { it.isChecked }
        if (rb == null) {
            mBinding.rbEnUS.isChecked = true
            apply = "en"
        }
    }

    private fun applyLanguage(tag : String){
        LocaleHelper.setLocale(this, tag)
        recreate()
    }

    override val themeindex: Int =1
}