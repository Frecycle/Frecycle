package io.github.frecycle

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import io.github.frecycle.models.Product
import io.github.frecycle.util.SliderAdapter
import java.lang.NullPointerException


class ProductActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var profileName : TextView
    private lateinit var productTitle : TextView
    private lateinit var productDescription : TextView
    private lateinit var productDateAndTime : TextView
    private lateinit var ratingBar: RatingBar

    private lateinit var productId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        productId = intent.extras["productId"].toString()

        Toast.makeText(applicationContext, productId, Toast.LENGTH_LONG).show()

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

        setupFirebaseAuth()

    }

    private fun setupFirebaseAuth(){
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database.reference

        val query : Query = reference.child("products")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                initializeProductData(dataSnapshot)
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e("ProductActivity", "Products' loads cancelled!")
            }
        })
    }

    private fun initializeProductData(dataSnapshot: DataSnapshot) {
        val product = Product()
        for (ds : DataSnapshot in dataSnapshot.children){
            if(ds.key.equals(productId)){
                try{
                    product.product_name = ds.getValue(Product::class.java)!!.product_name
                    product.description = ds.getValue(Product::class.java)!!.description
                    product.date = ds.getValue(Product::class.java)!!.date
                    product.time = ds.getValue(Product::class.java)!!.time
                    product.owner = ds.getValue(Product::class.java)!!.owner

                    productTitle = findViewById(R.id.productName)
                    productDescription = findViewById(R.id.productDescription)
                    productDateAndTime = findViewById(R.id.productDateAndTime)
                    profileName = findViewById(R.id.profileNameText)
                    ratingBar = findViewById(R.id.ratingBar)

                    productTitle.text = product.product_name
                    productDescription.text = product.description
                    productDateAndTime.text = product.date + " " + product.time
                    profileName.text = product.owner
                    ratingBar.rating = 4f
                }catch (e: NullPointerException){
                    Log.e("deneme",e.message.toString())
                }

            }

        }

    }

}
