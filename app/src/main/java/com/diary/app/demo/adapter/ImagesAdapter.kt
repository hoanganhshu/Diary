package com.diary.app.demo.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.diary.app.demo.R
import com.diary.app.demo.databinding.ItemImageBinding
import com.google.android.material.card.MaterialCardView

class ImagesAdapter(
    private val onImageRemoved: ((removedUri: Uri, position: Int) -> Unit)? = null
) : RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>() {

     val images = mutableListOf<Uri>()
    private val selectedPositions = mutableSetOf<Int>()

    fun submitList(list: List<Uri>) {
        images.clear()
        images.addAll(list)
        selectedPositions.clear()
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        if (position in images.indices) {
            val removed = images.removeAt(position)
            selectedPositions.remove(position)
            // shift lại các vị trí đã chọn > position
            val shifted = selectedPositions.map { if (it > position) it - 1 else it }.toMutableSet()
            selectedPositions.clear()
            selectedPositions.addAll(shifted)

            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount - position)

            onImageRemoved?.invoke(removed, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val binding = ItemImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        holder.bind(images[position], position)
    }

    override fun getItemCount(): Int = images.size

    inner class ImagesViewHolder(private val binding: ItemImageBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(uri: Uri, position: Int) {
            binding.imgSelected.setImageURI(uri)

            val card = binding.root as MaterialCardView
            val isSelected = selectedPositions.contains(position)


            if (isSelected) {
                card.strokeWidth = itemView.resources.getDimensionPixelSize(R.dimen.image_selected_stroke_width)
                card.strokeColor = ContextCompat.getColor(itemView.context, R.color.icon_unselect)
                binding.btnRemove.visibility = View.VISIBLE
            } else {
                card.strokeWidth = 0
                card.strokeColor = ContextCompat.getColor(itemView.context, android.R.color.transparent)
                binding.btnRemove.visibility = View.GONE
            }

            // Click vào ảnh → toggle chọn (viền + nút X)
            itemView.setOnClickListener {
                if (selectedPositions.contains(position)) {
                    selectedPositions.remove(position)
                } else {
                    selectedPositions.add(position)
                }
                notifyItemChanged(position)
            }

            // Bấm X → xóa ảnh khỏi list
            binding.btnRemove.setOnClickListener {
                removeAt(adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return@setOnClickListener)
            }
        }
    }
}
