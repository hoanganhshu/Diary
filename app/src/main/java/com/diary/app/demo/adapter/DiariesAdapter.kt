package com.diary.app.demo.adapter


import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.diary.app.demo.R
import com.diary.app.demo.data.local.DiaryEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DiariesAdapter( private val callback : (DiaryEntity)-> Unit) : Adapter<DiariesAdapter.DiariesViewHolder> (){
    private val items = mutableListOf<DiaryEntity>()


    override fun onBindViewHolder(holder: DiariesViewHolder, position : Int) {
        val diary =items[position]
        holder.bind(diary)
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            private var last = 0L
            override fun onClick(v: View) {
                val now = SystemClock.elapsedRealtime()
                if (now - last < 500) return
                last = now
                callback(diary)
            }
        })
        val divider = holder.itemView.findViewById<View>(R.id.divider)

        divider.visibility =
            if (position == items.lastIndex) View.GONE else View.VISIBLE


    }

    override fun getItemCount(): Int {
        return  items.size
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DiariesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_diary, parent, false)
         return DiariesViewHolder(view)
    }
    inner class DiariesViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview){
        private val title : TextView=itemview.findViewById(R.id.edtTitlelist)
        private val content : TextView=itemview.findViewById(R.id.edtContentlist)
        private val day : TextView=itemview.findViewById(R.id.day)
        private val month : TextView=itemview.findViewById(R.id.month)
        private val emoji : ImageView=itemview.findViewById(R.id.emojilist)
        private val imageList : RecyclerView=itemview.findViewById(R.id.rvImageslist)

        private val imgBackground: ImageView = itemView.findViewById(R.id.imgItemBackground)




        fun bind(item : DiaryEntity){
            title.text = item.title
            content.text = item.description
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                val date = dateFormat.parse(item.day) ?: Date(item.createdAt)
                val appLocales = AppCompatDelegate.getApplicationLocales()
                val locale = appLocales.get(0) ?: Locale("en", "US")
                Log.d("BASE_ADAPTER", "${date}")

                val dayText = SimpleDateFormat("dd", locale).format(date)
                val monthYearText = SimpleDateFormat("MMM yyyy", locale).format(date)

                day.text = dayText
                Log.d("BASE_ADAPTER", "${dayText}")
                month.text = monthYearText
            } catch (e: Exception) {

                android.util.Log.e("DiariesAdapter", "Error parsing date: ${item.day}", e)
                val date = Date(item.createdAt)
                val dayText = SimpleDateFormat("dd", Locale.ENGLISH).format(date)
                val monthYearText = SimpleDateFormat("MMM yyyy", Locale.ENGLISH).format(date)
                day.text = dayText
                month.text = monthYearText
            }

            title.setTextColor(item.color)
            content.setTextColor(item.color)
            day.setTextColor(item.color)
            month.setTextColor(item.color)
            title.setOnTouchListener { _, _ -> false }
            content.setOnTouchListener { _, _ -> false }





            if (item.emoji != 0) {
                emoji.visibility = View.VISIBLE
                emoji.setImageResource(item.emoji)
            } else {
                emoji.visibility = View.GONE
            }


          val themeRes = item.theme
            if (themeRes != null && themeRes != 0) {
                imgBackground.visibility = View.VISIBLE
                imgBackground.setImageResource(themeRes)
                imgBackground.visibility = View.VISIBLE
            }else{
               imgBackground.setImageDrawable(null)
              imgBackground.setBackgroundColor(android.graphics.Color.WHITE)
            }





            val imageListAdapter = ImagesAdapter()
            imageList.layoutManager = LinearLayoutManager(
                itemView.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            imageList.adapter = imageListAdapter
            if (item.uriListImage.isNotEmpty()) {
                imageList.visibility = View.VISIBLE

                imageListAdapter.submitList(item.uriListImage)
                imageList.post {
                    for (i in 0 until imageList.childCount) {
                        val child = imageList.getChildAt(i)
                        child?.isClickable = false
                        child?.isFocusable = false
                        child?.setOnTouchListener { _, _ -> false }
                    }
                }
            } else {
                imageList.visibility = View.GONE
            }
            imageList.isClickable = false
            imageList.isFocusable = false
            imageList.isFocusableInTouchMode = false
            imageList.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
            imageList.setOnTouchListener { _, _ -> false}



            if (item.style != 0) {
                val typeface = ResourcesCompat.getFont(itemView.context, item.style)
                title.typeface = typeface
                content.typeface = typeface
                day.typeface = typeface
                month.typeface = typeface

            }
        }


    }
    fun submitList(newItems: List<DiaryEntity>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}