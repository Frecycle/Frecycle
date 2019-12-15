package io.github.frecycle.util

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import io.github.frecycle.R
import io.github.frecycle.models.Product

class RecyclerViewAdapter() : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {
    private lateinit var context: Context
    private lateinit var products : ArrayList<Product>

    constructor(context: Context?, products: ArrayList<Product>?) : this() {
        this.context = context!!
        this.products = products!!
    }


    class MyViewHolder : RecyclerView.ViewHolder {
        var productPhoto : ImageView

        constructor(itemView: View) : super(itemView){
            productPhoto = itemView.findViewById(R.id.imageViewProduct)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.snippet_cardview_item_home,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //holder.productPhoto.setImageResource(products[position].coverPhoto)

        //val uri: Uri = Uri.parse(products[position].photos[0])
        //holder.productPhoto.setImageURI(uri)
    }

}