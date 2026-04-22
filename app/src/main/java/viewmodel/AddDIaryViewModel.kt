package com.diary.app.demo.viewmodel


import android.graphics.Color
import android.net.Uri
import android.view.Gravity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diary.app.demo.database.DiaryEntity
import com.diary.app.demo.database.repo.repoDiary
import com.diary.app.demo.model.AddDiaryModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddDIaryViewModel @Inject constructor(private val repoDiary: repoDiary,private val addDiarymodel: AddDiaryModel) : ViewModel(){


    private  var _selectedFont= MutableStateFlow<Int?>(null)
    val selectFont : StateFlow<Int?> = _selectedFont
    private  var _selectedColor= MutableStateFlow<Int?>(null)
    val selectColor : StateFlow<Int?> = _selectedColor
    private var _selectedEmoji = MutableStateFlow<Int?>(null)
    val selectedEmoji: StateFlow<Int?> = _selectedEmoji



    private  var _selectedBackGround= MutableStateFlow<Int?>(null)
    val selectBackGround : StateFlow<Int?> = _selectedBackGround
    private var _galleryItemHeight = MutableStateFlow<Int>(0)
    val galleryItemHeight: StateFlow<Int?> = _galleryItemHeight

    private val _textAlign = MutableStateFlow(Gravity.START)
    val textAlign = _textAlign

    fun getAll() : Flow<List<DiaryEntity>> {
        return repoDiary.getAll()
    }
    fun getDiaryDaily(day : String ) : Flow<List<DiaryEntity>>{
       return repoDiary.getDiaryDaily(day)

    }
    fun selectFont(fonId : Int){
        _selectedFont.value=fonId
    }
    fun saveDiary(
        id: Int,
        title: String,
        description: String,
        imageList: List<Uri>,
        theme: Int?,
        emoji: Int,
        day: String,
        style: Int,
        color: Int,
        align: Int,
        createdAt : Long
    ) {
        viewModelScope.launch {

            val paths: List<String> = addDiarymodel.copyImagesFromUriList(imageList)
            val finalEmoji = (if (emoji != 0) emoji else _selectedEmoji.value) ?: 0


            val localUris: List<Uri> = paths.map { path ->
                Uri.fromFile(File(path))
            }


            val diary = DiaryEntity(
                id=id,
                title = title,
                description = description,
                uriListImage = localUris,
                theme = theme,
                emoji = finalEmoji,
                day = day,
                style = style,
                color = color,
                align = align,
                createdAt = createdAt
            )


            repoDiary.saveDiary(diary)
        }
    }
    fun calculateGalleryItemHeight(recyclerViewWidth: Int, density: Float): Int {
        val spacingPx = (5 * density).toInt()
        val totalSpacing = spacingPx * (4 + 1) // 4 columns + 1 edge spacing
        val itemWidth = (recyclerViewWidth - totalSpacing) / 4
        _galleryItemHeight.value = itemWidth
        return itemWidth
    }

    fun deleteDiary(id: Int) {
        viewModelScope.launch {
            repoDiary.deleteDiary(id)

    }}
    fun getDiaryById(id: Int): Flow<DiaryEntity?> {
        return repoDiary.getDiaryById(id)
    }






    fun selectAlign(alignment: Int) {
        _textAlign.value = alignment
    }
    fun selectColor(hex: String) {
        _selectedColor.value = Color.parseColor(hex)
    }
    fun setBackGround(id : Int){
        _selectedBackGround.value=id

    }
    fun selectEmoji(resId: Int) {
        _selectedEmoji.value = resId
    }

}