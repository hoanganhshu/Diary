package com.diary.app.demo.adapter

import com.diary.app.demo.util.DashedDividerDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diary.app.demo.R
import com.diary.app.demo.data.local.DiaryEntity


data class DiaryCard(
    val diaries: List<DiaryEntity>
)

class AdapterDiaries(
    private val callback: (DiaryEntity) -> Unit
) : RecyclerView.Adapter<AdapterDiaries.ParentVH>() {

    private val items = mutableListOf<DiaryCard>()

    fun submitList(list: List<DiaryCard>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_diaries, parent, false)
        return ParentVH(view)
    }

    override fun onBindViewHolder(holder: ParentVH, position: Int) {
        holder.bind(items[position])
    }

    inner class ParentVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val rvChild: RecyclerView =
            itemView.findViewById(R.id.recyclerviewdiaries)



        fun bind(card: DiaryCard) {
            val adapter = DiariesAdapter(callback)

            rvChild.layoutManager = LinearLayoutManager(itemView.context)
            rvChild.adapter = adapter
            if (rvChild.itemDecorationCount == 0) {
                rvChild.addItemDecoration(
                    DashedDividerDecoration(
                        context = rvChild.context,
                        marginHorizontalDp = 12f
                    )
                )

            }
            rvChild.setHasFixedSize(false)

            adapter.submitList(card.diaries)
        }
    }
}
