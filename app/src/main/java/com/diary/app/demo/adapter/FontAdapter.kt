package com.diary.app.demo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.diary.app.demo.R

class FontAdapter(
    private val items: List<Pair<String, Int>>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<FontAdapter.FontViewHolder>() {

    class FontViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFont: TextView = itemView.findViewById(R.id.tvstylefont)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_style_font, parent, false)
        return FontViewHolder(view)
    }

    override fun onBindViewHolder(holder: FontViewHolder, position: Int) {
        val fontItem = items[position]
        holder.tvFont.text = fontItem.first

        try {
            val typeface = ResourcesCompat.getFont(holder.itemView.context, fontItem.second)
            holder.tvFont.typeface = typeface
        } catch (e: Exception) {
            e.printStackTrace()
        }

        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount() = items.size
}
