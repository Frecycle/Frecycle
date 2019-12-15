package io.github.frecycle.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import io.github.frecycle.R


class SliderAdapter() : SliderViewAdapter<SliderAdapter.SliderAdapterVH>() {
    private lateinit var context: Context
    private lateinit var images : ArrayList<String>

    constructor(context: Context?, images: ArrayList<String>?) : this() {
        this.context = context!!
        this.images = images!!
    }

    class SliderAdapterVH : SliderViewAdapter.ViewHolder {
        var imageViewBackground : ImageView
        var itemView : View

        constructor(itemView: View) : super(itemView) {
            imageViewBackground = itemView.findViewById(R.id.imageSliderBackground)
            this.itemView = itemView
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterVH {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.snippet_image_slider_item, null)
        return SliderAdapterVH(view)
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH?, position: Int) {
        when(position){
            0 -> Glide.with(viewHolder?.itemView!!).load(images[0]).into(viewHolder.imageViewBackground)

            1-> Glide.with(viewHolder?.itemView!!).load(images[1]).into(viewHolder.imageViewBackground)
        }
    }
}