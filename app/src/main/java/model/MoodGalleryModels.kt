package model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.diary.app.demo.MoodGroup
import com.diary.app.demo.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MoodGalleryModels@Inject constructor(@ApplicationContext private  val context : Context) {
    val PREF_NAME="mood_gallery"
    val emojiList1 = listOf(R.drawable.emoj1, R.drawable.emoj2, R.drawable.emoj3, R.drawable.emoj4, R.drawable.emoj5, R.drawable.emoj6, R.drawable.emoj7, R.drawable.emoj8)
    val emojiList2 = listOf(R.drawable.chick_1, R.drawable.chick_2, R.drawable.chick_3, R.drawable.chick_4, R.drawable.chick_5, R.drawable.chick_6, R.drawable.chick_7, R.drawable.chick_8)
    val emojiList3 = listOf(R.drawable.kitten_1, R.drawable.kitten_2, R.drawable.kitten_3, R.drawable.kitten_4, R.drawable.kitten_5, R.drawable.kitten_6, R.drawable.kitten_7, R.drawable.kitten_8)
    val emojiList4=listOf(R.drawable.emoji2_1, R.drawable.emoji2_2, R.drawable.emoji2_3, R.drawable.emoji2_4, R.drawable.emoji2_5, R.drawable.emoji2_6, R.drawable.emoji2_7, R.drawable.emoji2_8)
    val emojiList5=listOf(R.drawable.babystar_1, R.drawable.babystar_2, R.drawable.babystar_3, R.drawable.babystar_4, R.drawable.babystar_5, R.drawable.babystar_6, R.drawable.babystar_7, R.drawable.babystar_8)
    val emojiList6=listOf(R.drawable.rabit_1, R.drawable.rabit_2, R.drawable.rabit_3, R.drawable.rabit_4, R.drawable.rabit_5, R.drawable.rabit_6, R.drawable.rabit_7, R.drawable.rabit_8)
    val emojiList7=listOf(R.drawable.rabit2_1, R.drawable.rabit2_2, R.drawable.rabit2_3, R.drawable.rabit2_4, R.drawable.rabit2_5, R.drawable.rabit2_6, R.drawable.rabit2_7, R.drawable.rabit2_8)
    val emojiList8=listOf(R.drawable.puppy_1, R.drawable.puppy_2, R.drawable.puppy_3, R.drawable.puppy_4, R.drawable.puppy_5, R.drawable.puppy_6, R.drawable.puppy_7, R.drawable.puppy_8)
    val emojiList9=listOf(R.drawable.girlmonster_1, R.drawable.girlmonster_2, R.drawable.girlmonster_3, R.drawable.girlmonster_4, R.drawable.girlmonster_5, R.drawable.girlmonster_6, R.drawable.girlmonster_7, R.drawable.girlmonster_8)
    val emojiList10=listOf(R.drawable.babygirl1, R.drawable.babygirl2, R.drawable.babygirl_3, R.drawable.babygirl_4, R.drawable.babygirl_5, R.drawable.babygirl_6, R.drawable.babygirl_7, R.drawable.babygirl_8)
    val emojiList11=listOf(R.drawable.orange_1, R.drawable.orange_2, R.drawable.orange_3, R.drawable.orange_4, R.drawable.orange_5, R.drawable.orange_6, R.drawable.orange_7, R.drawable.orange_8)
    val emojiList12=listOf(R.drawable.puppy2_1, R.drawable.puppy2_2, R.drawable.puppy2_3, R.drawable.puppy2_4, R.drawable.puppy2_5, R.drawable.puppy2_6, R.drawable.puppy2_7, R.drawable.puppy2_8)

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

