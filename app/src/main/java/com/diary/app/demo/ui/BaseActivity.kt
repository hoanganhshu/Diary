package com.diary.app.demo.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.diary.app.demo.R
import com.diary.app.demo.util.LocaleHelper

abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity(), Themes {

    lateinit var mBinding: VB
    private val KEY_THEME = "current_theme"
    private val PREFS = "app_theme"
    private var lastAppliedThemeId = -1

    private val prefs: SharedPreferences by lazy {
        getSharedPreferences(PREFS, MODE_PRIVATE)
    }

    abstract fun getLayoutActivity(): Int

    open fun initViews() {}

    object AppThemes {
        const val DEFAULT = 0
        const val FLOWERS = 1
        const val FRUIT = 2
        const val COLOR = 3
    }

    open val themeindex: Int = 0

    open fun Themes(): Map<Int, List<Int>> = mapOf(
        AppThemes.DEFAULT to listOf(R.drawable.fix1, R.drawable.fix1),
        AppThemes.FLOWERS to listOf(R.drawable.fix2, R.drawable.bg_1_1),
        AppThemes.FRUIT to listOf(R.drawable.fix3, R.drawable.bg_2_1),
        AppThemes.COLOR to listOf(R.drawable.fix4, R.drawable.bg_3_1)
    )

    override fun setCurrentTheme(themeId: Int) {
        prefs.edit().putInt(KEY_THEME, themeId).apply()
        recreate()
    }

    override fun getCurrentTheme(): Int {
        return prefs.getInt(KEY_THEME, 0)
    }

    override fun applyTheme() {
        val themeId = getCurrentTheme()
        val list = Themes()[themeId]
        mBinding.root.setBackgroundResource(list?.get(themeindex) ?: R.drawable.bg_default)
    }

    override fun setColor(color: Int) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(themeStyleOf(getCurrentTheme()))
        super.onCreate(savedInstanceState)

        val layoutView = getLayoutActivity()
        mBinding = DataBindingUtil.setContentView(this, layoutView)
        mBinding.lifecycleOwner = this
        applyTheme()

        lastAppliedThemeId = getCurrentTheme()
        initViews()
    }

    override fun onResume() {
        super.onResume()
        val cur = getCurrentTheme()
        if (cur != lastAppliedThemeId) {
            lastAppliedThemeId = cur
            recreate()
        }
    }

    protected fun themeStyleOf(id: Int): Int = when (id) {
        AppThemes.DEFAULT -> R.style.Theme_MyApp_Default
        AppThemes.FLOWERS -> R.style.Theme_MyApp_Flowers
        AppThemes.FRUIT -> R.style.Theme_MyApp_Fruit
        AppThemes.COLOR -> R.style.Theme_MyApp_Color
        else -> R.style.Theme_MyApp_Default
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }
}

interface Themes {
    fun getCurrentTheme(): Int
    fun applyTheme()
    fun setCurrentTheme(themeId: Int)
    fun setColor(color: Int)
}
