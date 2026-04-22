package view

import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.diary.app.demo.BaseActivity
import com.diary.app.demo.adapter.MoodGalleryAdapter
import com.diary.app.demo.databinding.ActivityMoodGalleryBinding
import dagger.hilt.android.AndroidEntryPoint
import viewmodel.MoodGalleryViewModels

@AndroidEntryPoint

class MoodGallery : BaseActivity<ActivityMoodGalleryBinding>() {

    private lateinit var adapter: MoodGalleryAdapter

    private  val viewModel: MoodGalleryViewModels by viewModels()

    override val themeindex: Int =1

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityMoodGalleryBinding {
        return ActivityMoodGalleryBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()
        adapter = MoodGalleryAdapter { position ->
            adapter.setSelectedPosition(position)
            viewModel.saveMoodGalleryPosition(position)
        }
        mBinding.reyclerviewmooodgallery.layoutManager = LinearLayoutManager(this)
        mBinding.reyclerviewmooodgallery.adapter = adapter






        adapter.submitList(viewModel.getMoodsList())
        mBinding.btnApplyMoods.setOnClickListener {
            adapter.getSelectedPositionOrNull()?.let { pos ->
                adapter.setAppliedPosition(pos)
                viewModel.saveMoodGalleryPosition(pos)
            }

        }
        mBinding.btnBack.setOnClickListener {
            finish()
        }




    }
}