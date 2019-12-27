package io.github.frecycle

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mikhaellopez.circularimageview.CircularImageView
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import io.github.frecycle.models.Product
import io.github.frecycle.models.User
import io.github.frecycle.util.FavoritesHelper
import io.github.frecycle.util.FirebaseMethods
import io.github.frecycle.util.SliderAdapter
import io.github.frecycle.util.UniversalImageLoader
import java.lang.NullPointerException
import kotlin.collections.ArrayList


class ProductActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var reference : DatabaseReference
    private lateinit var methods : FirebaseMethods

    private lateinit var profileName : TextView
    private lateinit var productTitle : TextView
    private lateinit var productDescription : TextView
    private lateinit var productDateAndTime : TextView
    private lateinit var profilePhoto : CircularImageView
    private lateinit var ratingBar: RatingBar

    private lateinit var favButton : ImageButton
    private var favState : Boolean = false

    private lateinit var productId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        productId = intent.extras["productId"].toString()

        setupFirebaseAuth()

        favButton = findViewById(R.id.productFavButton)

        // key is product id, value is date
        favButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if(auth.currentUser != null){
                    if(!favState){
                        favState = true
                        FavoritesHelper.instance.addToFavorites(applicationContext,favButton,productId)
                    }else{
                        favState = false
                        FavoritesHelper.instance.removeFromFavorites(applicationContext,favButton,productId)
                    }
                }else{
                    Toast.makeText(applicationContext, R.string.should_sign_in, Toast.LENGTH_SHORT).show()
                }
            }

        })

    }

    private fun setupFirebaseAuth(){
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database.reference
        methods = FirebaseMethods(this)

        //val query : Query = reference.child("products")

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
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
            if (ds.key.equals("products")){
                    try{
                        product.product_name = ds.child(productId).getValue(Product::class.java)!!.product_name
                        product.description = ds.child(productId).getValue(Product::class.java)!!.description
                        product.date = ds.child(productId).getValue(Product::class.java)!!.date
                        product.time = ds.child(productId).getValue(Product::class.java)!!.time
                        product.owner = ds.child(productId).getValue(Product::class.java)!!.owner

                        productTitle = findViewById(R.id.productName)
                        productDescription = findViewById(R.id.productDescription)
                        productDateAndTime = findViewById(R.id.productDateAndTime)


                        productTitle.text = product.product_name
                        productDescription.text = product.description
                        productDateAndTime.text = product.date + " " + product.time
                        //profileName.text = product.owner
                        //ratingBar.rating = 4f
                    }catch (e: NullPointerException){
                        Log.e("deneme",e.message.toString())
                    }
                }
        }
        for (ds : DataSnapshot in dataSnapshot.children){
            if (ds.key.equals("users")){
                profilePhoto = findViewById(R.id.profilePhoto)
                profileName = findViewById(R.id.profileNameText)
                ratingBar = findViewById(R.id.ratingBar)

                UniversalImageLoader.setImage(ds.child(product.owner).getValue(User::class.java)!!.profile_photo,profilePhoto,null,"")
                profileName.text = ds.child(product.owner).getValue(User::class.java)!!.name
                ratingBar.rating = ds.child(product.owner).getValue(User::class.java)!!.rank
            }
        }
        for(ds : DataSnapshot in dataSnapshot.children){
            if(ds.key.equals("products_photos")){
                val productImages = ArrayList<String>()

                ds.child(productId).children.forEach { x-> productImages.add(x.value.toString()) }

                val sliderView: SliderView = findViewById(R.id.imageSlider)
                val sliderAdapter = SliderAdapter(this, productImages)
                sliderView.sliderAdapter = sliderAdapter

                sliderView.startAutoCycle()
                sliderView.indicatorSelectedColor = Color.BLACK
                sliderView.indicatorUnselectedColor = Color.GRAY

                sliderView.setIndicatorAnimation(IndicatorAnimations.WORM)
                sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            }
        }
        for(ds : DataSnapshot in dataSnapshot.children){
            if(auth.currentUser == null) break
            if(ds.key.equals("user_favorites")){
                ds.child(auth.currentUser!!.uid).children.forEach {x->
                    if(x.key.equals(productId)){
                        favButton.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.favorite))
                        favState = true
                    }
                }
            }
        }

    }

}
