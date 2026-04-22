package com.diary.app.demo.database.repo

import com.diary.app.demo.database.DiaryEntity
import com.diary.app.demo.database.dao.TodoDaoDiary

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class repoDiary @Inject constructor(private val todoDaoDiary: TodoDaoDiary) {
    fun getAll() : Flow<List<DiaryEntity>> = todoDaoDiary.getAll()

    fun getDiaryDaily( day : String) : Flow<List<DiaryEntity>> =todoDaoDiary.getDiaryDay(day)
    suspend fun saveDiary(diary: DiaryEntity) {
        todoDaoDiary.saveDiary(diary)
    }
    suspend fun deleteDiary(id: Int) {
        todoDaoDiary.deleteDiary(id)
    }
    fun getAllByCreatedAtDesc(): Flow<List<DiaryEntity>> = todoDaoDiary.getAllByCreatedAtDesc()
    fun getAllByCreatedAtAsc(): Flow<List<DiaryEntity>> = todoDaoDiary.getAllByCreatedAtAsc()

    fun getAllByDayDesc(): Flow<List<DiaryEntity>> = todoDaoDiary.getAllByDayDesc()
    fun getAllByDayAsc(): Flow<List<DiaryEntity>> = todoDaoDiary.getAllByDayAsc()

    fun getDiaryById(id: Int): Flow<DiaryEntity> = todoDaoDiary.getDiaryById(id)



}