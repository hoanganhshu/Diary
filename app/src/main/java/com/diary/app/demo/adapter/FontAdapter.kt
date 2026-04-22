package com.diary.app.demo.com.diary.app.demo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.diary.app.demo.R

class FontAdapter(
    private val items: List<Int>,

    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<FontAdapter.FontViewHolder>() {

//    private val items: MutableList<Int> = mutableListOf()
//    fun submitData(list:List<Int>){
//        return newList = list.filter { it == 2 }
//    }

    class FontViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.imgstylefont)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_style_font, parent, false)
        return FontViewHolder(view)
    }

    override fun onBindViewHolder(holder: FontViewHolder, position: Int) {
        val fontResId = items[position]
        holder.img.setImageResource(fontResId)



        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount() = items.size
}
