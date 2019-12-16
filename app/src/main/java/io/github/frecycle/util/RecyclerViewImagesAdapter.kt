package io.github.frecycle.util

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import io.github.frecycle.AddProductActivity
import io.github.frecycle.R
import io.github.frecycle.models.Product

class RecyclerViewImagesAdapter() : RecyclerView.Adapter<RecyclerViewImagesAdapter.MyViewHolder>() {
    private lateinit var context: Context
    private lateinit var productsImages : ArrayList<String>
    private lateinit var mListener: OnItemClickListener

    constructor(context: Context?, productsImages: ArrayList<String>?) : this() {
        this.context = context!!
        this.productsImages = productsImages!!
    }

    class MyViewHolder : RecyclerView.ViewHolder {
        var productCoverImage : ImageView
        var progressBar : ProgressBar


        constructor(itemView: View, listener: OnItemClickListener) : super(itemView){
            productCoverImage = itemView.findViewById(R.id.imageViewProduct)
            this.progressBar = itemView.findViewById(R.id.progressBar4Item)


            itemView.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v: View?) {
                    if (v != null){
                        val position = adapterPosition
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position)
                        }
                    }
                }
            } )
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.snippet_cardview_item_home,parent,false)
        return MyViewHolder(view, mListener)
    }

    override fun getItemCount(): Int {
        return productsImages.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //holder.productPhoto.setImageResource(products[position].coverPhoto)

        Glide.with(holder.itemView).load(productsImages[position]).listener(object : RequestListener<Drawable>{
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                Log.e("Load Product Item Glide", e.toString())
                loadingDone()
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                loadingDone()
                return false
            }

            private fun loadingDone(){
                holder.progressBar.visibility = View.GONE
            }

        }).into(holder.productCoverImage)

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.mListener = listener
    }
}