package com.diary.app.demo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.diary.app.demo.database.dao.TodoDaoDiary

@Database(entities = [DiaryEntity::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SaveDiary : RoomDatabase() {
    abstract fun todoDao(): TodoDaoDiary
}
