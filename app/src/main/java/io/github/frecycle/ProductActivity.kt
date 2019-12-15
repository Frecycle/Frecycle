package io.github.frecycle

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import io.github.frecycle.util.SliderAdapter


class ProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val productImages = ArrayList<String>()

        productImages.add("https://images.unsplash.com/photo-1562887250-1ccd2e28a02c?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1489&q=80")
        productImages.add("https://images.unsplash.com/photo-1562887194-a50958050e39?ixlib=rb-1.2.1&auto=format&fit=crop&w=1489&q=80")


        val sliderView: SliderView = findViewById(R.id.imageSlider)
        val sliderAdapter = SliderAdapter(this, productImages)

        sliderView.sliderAdapter = sliderAdapter

        sliderView.startAutoCycle()
        sliderView.indicatorSelectedColor = Color.BLACK
        sliderView.indicatorUnselectedColor = Color.GRAY

        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM)
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView.scrollTimeInSec = 3

    }
}
