package view

import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.diary.app.demo.BaseActivity
import com.diary.app.demo.databinding.ActivityLanguageBinding

class LanguageActivity : BaseActivity<ActivityLanguageBinding>() {
    private lateinit var radios : List<RadioButton>
    private  var apply : String="en-US"

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityLanguageBinding {
        return ActivityLanguageBinding.inflate(layoutInflater)
    }

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

    private fun bind(rb : RadioButton,tag : String){
        rb.setOnClickListener {
            radios.forEach {
                it.isChecked = (it == rb)
            }
            apply=tag
        }

    }
    private fun syncUIWithCurrentLocale() {
        val current = AppCompatDelegate.getApplicationLocales().toLanguageTags()

        val first = current.split(",").firstOrNull()?.trim().orEmpty()

        fun check(tag: String, rbId: Int) {
            if (first.equals(tag, ignoreCase = true)) {
                radios.forEach { it.isChecked = false }
                findViewById<RadioButton>(rbId).isChecked = true
                apply = tag
            }
        }}
    private fun applyLanguage(tag : String){
        val locales = LocaleListCompat.forLanguageTags(tag)
        AppCompatDelegate.setApplicationLocales(locales)
        recreate()

    }

    override val themeindex: Int =1
}