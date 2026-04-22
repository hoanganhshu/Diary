//package com.diary.app.demo.com.diary.app.demo.adapter
//
//import androidx.databinding.ViewDataBinding
//import com.diary.app.demo.R
//
//interface OnClickListener{
//    fun onClick(position: Int, text:String)
//}
//class ViewAdapter : BaseAdapter<Int>() {
//
//    private var onClickListener: OnClickListener? = null
////
////    fun setOnClickListener(listener: OnClickListener) {
////        onClickListener = listener
////    }
//
//    override fun getItemLayout(): Int = R.layout.item_style_font
//     override fun submitData(newData: List<Int>): List<Int> {
//       return newData.filter { it == 2 }
//    }
//
//    override fun setData(
//        binding: ViewDataBinding,
//        item: Int,
//        layoutPosition: Int
//    ) {
////        binding.root.setOnClickListener {
////            onClickListener?.onClick(position = layoutPosition, )
////        }
//    }
//
//    override fun onClickViews(binding: ViewDataBinding, obj: Int, layoutPosition: Int) {
//        super.onClickViews(binding, obj, layoutPosition)
//    }
//}