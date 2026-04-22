package com.diary.app.demo.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diary.app.demo.MoodGroup
import com.diary.app.demo.R


class MoodGalleryAdapter(
    private val onItemClick: (position: Int) -> Unit
) : RecyclerView.Adapter<MoodGalleryAdapter.MoodViewHolder>() {

    private val moodGroups = mutableListOf<MoodGroup>()
    private var selectedPosition = RecyclerView.NO_POSITION
    private var appliedPosition = RecyclerView.NO_POSITION

    fun submitList(list: List<MoodGroup>) {
        moodGroups.clear()
        moodGroups.addAll(list)
        notifyDataSetChanged()
    }

    inner class MoodViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
        private val card = v.findViewById<com.google.android.material.card.MaterialCardView>(R.id.moodCard)
        private val title = v.findViewById<TextView>(R.id.titlemood)
        private val appliedBadge = v.findViewById<TextView>(R.id.appliedBadge)
        private val emojis = listOf(
            v.findViewById<ImageView>(R.id.mood1),
            v.findViewById<ImageView>(R.id.mood2),
            v.findViewById<ImageView>(R.id.mood3),
            v.findViewById<ImageView>(R.id.mood4),
            v.findViewById<ImageView>(R.id.mood5),
            v.findViewById<ImageView>(R.id.mood6),
            v.findViewById<ImageView>(R.id.mood7),
            v.findViewById<ImageView>(R.id.mood8)
        )

        fun bind(group: MoodGroup, position: Int) {

            title.text = group.title
            group.emojiList.forEachIndexed { i, res -> emojis[i].setImageResource(res) }

            card.setOnClickListener { onItemClick(position) }


            val isSelected = position == selectedPosition
            card.strokeWidth = if (isSelected) 3 else 0
            card.strokeColor = if (isSelected)
                v.resources.getColor(R.color.purple_500, v.context.theme)
            else
                android.graphics.Color.TRANSPARENT

            if(title.currentTextColor==Color.WHITE){
                title.setTextColor(Color.BLACK)
            }

            appliedBadge.visibility = if (position == appliedPosition) View.VISIBLE else View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mood_gallery, parent, false)
        return MoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        holder.bind(moodGroups[position], position)
    }

    override fun getItemCount(): Int = moodGroups.size

    fun setSelectedPosition(position: Int) {
        if (position == selectedPosition) return
        val old = selectedPosition
        selectedPosition = position
        if (old != RecyclerView.NO_POSITION) notifyItemChanged(old)
        if (selectedPosition != RecyclerView.NO_POSITION) notifyItemChanged(selectedPosition)
    }

    fun setAppliedPosition(position: Int) {
        if (position == appliedPosition) return
        val old = appliedPosition
        appliedPosition = position
        if (old != RecyclerView.NO_POSITION) notifyItemChanged(old)
        if (appliedPosition != RecyclerView.NO_POSITION) notifyItemChanged(appliedPosition)
    }

    fun getSelectedPositionOrNull(): Int? =
        if (selectedPosition == RecyclerView.NO_POSITION) null else selectedPosition
}

