package com.diary.app.demo.data.local

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    // List<Uri> -> String (JSON)
    @TypeConverter
    fun fromUriList(uriList: List<Uri>): String {
        val uriStringList = uriList.map { it.toString() }
        return Gson().toJson(uriStringList)
    }

    // String (JSON) -> List<Uri>
    @TypeConverter
    fun toUriList(data: String): List<Uri> {
        val stringList: List<String> =
            Gson().fromJson(data, object : TypeToken<List<String>>() {}.type)
        return stringList.map { Uri.parse(it) }
    }

    // Uri đơn -> String
    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }

    // String -> Uri đơn
    @TypeConverter
    fun toUri(uriString: String?): Uri? {
        return uriString?.let { Uri.parse(it) }
    }
}
