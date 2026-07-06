package com.diary.app.demo.ui.home

import android.content.Intent
import android.view.Gravity
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.diary.app.demo.R
import com.diary.app.demo.databinding.ActivityHomeBinding
import com.diary.app.demo.ui.diary.AddDiary
import com.diary.app.demo.ui.BaseActivity
import com.diary.app.demo.ui.calendar.CalendarActivity
import com.diary.app.demo.ui.home.fragment.DiaryFragment
import com.diary.app.demo.ui.home.fragment.MineFragment
import com.diary.app.demo.ui.search.SearchActivity
import com.diary.app.demo.ui.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    private val homeViewModel: HomeViewModel by viewModels()

    override fun getLayoutActivity(): Int = R.layout.activity_home

    override fun initViews() {
        val dropmenu = PopupMenu(
            ContextThemeWrapper(this, R.style.MyPopupMenu),
            mBinding.topAppBar.findViewById(R.id.appbar_threedots)
        )
        dropmenu.menuInflater.inflate(R.menu.calendar_menu, dropmenu.menu)
        var isNewest = true

        // Xử lý system bar insets để bottomNav và curveBg sát đáy màn hình
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Cập nhật chiều cao và padding của curveBg
            val curveParams = mBinding.curveBg.layoutParams
            val density = resources.displayMetrics.density
            curveParams.height = (72 * density).toInt() + systemBars.bottom
            mBinding.curveBg.layoutParams = curveParams
            mBinding.curveBg.setPadding(0, 0, 0, systemBars.bottom)

            // Cập nhật padding bottom của bottomNav
            mBinding.bottomNav.updatePadding(
                bottom = systemBars.bottom
            )

            // Cập nhật margin bottom của nút center "+"
            val centerParams = mBinding.centerButton.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
            centerParams.bottomMargin = (32 * density).toInt() + systemBars.bottom
            mBinding.centerButton.layoutParams = centerParams

            insets
        }

        // Nút FAB center
        mBinding.centerButton.setOnClickListener {
            val intent = Intent(this, AddDiary::class.java)
            intent.putExtra("show", true)
            startActivity(intent)
        }

        // Mặc định load DiaryFragment
        if (supportFragmentManager.findFragmentById(R.id.container) == null) {
            replaceFragment(DiaryFragment())
            mBinding.topAppBar.title = getString(R.string.my_diary)
            mBinding.bottomNav.selectedItemId = R.id.nav_diary
        }

        // Bottom navigation listener
        mBinding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_diary -> {
                    replaceFragment(DiaryFragment())
                    mBinding.topAppBar.title = getString(R.string.my_diary)
                    true
                }
                R.id.nav_mine -> {
                    replaceFragment(MineFragment())
                    mBinding.topAppBar.title = getString(R.string.mine)
                    true
                }
                R.id.nav_center -> {
                    mBinding.centerButton.performClick()
                    false
                }
                else -> false
            }
        }

        // Calendar button
        mBinding.btnCalendar.setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
        }
        mBinding.tvCalendar.setOnClickListener { mBinding.btnCalendar.performClick() }
        mBinding.ivCalendar.setOnClickListener { mBinding.btnCalendar.performClick() }

        // Toolbar menu
        fun updateIcons() {
            dropmenu.menu.findItem(R.id.newest)?.let {
                it.isVisible = true
                it.isChecked = isNewest
            }
            dropmenu.menu.findItem(R.id.oldest)?.let {
                it.isVisible = true
                it.isChecked = !isNewest
            }
        }

        mBinding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.appbar_search -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                }
                R.id.appbar_threedots -> {
                    dropmenu.gravity = Gravity.BOTTOM
                    dropmenu.setOnMenuItemClickListener { subItem ->
                        when (subItem.itemId) {
                            R.id.oldest -> {
                                isNewest = false
                                updateIcons()
                                homeViewModel.setSortNewestFirst(false)
                            }
                            R.id.newest -> {
                                isNewest = true
                                updateIcons()
                                homeViewModel.setSortNewestFirst(true)
                            }
                        }
                        true
                    }
                    updateIcons()
                    dropmenu.show()
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}