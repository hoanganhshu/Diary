package com.diary.app.demo.adapter



import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class EmojiAdapter(
    private val items: List<Int>,
    private val onClick: ( Int) -> Unit
) : RecyclerView.Adapter<EmojiAdapter.EmojiVH>() {

    inner class EmojiVH(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiVH {
        val iv = ImageView(parent.context).apply {
            val size = (56 * resources.displayMetrics.density).toInt()
            layoutParams = ViewGroup.LayoutParams(size, size)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        return EmojiVH(iv)
    }

    override fun onBindViewHolder(holder: EmojiVH, position: Int) {
        val resId = items[position]
        holder.imageView.setImageResource(resId)
        holder.imageView.setOnClickListener {
            onClick(resId)

        }
    }

    override fun getItemCount(): Int = items.size
}
