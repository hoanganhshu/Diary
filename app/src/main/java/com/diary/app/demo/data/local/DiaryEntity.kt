package com.diary.app.demo.data.local

// Converters cùng package thì có thể bỏ import, nhưng an toàn cứ để:
import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "Diary")
@TypeConverters(Converters::class)
data class DiaryEntity(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    @ColumnInfo(name = "title") val title : String,
    @ColumnInfo(name = "description") val description : String,
    @ColumnInfo(name = "uri_list_image") val uriListImage: List<Uri>,
    @ColumnInfo(name = "theme") val theme : Int?=null,
    @ColumnInfo(name = "emoji") val emoji : Int,
    @ColumnInfo(name = "day") val day : String,
    @ColumnInfo(name = "textstyle") val style : Int,
    @ColumnInfo(name = "textcolor") val color : Int,
    @ColumnInfo(name = "textalign") val align : Int,
    @ColumnInfo(name = "createdAt") val createdAt: Long

)
