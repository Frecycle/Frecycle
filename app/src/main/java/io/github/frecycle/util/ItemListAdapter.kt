package io.github.frecycle.util
/*



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import androidx.palette



class ItemListAdapter(private var context: Context) : RecyclerView.Adapter<ItemListAdapter.ViewHolder>() {

    // 1
    override fun getItemCount() = ItemData.ItemList().size

    // 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_items, parent, false)
        return ViewHolder(itemView)
    }

    //3
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = ItemData.ItemList()[position]
        holder.itemView.ItemName.text = place.name
        Picasso.with(context).load(place.getImageResourceId(context)).into(holder.itemView.itemImage)
    }

    // 2
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    }
}

 */