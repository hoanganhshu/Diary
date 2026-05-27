package com.diary.app.demo.ui.moodgalery.viewmodel

import androidx.lifecycle.ViewModel
import com.diary.app.demo.data.local.MoodGroup
import com.diary.app.demo.data.repository.MoodGalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel


class MoodGalleryViewModels@Inject constructor(private val model : MoodGalleryRepository) : ViewModel() {


    fun saveMoodGalleryPosition(position: Int) {
        model.saveMoodGalleryPosition(position)
    }


    fun getMoods(): MoodGroup {
        return model.getMoods()
    }
    fun getMoodsList(): List<MoodGroup> {
        return model.getMoodsList()
    }

}