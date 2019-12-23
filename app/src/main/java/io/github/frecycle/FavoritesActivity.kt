package io.github.frecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.github.frecycle.models.Product
import io.github.frecycle.util.BottomNavigationViewHelper
import io.github.frecycle.util.RecyclerViewAdapter

class FavoritesActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var productName : TextView
    private lateinit var productCity : TextView
    private lateinit var productDate : TextView

    private val productList = ArrayList<Product>()
    private val productPhotoList = ArrayList<String>()
    private lateinit var bottomNavigation : BottomNavigationView
    private val activityNum : Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        setupFirebaseAuth()

        bottomNavigation = findViewById(R.id.bottom_nav)

        // BottomNavigationView activity changer
        BottomNavigationViewHelper.setupBottomNavigationView(applicationContext,this, bottomNavigation)
    }

    private fun setupFirebaseAuth() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database.reference

        val favList = ArrayList<String>()

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds: DataSnapshot in dataSnapshot.children) {
                    if (ds.key.equals("user_favorites")) {
                        try {
                            ds.child(auth.currentUser!!.uid).children.forEach { x->
                                favList.add(x.key.toString())
                            }

                        }catch ( e: NullPointerException){
                            Log.d("FavoritesActivity: ", e.message)
                        }
                    }
                }
                for (ds: DataSnapshot in dataSnapshot.children) {
                    if (ds.key.equals("products_photos")) {
                        try {
                            ds.children.forEach { x->

                                for((index) in favList.withIndex()){
                                    if(x.key.equals(favList[index])){
                                        x.children.forEach { y-> productPhotoList.add(y.value.toString()) }
                                    }
                                }
                            }
                            Log.e("deneme", productPhotoList.toString())

                        }catch ( e: NullPointerException){
                            Log.d("FavoritesActivity: ", e.message)
                        }
                    }
                }
                for (ds: DataSnapshot in dataSnapshot.children) {
                    if (ds.key.equals("products")) {
                        if(favList.size>0){
                            for((index) in favList.withIndex()){
                                val product = Product()

                                product.product_name = ds.child(favList[index]).getValue(Product::class.java)!!.product_name
                                product.city = ds.child(favList[index]).getValue(Product::class.java)!!.city
                                product.date = ds.child(favList[index]).getValue(Product::class.java)!!.date

                                productList.add(product)

                            }
                        }
                    }
                }

                initializeRecyclerView(productList, productPhotoList)

            }
            override fun onCancelled(p0: DatabaseError) {
                Log.e("FavoritesActivity", "UserFavorites' loads cancelled!")
            }

        })
    }

    private fun initializeRecyclerView(products: ArrayList<Product>, productPhotos : ArrayList<String>){
        val recyclerView : RecyclerView = findViewById(R.id.recycleViewFavorites)
        val recyclerViewAdapter = RecyclerViewAdapter(this,products,productPhotos)
        recyclerView.layoutManager = GridLayoutManager(this,1)
        recyclerView.adapter = recyclerViewAdapter
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
    }

    override fun onResume() {
        super.onResume()
        BottomNavigationViewHelper.Check.checkMenuItem(bottomNavigation,activityNum)
    }

    override fun onBackPressed() {
        val startMainAcivity = Intent(applicationContext,HomeActivity::class.java)
        startMainAcivity.addCategory(Intent.CATEGORY_HOME)
        startMainAcivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMainAcivity)

    }

}
