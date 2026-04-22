//package com.diary.app.demo.com.diary.app.demo.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.BaseAdapter
//import android.widget.ImageView
//import androidx.core.graphics.toColorInt
//import androidx.recyclerview.widget.RecyclerView
//import com.diary.app.demo.R
//import com.google.android.material.card.MaterialCardView
//
//class ColorAdapter(listen : OnItemSelect<String>) : BaseAdapter<String, ColorAdapter.ColorVH>(listen){
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): ColorVH {
//       val v= LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false)
//        return ColorVH(v)
//    }
//
//
//    override fun onBindViewHolder(
//        holder: ColorVH,
//        position: Int
//    ) {
//
//        val colorInt = getItem(position).toColorInt()
//        holder.img.background.setTint(colorInt)
//        holder.itemView.setOnClickListener {
//            selectItem(position)
//        }
//        holder.card.strokeWidth = if (selectPosition == position) 4 else 0
//
//
//
//    }
//
//    inner class ColorVH(itemView : View) : RecyclerView.ViewHolder(itemView){
//
//            val card: MaterialCardView = itemView.findViewById(R.id.cardColor)
//            val img: ImageView = itemView.findViewById(R.id.circleImg)
//
//    }
//}