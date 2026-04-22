package viewmodel

import androidx.lifecycle.ViewModel
import com.diary.app.demo.MoodGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import model.MoodGalleryModels
import javax.inject.Inject


@HiltViewModel


class MoodGalleryViewModels@Inject constructor(private val model : MoodGalleryModels) : ViewModel() {


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