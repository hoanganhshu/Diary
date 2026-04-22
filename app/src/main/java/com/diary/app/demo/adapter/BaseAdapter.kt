//package com.diary.app.demo.com.diary.app.demo.adapter
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.databinding.DataBindingUtil
//import androidx.databinding.ViewDataBinding
//import androidx.recyclerview.widget.RecyclerView
//
//interface OnItemSelect<T> {
//    fun onItemSelected(item: T, positon: Int)
//
//}
//
//abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseAdapter<T>.ViewHolder>() {
//
//    var inet4Address :OnItemSelect<T> ?= null
//    abstract fun getItemLayout(): Int
//   open fun submitData(newData: List<T>) : List<T>{
//       newData.clear()
//       newData.addAll(submitData(newData))
//       return newData
//   }
//    abstract fun setData(binding: ViewDataBinding, item: T, layoutPosition: Int)
//    open fun onResizeViews(binding: ViewDataBinding) {}
//    open fun onClickViews(binding: ViewDataBinding, obj: T, layoutPosition: Int) {}
//
//    val list: MutableList<T> = mutableListOf()
//    var context: Context? = null
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        context = parent.context
//        return ViewHolder(
//            DataBindingUtil.inflate(
//                LayoutInflater.from(parent.context),
//                getItemLayout(), parent, false
//            )
//        )
//    }
//    protected fun setUpData(newData: List<T>){
//        list.clear()
//        list.addAll(submitData(newData))
//        notifyDataSetChanged()
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bindData(list[position])
//        inet4Address.onItemSelected()
//    }
//
//    override fun getItemCount(): Int {
//        if (list.isNotEmpty()) {
//            return list.size
//        }
//        return 0
//    }
//
//
//    inner class ViewHolder(var binding: ViewDataBinding) : BaseViewHolder<T>(binding) {
//        override fun bindData(obj: T) {
//            setData(binding, obj, layoutPosition)
//        }
//    }
//
//}
//
//abstract class BaseViewHolder<T>(mBinding: ViewDataBinding) :
//    RecyclerView.ViewHolder(mBinding.root) {
//    abstract fun bindData(obj: T)
//}
//
///*  protected var selectPosition = RecyclerView.NO_POSITION
//fun submit(list: List<T>) {
//    items.clear()
//    items.addAll(list)
//    notifyDataSetChanged()
//}
//
//override fun getItemCount(): Int {
//    return items.size
//}
//
//protected fun getItem(positon: Int): T {
//    return items[positon]
//}
//
//protected fun selectItem(positon: Int) {
//    val old = selectPosition
//    selectPosition = positon
//    onItemSelect.onItemSelected(items[positon], positon)
//    if (old != RecyclerView.NO_POSITION) notifyItemChanged(old)
//    notifyItemChanged(selectPosition)
//
//
//}
//*/