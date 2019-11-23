package io.github.frecycle.util

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import io.github.frecycle.R

class UniversalImageLoader {
    private val defaultImage = R.drawable.ic_person
    private var mContext : Context

    constructor(context: Context) {
        this.mContext = context
    }

    public fun getConfig() : ImageLoaderConfiguration{
        val defaultOptions: DisplayImageOptions = DisplayImageOptions.Builder()
            .showImageOnLoading(defaultImage)
            .showImageForEmptyUri(defaultImage)
            .showImageOnFail(defaultImage)
            .cacheOnDisk(true).cacheInMemory(true)
            .cacheOnDisk(true).resetViewBeforeLoading(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .displayer(FadeInBitmapDisplayer(300)).build()

        return ImageLoaderConfiguration.Builder(mContext)
            .defaultDisplayImageOptions(defaultOptions)
            .memoryCache(WeakMemoryCache())
            .diskCacheSize(100 * 1024 * 1024).build()
    }
    companion object {
        fun setImage(imgURL: String, image: ImageView, progressBar: ProgressBar?, append: String) {
            val imageLoader: ImageLoader = ImageLoader.getInstance()
            imageLoader.displayImage(append + imgURL, image, object : SimpleImageLoadingListener() {
                override fun onLoadingStarted(imageUri: String, view: View) {
                    if (progressBar != null) progressBar.visibility = View.VISIBLE
                }

                override fun onLoadingFailed(imageUri: String, view: View, failReason: FailReason) {
                    if (progressBar != null) progressBar.visibility = View.GONE
                }

                override fun onLoadingComplete(imageUri: String, view: View, loadedImage: Bitmap) {
                    if (progressBar != null) progressBar.visibility = View.GONE
                }

                override fun onLoadingCancelled(imageUri: String, view: View) {
                    if (progressBar != null) progressBar.visibility = View.GONE
                }
            })
        }
    }
}