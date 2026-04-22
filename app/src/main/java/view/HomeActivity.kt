package view

import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.diary.app.demo.BaseActivity
import com.diary.app.demo.R
import com.diary.app.demo.databinding.ActivityHomeBinding
import com.diary.app.demo.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity :  BaseActivity<ActivityHomeBinding>() {
    private val homeViewModel: HomeViewModel by viewModels()


    override fun inflateBinding(layoutInflater: LayoutInflater) =
        ActivityHomeBinding.inflate(layoutInflater)


    override fun initViews() {



        val dropmenu = PopupMenu(
            ContextThemeWrapper(this,R.style.MyPopupMenu),
            mBinding.topAppBar.findViewById(R.id.appbar_threedots)
        )

        dropmenu.menuInflater.inflate(R.menu.calendar_menu, dropmenu.menu)
        var isNewest=true
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            mBinding.curveBg.updatePadding(
                bottom = systemBars.bottom
            )

            mBinding.bottomNav.updatePadding(
                bottom = systemBars.bottom
            )

            insets
        }


        mBinding.centerButton.setOnClickListener {
            val intent= Intent(this, AddDiary::class.java)
            intent.putExtra("show",true)
            startActivity(intent)
        }
        if (supportFragmentManager.findFragmentById(R.id.container) == null) {
            replaceFragment(DiaryFragment())
            mBinding.topAppBar.title = "My Diary"
            mBinding.bottomNav.selectedItemId = R.id.nav_diary
        }

        mBinding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_diary -> {
                    replaceFragment(DiaryFragment())
                    mBinding.topAppBar.title="My Diary"
                    true
                }
                R.id.nav_mine -> {
                    replaceFragment(MineFragment())
                    mBinding.topAppBar.title="Mine"
                    true
                }
                R.id.nav_center -> {
                    mBinding.centerButton.performClick()
                    false
                }
                else -> false

            }

        }
        mBinding.btnCalendar.setOnClickListener {
            val intent= Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }
        mBinding.tvCalendar.setOnClickListener {
            mBinding.btnCalendar.performClick()
        }


        mBinding.ivCalendar.setOnClickListener {
            mBinding.btnCalendar.performClick()
        }
        fun updateIcons() {
            val newest = dropmenu.menu.findItem(R.id.newest)
            val oldest = dropmenu.menu.findItem(R.id.oldest)

            newest.isVisible = true
            oldest.isVisible = true

            newest.isChecked = isNewest
            oldest.isChecked = !isNewest



        }



        mBinding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.appbar_search -> {
                    val intent=Intent(this, SearchActivity::class.java)
                    startActivity(intent)
                }
                R.id.appbar_threedots -> {
                    dropmenu.gravity= Gravity.BOTTOM
                    dropmenu.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.oldest -> {
                                isNewest=false
                                updateIcons()
                                homeViewModel.setSortNewestFirst(isNewest)

                                true
                            }
                            R.id.newest -> {
                                isNewest=true
                                updateIcons()
                                homeViewModel.setSortNewestFirst(isNewest)
                                true
                            }

                        }
                        true}

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








