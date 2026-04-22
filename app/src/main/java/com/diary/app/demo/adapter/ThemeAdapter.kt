
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.diary.app.demo.R


class BackgroundAdapter(
    private var items: List<Int>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<BackgroundAdapter.BgViewHolder>() {

    inner class BgViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgBg: ImageView = itemView.findViewById(R.id.imgBackground)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BgViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_background, parent, false)
        return BgViewHolder(view)
    }

    override fun onBindViewHolder(holder: BgViewHolder, position: Int) {
        val item = items[position]
        holder.imgBg.setImageResource(item)



        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount() = items.size
    fun submitList(newItems: List<Int>) {
        items = newItems
        notifyDataSetChanged()
    }
}
