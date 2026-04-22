package com.diary.app.demo.com.diary.app.demo.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.diary.app.demo.R
import com.google.android.material.card.MaterialCardView

class ColorsAdapter(
    private val colors: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<ColorsAdapter.ColorViewHolder>() {
    private var selectedPosition = RecyclerView.NO_POSITION
    inner class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: MaterialCardView = itemView.findViewById(R.id.cardColor)
        val img: ImageView = itemView.findViewById(R.id.circleImg)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_color, parent, false)
        return ColorViewHolder(view)
    }
    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val hex = colors[position]
        val colorInt = Color.parseColor(hex)
        holder.img.background.setTint(colorInt)
        val isSelected = position == selectedPosition
        holder.card.strokeWidth = if (isSelected) 4 else 0
        holder.itemView.setOnClickListener {
            val oldPosition = selectedPosition
            selectedPosition = holder.adapterPosition

            onItemClick(hex)

            if (oldPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(oldPosition)
            }
            notifyItemChanged(selectedPosition)
        }
    }

    override fun getItemCount() = colors.size
}
