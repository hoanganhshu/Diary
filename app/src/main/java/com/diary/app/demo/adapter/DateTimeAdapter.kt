import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.diary.app.demo.R
import com.google.android.material.radiobutton.MaterialRadioButton

class DateTimeAdapter(
    private val onPick: (position: Int) -> Unit
) : RecyclerView.Adapter<DateTimeAdapter.VH>() {

    private val data = mutableListOf<String>()
    private var selected = RecyclerView.NO_POSITION

    fun submit(options: List<String>, preselect: Int = RecyclerView.NO_POSITION) {
        data.clear()
        data.addAll(options)
        selected = preselect
        notifyDataSetChanged()
    }

    fun getSelected(): Int = selected

    inner class VH(val rb: MaterialRadioButton) : RecyclerView.ViewHolder(rb) {
        fun bind(text: String, pos: Int) {
            rb.text = text
            rb.isChecked = pos == selected
            rb.setOnClickListener {
                val old = selected
                selected = pos
                if (old != RecyclerView.NO_POSITION) notifyItemChanged(old)
                notifyItemChanged(selected)
                onPick(selected)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bottom_date_time, parent, false) as MaterialRadioButton
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(data[position], position)
    }

    override fun getItemCount() = data.size
}
