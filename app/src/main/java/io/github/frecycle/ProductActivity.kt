package io.github.frecycle

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
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
import java.lang.Exception
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
    private lateinit var sendRequestButton: Button
    private lateinit var favButton : ImageButton
    private lateinit var productDeleteButton : ImageButton

    private var favState : Boolean = false
    private var REQUEST_STATUS: Int  = -1
    private lateinit var productId: String
    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        initWidgets()

        productId = intent.getStringExtra("productId")
        setupFirebaseAuth()
        setupButtons()

    }

    private fun setupButtons() {
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

        sendRequestButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                if (auth.currentUser != null && auth.currentUser!!.uid == product.owner){
                    Toast.makeText(this@ProductActivity,getString(R.string.owner_cannot_send_request),Toast.LENGTH_SHORT).show()
                }else if(auth.currentUser == null){
                    Toast.makeText(this@ProductActivity,getString(R.string.should_login_to_continue),Toast.LENGTH_SHORT).show()
                    val intent =  Intent(this@ProductActivity, SignInUpActivity::class.java)
                    startActivity(intent)
                }else{
                    // TODO redirect to message page
                    if(REQUEST_STATUS == -1 && product.state == 0){
                        sendRequest()
                        sendRequestButton.setBackgroundResource(R.drawable.button_background_color_disabled)
                        sendRequestButton.text = "Waiting Response"
                        Toast.makeText(this@ProductActivity,"Request sent!",Toast.LENGTH_SHORT).show()
                    }else if (REQUEST_STATUS == 0 && product.state == 0){
                        Toast.makeText(this@ProductActivity,getString(R.string.waiting_response),Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this@ProductActivity,getString(R.string.product_not_reachable),Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        productDeleteButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this@ProductActivity)
                alertDialogBuilder.setMessage(getString(R.string.sure_delete_product))
                alertDialogBuilder.setPositiveButton("Yes", object: DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val progressDialog: ProgressDialog = ProgressDialog(this@ProductActivity)
                        progressDialog.setTitle("Be patient!")
                        progressDialog.setMessage("We are deleting your product :)")
                        progressDialog.setCanceledOnTouchOutside(false)
                        progressDialog.show()

                        reference.addValueEventListener(object: ValueEventListener{
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (ds : DataSnapshot in dataSnapshot.children){
                                    try {
                                        if (ds.key.equals("products")){
                                            Log.d("silme işlemi1-> ", ds.child(productId).toString())
                                            ds.child(productId).ref.removeValue()
                                        }else if(ds.key.equals("products_photos")){
                                            Log.d("silme işlemi2-> ", ds.child(productId).toString())
                                            ds.child(productId).ref.removeValue()
                                        }else if(ds.key.equals("user_products")){
                                            ds.child(auth.currentUser!!.uid).children.forEach userProduct@{ keyNProductId->
                                                if (keyNProductId.value!!.equals(productId)){
                                                    Log.d("silme işlemi3-> ", keyNProductId.toString())
                                                    keyNProductId.ref.removeValue()
                                                    return@userProduct
                                                }
                                            }
                                        }else if(ds.key.equals("user_favorites")){
                                            for (snapShot : DataSnapshot in ds.children){
                                                snapShot.children.forEach { productNDate ->
                                                    if(productNDate.key == productId){
                                                        Log.d("silme işlemi4-> ", productNDate.toString())
                                                        productNDate.ref.removeValue()
                                                    }
                                                }
                                            }
                                        }
                                    }catch (e: Exception){
                                        Log.e("ItemDeletion@ProductAct", e.message)
                                        progressDialog.dismiss()
                                        Toast.makeText(this@ProductActivity, e.message, Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                            override fun onCancelled(e: DatabaseError) {
                                Log.e("ItemDeletion@ProductAct", e.message)
                                progressDialog.dismiss()
                            }
                        })
                        //firestore'den silme işlemi yapmadık
                        progressDialog.dismiss()
                        finish()
                    }
                })
                alertDialogBuilder.setNegativeButton("No", object: DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        dialog!!.cancel()
                    }
                })

                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
        })

    }

    private fun sendRequest(){
        val query: Query = reference.child("products_requests").child(product.owner).child(productId).child(auth.currentUser!!.uid)
        query.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val snapshot = dataSnapshot.ref
                snapshot.setValue(0)
                REQUEST_STATUS = 0
                // 0: PENDING, 100: YES, 200: NO


            }
            override fun onCancelled(e: DatabaseError) {
                Log.e("SendRequest:Product", e.message)
            }
        })
    }

    private fun initWidgets() {
        profilePhoto = findViewById(R.id.profilePhotoEdit)
        profileName = findViewById(R.id.profileNameText)
        ratingBar = findViewById(R.id.ratingBar)
        productTitle = findViewById(R.id.productName)
        productDescription = findViewById(R.id.productDescription)
        productDateAndTime = findViewById(R.id.productDateAndTime)
        favButton = findViewById(R.id.productFavButton)
        sendRequestButton = findViewById(R.id.productSendRequestButton)
        productDeleteButton = findViewById(R.id.productDeleteButton)
    }

    private fun setupFirebaseAuth(){
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database.reference
        methods = FirebaseMethods(this)

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                initProductData(dataSnapshot)
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e("ProductActivity", "Product's loads cancelled!")
            }
        })
    }

    private fun initProductData(dataSnapshot: DataSnapshot){
        product = Product()
        for (ds : DataSnapshot in dataSnapshot.children) {
            if (ds.key.equals("products")){
                try{
                    product.product_name = ds.child(productId).getValue(Product::class.java)!!.product_name
                    product.description = ds.child(productId).getValue(Product::class.java)!!.description
                    product.date = ds.child(productId).getValue(Product::class.java)!!.date
                    product.time = ds.child(productId).getValue(Product::class.java)!!.time
                    product.owner = ds.child(productId).getValue(Product::class.java)!!.owner
                    product.state = ds.child(productId).getValue(Product::class.java)!!.state

                    productTitle.text = product.product_name
                    productDescription.text = product.description
                    productDateAndTime.text = product.date + " " + product.time



                }catch (e: Exception){
                    Log.e("initProductData", e.message.toString())
                    Toast.makeText(applicationContext, getString(R.string.product_not_reachable), Toast.LENGTH_LONG).show()
                    this@ProductActivity.finish()
                    return
                }
            }
        }
        for (ds : DataSnapshot in dataSnapshot.children){
            if (ds.key.equals("users")){
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
        if(auth.currentUser != null){
            for(ds : DataSnapshot in dataSnapshot.children) {
                if (ds.key.equals("products_requests")) {
                    ds.child(product.owner).child(productId).children.forEach { userNAnswers ->
                        if(userNAnswers.key.equals(auth.currentUser!!.uid)){
                            REQUEST_STATUS = userNAnswers.value.toString().toInt()
                        }
                    }
                }
            }
        }
        initAppearanceRequestButton()
    }

    private fun initAppearanceRequestButton(){
        Log.e("reqdeneme", REQUEST_STATUS.toString())
        if (auth.currentUser != null && auth.currentUser!!.uid == product.owner){
            sendRequestButton.setBackgroundResource(R.drawable.button_background_color_disabled)
            productDeleteButton.visibility = View.VISIBLE
        }else if(auth.currentUser != null && product.state == 0 && REQUEST_STATUS == 0){
            sendRequestButton.setBackgroundResource(R.drawable.button_background_color_disabled)
            sendRequestButton.text = "Waiting Response"
        }else if(auth.currentUser != null && product.state == 100){
            sendRequestButton.setBackgroundResource(R.drawable.button_background_color_disabled)
            sendRequestButton.text = "RECYCLED"
        }
    }

}
