package com.diary.app.demo.data.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.diary.app.demo.data.local.MoodGroup
import com.diary.app.demo.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MoodGalleryRepository@Inject constructor(@ApplicationContext private  val context : Context) {
    val PREF_NAME="mood_gallery"
    val emojiList1 = listOf(R.drawable.emoj1_new, R.drawable.emoj2_new, R.drawable.emoj3_new, R.drawable.emoj4_new, R.drawable.emoj5_new, R.drawable.emoj6_new, R.drawable.emoj7_new, R.drawable.emoj8_new)
    val emojiList2 = listOf(R.drawable.chick_1_new, R.drawable.chick_2_new, R.drawable.chick_3_new, R.drawable.chick_4_new, R.drawable.chick_5_new, R.drawable.chick_6_new, R.drawable.chick_7_new, R.drawable.chick_8_new)
    val emojiList3 = listOf(R.drawable.kitten_1_new, R.drawable.kitten_2_new, R.drawable.kitten_3_new, R.drawable.kitten_4_new, R.drawable.kitten_5_new, R.drawable.kitten_6_new, R.drawable.kitten_7_new, R.drawable.kitten_8_new)
    val emojiList4=listOf(R.drawable.emoji2_1_new, R.drawable.emoji2_2_new, R.drawable.emoji2_3_new, R.drawable.emoji2_4_new, R.drawable.emoji2_5_new, R.drawable.emoji2_6_new, R.drawable.emoji2_7_new, R.drawable.emoji2_8_new)
    val emojiList5=listOf(R.drawable.babystar_1_new, R.drawable.babystar_2_new, R.drawable.babystar_3_new, R.drawable.babystar_4_new, R.drawable.babystar_5_new, R.drawable.babystar_6_new, R.drawable.babystar_7_new, R.drawable.babystar_8_new)
    val emojiList6=listOf(R.drawable.rabit_1_new, R.drawable.rabit_2_new, R.drawable.rabit_3_new, R.drawable.rabit_4_new, R.drawable.rabit_5_new, R.drawable.rabit_6_new, R.drawable.rabit_7_new, R.drawable.rabit_8_new)
    val emojiList7=listOf(R.drawable.rabit2_1_new, R.drawable.rabit2_2_new, R.drawable.rabit2_3_new, R.drawable.rabit2_4_new, R.drawable.rabit2_5_new, R.drawable.rabit2_6_new, R.drawable.rabit2_7_new, R.drawable.rabit2_8_new)
    val emojiList8=listOf(R.drawable.puppy_1_new, R.drawable.puppy_2_new, R.drawable.puppy_3_new, R.drawable.puppy_4_new, R.drawable.puppy_5_new, R.drawable.puppy_6_new, R.drawable.puppy_7_new, R.drawable.puppy_8_new)
    val emojiList9=listOf(R.drawable.girlmonster_1_new, R.drawable.girlmonster_2_new, R.drawable.girlmonster_3_new, R.drawable.girlmonster_4_new, R.drawable.girlmonster_5_new, R.drawable.girlmonster_6_new, R.drawable.girlmonster_7_new, R.drawable.girlmonster_8_new)
    val emojiList10=listOf(R.drawable.babygirl1_new, R.drawable.babygirl2_new, R.drawable.babygirl_3_new, R.drawable.babygirl_4_new, R.drawable.babygirl_5_new, R.drawable.babygirl_6_new, R.drawable.babygirl_7_new, R.drawable.babygirl_8_new)
    val emojiList11=listOf(R.drawable.orange_1_new, R.drawable.orange_2_new, R.drawable.orange_3_new, R.drawable.orange_4_new, R.drawable.orange_5_new, R.drawable.orange_6_new, R.drawable.orange_7_new, R.drawable.orange_8_new)
    val emojiList12=listOf(R.drawable.puppy2_1_new, R.drawable.puppy2_2_new, R.drawable.puppy2_3_new, R.drawable.puppy2_4_new, R.drawable.puppy2_5_new, R.drawable.puppy2_6_new, R.drawable.puppy2_7_new, R.drawable.puppy2_8_new)

    val moodGroups = listOf(
        MoodGroup("Default", emojiList1),
        MoodGroup("Chick", emojiList2),
        MoodGroup("Kitten", emojiList3),
        MoodGroup("Emoji #2", emojiList4),
        MoodGroup("Baby Star", emojiList5),
        MoodGroup("Rabbit", emojiList6),
        MoodGroup("Rabbit #2", emojiList7),
        MoodGroup("Puppy ", emojiList8),
        MoodGroup("Girl Monster", emojiList9),
        MoodGroup("Baby Girl", emojiList10),
        MoodGroup("Orange ", emojiList11),
        MoodGroup("Puppy #2", emojiList12)

    )
    private val prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)

    fun saveMoodGalleryPosition(position: Int) {

        prefs.edit().putInt("mood_gallery_position", position).apply()
    }

    fun getMoodGalleryPosition(): Int {
        return prefs.getInt("mood_gallery_position", 0)
    }
    fun getMoods(): MoodGroup {
        return moodGroups[getMoodGalleryPosition()]
    }
    fun getMoodsList() : List<MoodGroup>{
        return moodGroups
    }
}

