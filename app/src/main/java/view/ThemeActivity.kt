package view

import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import com.diary.app.demo.BaseActivity
import com.diary.app.demo.R
import com.diary.app.demo.databinding.ActivityThemeBinding

class ThemeActivity : BaseActivity<ActivityThemeBinding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityThemeBinding {
        return ActivityThemeBinding.inflate(layoutInflater)
    }

    override val themeindex: Int = 1
    private var currentTheme :Int = 0
    private var indec: Int = 0

    override fun initViews() {
        currentTheme = getCurrentTheme()

        val themeApply = listOf(
            AppThemes.DEFAULT,
            AppThemes.FLOWERS,
            AppThemes.FRUIT,
            AppThemes.COLOR
        )

        val themes = listOf(
            R.drawable.themedemo,
            R.drawable.themedemo1,
            R.drawable.themedemo2,
            R.drawable.themedemo3
        )
        val themesreal=listOf(
            R.drawable.fix1,
            R.drawable.fix2,
            R.drawable.fix3,
            R.drawable.fix4
        )


        val currentIndex = themeApply.indexOf(currentTheme).let { if (it >= 0) it else 0 }
        indec = currentIndex


        mBinding.imgPreview.setImageResource(themes[currentIndex])
        mBinding.root.setBackgroundResource(themesreal[currentIndex])


        mBinding.layoutThemes.forEach { it.foreground = null }
        mBinding.layoutThemes.getChildAt(currentIndex)?.foreground =
            ContextCompat.getDrawable(this, R.drawable.bg_selected_border)

        mBinding.btnApply.text = getString(R.string.applied)
        mBinding.layoutThemes.forEachIndexed { index, itemView ->
            itemView.setOnClickListener {
                mBinding.imgPreview.setImageResource(themes[index])
                mBinding.root.setBackgroundResource(themesreal[index])

                mBinding.layoutThemes.forEach { v -> v.foreground = null }
                itemView.foreground = ContextCompat.getDrawable(this, R.drawable.bg_selected_border)

                mBinding.btnApply.text =
                    if (currentIndex == index) getString(R.string.applied) else getString(R.string.apply)

                indec = index
            }
        }

        mBinding.btnApply.setOnClickListener {
            setCurrentTheme(themeApply[indec])
            finish()
        }

        mBinding.btnBack.setOnClickListener { finish() }
    }

}
