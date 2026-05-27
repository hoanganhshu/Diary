package com.diary.app.demo.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import com.diary.app.demo.data.local.DiaryEntity
import com.diary.app.demo.data.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: DiaryRepository
) : ViewModel() {

    private val flowdiary = MutableStateFlow(true)
    fun getAllDiaries(): Flow<List<DiaryEntity>> = repo.getAll()



    private val sortNewestFirst = MutableStateFlow(true)
    fun setSortNewestFirst(newest: Boolean) {
        sortNewestFirst.value = newest
    }
    fun getDiarybyId(id: Int) = repo.getDiaryById(id)

    fun getDiarybyDay(day: String) : Flow<List<DiaryEntity>> = repo.getDiaryDaily(day)




    val diaries =
        sortNewestFirst.flatMapLatest { newest ->
            if (newest) repo.getAllByCreatedAtDesc()
            else repo.getAllByCreatedAtAsc()
        }



}