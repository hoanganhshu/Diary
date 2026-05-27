package com.diary.app.demo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.diary.app.demo.data.local.DiaryEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface TodoDaoDiary {
    @Query("Select * from Diary ")
    fun getAll() : Flow<List<DiaryEntity>>

    @Query("Select * from Diary where day=:day")
    fun getDiaryDay(day : String ) : Flow<List<DiaryEntity>>

    @Query("SELECT * FROM Diary ORDER BY day DESC, createdAt DESC")
    fun getAllByCreatedAtDesc(): Flow<List<DiaryEntity>>

    @Query("SELECT * FROM Diary ORDER BY day ASC, createdAt ASC")
    fun getAllByCreatedAtAsc(): Flow<List<DiaryEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDiary(diaryEntity: DiaryEntity)


    @Query("DELETE FROM Diary WHERE id = :id")
    suspend fun deleteDiary(id: Int)

    @Query("SELECT * FROM Diary ORDER BY day DESC")
    fun getAllByDayDesc(): Flow<List<DiaryEntity>>

    @Query("SELECT * FROM Diary ORDER BY day ASC")
    fun getAllByDayAsc(): Flow<List<DiaryEntity>>

    @Query("SELECT * FROM Diary WHERE id = :id LIMIT 1")
    fun getDiaryById(id: Int): Flow<DiaryEntity>

    @Update
    suspend fun updateDiary(diary: DiaryEntity)



}