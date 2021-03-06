package io.github.frecycle.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.frecycle.R
import io.github.frecycle.models.Product

class RecyclerViewFavoritesAdapter() : RecyclerView.Adapter<RecyclerViewFavoritesAdapter.MyViewHolder>() {
    private lateinit var context: Context
    private lateinit var products : ArrayList<Product>
    private lateinit var productImages : ArrayList<String>
    private lateinit var mListener: OnItemClickListener


    constructor(context: Context?, products: ArrayList<Product>?, productsImages : ArrayList<String>?) : this() {
        this.context = context!!
        this.products = products!!
        this.productImages = productsImages!!
    }


    class MyViewHolder : RecyclerView.ViewHolder {
        var productPhoto : ImageView
        var productName : TextView
        var productCity : TextView
        var productDate : TextView
        var progressBar: ProgressBar

        constructor(itemView: View, context: Context?, listener: OnItemClickListener) : super(itemView){
            productPhoto = itemView.findViewById(R.id.productPhotoFav)
            productName = itemView.findViewById(R.id.productNameFav)
            productCity = itemView.findViewById(R.id.productCityFav)
            productDate = itemView.findViewById(R.id.productDateFav)
            this.progressBar = itemView.findViewById(R.id.progressBar4ProductFav)

            itemView.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v: View?) {
                    if(v != null){
                        val position = adapterPosition
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position)
                        }
                    }
                }
            })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.snippet_cardview_item_favorites,parent,false)
        return MyViewHolder(view, context, mListener)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(holder.itemView).load(productImages[position]).into(holder.productPhoto)
        holder.productName.text = products[position].product_name
        holder.productCity.text = products[position].city
        holder.productDate.text = products[position].date

        holder.progressBar.visibility = View.GONE
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.mListener = listener
    }

}