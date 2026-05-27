package com.diary.app.demo.ui.search
import com.diary.app.demo.R

import android.content.Intent
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.R as AppCompatR
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.diary.app.demo.adapter.DiariesAdapter
import com.diary.app.demo.databinding.ActivitySearchBinding
import com.diary.app.demo.ui.BaseActivity
import com.diary.app.demo.ui.calendar.CalendarActivity
import com.diary.app.demo.ui.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : BaseActivity<ActivitySearchBinding>() {

    private lateinit var diariesAdapter: DiariesAdapter
    private val homeViewModel: HomeViewModel by viewModels()



    override fun onResume() {
        super.onResume()
        mBinding.searchview.clearFocus()
    }
    override fun getLayoutActivity(): Int = R.layout.activity_search

    override fun initViews() {

        diariesAdapter = DiariesAdapter { diary ->
            mBinding.searchview.clearFocus()
            hideKeyBoard()
            val intent = Intent(this, CalendarActivity::class.java)
            intent.putExtra("diary_day", diary.day)
            intent.putExtra("show", false)
            startActivity(intent)
        }
        mBinding.searchview.setIconifiedByDefault(false)
        mBinding.searchview.isIconified = false
        mBinding.searchview.post {
            val searchEditText = mBinding.searchview.findViewById<EditText>(
                AppCompatR.id.search_src_text
            )
            searchEditText?.apply {
                backgroundTintMode = null
                backgroundTintList = null
                setBackgroundColor(Color.TRANSPARENT)
            }}



        mBinding.rvDiariesSearch.adapter = diariesAdapter
        mBinding.rvDiariesSearch.setOnTouchListener { _, _ ->
            mBinding.searchview.clearFocus()
            hideKeyBoard()
            false
        }


        mBinding.searchview.post {
            val closeBtn = mBinding.searchview.findViewById<ImageView>(
                AppCompatR.id.search_close_btn
            )
            closeBtn?.visibility = View.GONE
        }

        mBinding.root.setOnTouchListener { _, _ ->
            mBinding.searchview.clearFocus()
            hideKeyBoard()
            false
        }


        mBinding.btnClear.setOnClickListener {
            mBinding.searchview.setQuery("", false)
            mBinding.searchview.clearFocus()
            hideKeyBoard()
        }



        mBinding.rvDiariesSearch.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = diariesAdapter
        }
        mBinding.searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query(query.orEmpty())
                mBinding.searchview.clearFocus()
                hideKeyBoard()
                return true

            }



            override fun onQueryTextChange(newText: String?): Boolean {
                disableDefaultSearchCloseIcon()
                query(newText.orEmpty())
                return true

            }


        })

    }
    private fun disableDefaultSearchCloseIcon() {
        val closeBtn = mBinding.searchview.findViewById<ImageView>(AppCompatR.id.search_close_btn)
        closeBtn?.apply {

            visibility = View.GONE
            isEnabled = false
            isClickable = false
            setImageDrawable(null)
            setOnClickListener(null)
        }
    }
    private fun hideKeyBoard(){
        val hide = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        hide.hideSoftInputFromWindow(currentFocus?.windowToken ?:mBinding.root.windowToken,0)
    }



    private fun query(query: String) {

        if (query.isBlank()) {
            diariesAdapter.submitList(emptyList())
            mBinding.emptyState.visibility = View.VISIBLE
            mBinding.rvDiariesSearch.visibility = View.GONE
            return
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.getAllDiaries().collect { list ->
                    val filteredDiaries = list.filter { diary ->
                        diary.title.contains(query, true) ||
                                diary.description.contains(query, true) ||
                                diary.day.contains(query, true)
                    }

                    diariesAdapter.submitList(filteredDiaries)
                    val isEmpty = filteredDiaries.isEmpty()
                    mBinding.emptyState.visibility =
                        if (isEmpty) View.VISIBLE else View.GONE
                    mBinding.rvDiariesSearch.visibility =
                        if (isEmpty) View.GONE else View.VISIBLE
                }
            }
        }
    }

}