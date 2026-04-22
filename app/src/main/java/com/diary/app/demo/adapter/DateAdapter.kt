//package com.diary.app.demo.com.diary.app.demo.adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.diary.app.demo.R
//import com.google.android.material.radiobutton.MaterialRadioButton
//
//class DateAdapter(listener: OnItemSelect<String>) : BaseAdapter<String, DateAdapter.DateVH>(listener) {
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): DateAdapter.DateVH {
//        val v = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_bottom_date_time, parent, false) as MaterialRadioButton
//        return DateVH(v)
//    }
//
//    override fun onBindViewHolder(holder: DateAdapter.DateVH, position: Int) {
//        TODO("Not yet implemented")
//    }
//    inner class DateVH( rb : MaterialRadioButton): RecyclerView.ViewHolder(rb){
//        fun bind(text: String, pos: Int) {
//
//
//        }
//
//
//
//
//
//    }
//}