package io.github.frecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.nostra13.universalimageloader.core.ImageLoader
import io.github.frecycle.util.*
import kotlin.system.exitProcess

class HomeActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var methods: FirebaseMethods

    private lateinit var bottomNavigation : BottomNavigationView
    private lateinit var backToast: Toast
    private lateinit var addProductButton : Button

    private var backPressedTime : Long = 0
    private val activityNum : Int = 0
    private val productsImages : LinkedHashMap<String,String> = LinkedHashMap()
    private val productsFavs : LinkedHashMap<String,String> = LinkedHashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupFirebaseAuth()

        bottomNavigation = findViewById(R.id.bottom_nav)

        // BottomNavigationView activity changer
        BottomNavigationViewHelper.setupBottomNavigationView(applicationContext,this, bottomNavigation)

        addProductButton = findViewById(R.id.addProductButton)

        addProductButton.setOnClickListener{
            if(auth.currentUser != null){
                val intent = Intent(applicationContext, AddProductActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(applicationContext, getString(R.string.should_sign_in), Toast.LENGTH_LONG).show()
                val intent = Intent(applicationContext, SignInUpActivity::class.java)
                startActivity(intent)
            }

        }

        initImageLoader()
        //tryCardView()
    }

    private fun setupFirebaseAuth(){
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database.reference
        methods = FirebaseMethods(this)

        val query : Query = reference.child("products_photos")

        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(singleSnapshot : DataSnapshot in dataSnapshot.children){
                    for(ss : DataSnapshot in singleSnapshot.children){
                        productsImages[singleSnapshot.key.toString()] = ss.value.toString()
                        break
                    }
                }
                initializeRecyclerViewHome(ArrayList(productsImages.values))
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e("HomeActivity", "Products' loads cancelled!")
            }
        })

        /*       val queryFavorites : Query = reference.child("user_favorites")

               queryFavorites.addListenerForSingleValueEvent(object : ValueEventListener{
                   override fun onDataChange(dataSnapshot: DataSnapshot) {
                       for (ds: DataSnapshot in dataSnapshot.children) {
                           if (ds.key.equals(auth.currentUser!!.uid)) {
                               //TODO FAVORI BUTONLARI GUNCELLENECEK
                           }
                       }
                   }
                   override fun onCancelled(p0: DatabaseError) {
                       Log.e("HomeActivity", "UserFavorites' loads cancelled!")
                   }

               })*/

    }

    private fun initializeRecyclerViewHome(list: ArrayList<String>) {
        val recyclerView : RecyclerView = findViewById(R.id.recycleViewHome)
        val recyclerViewImagesAdapter = RecyclerViewImagesAdapter(this, list)
        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.adapter = recyclerViewImagesAdapter

        recyclerViewImagesAdapter.setOnItemClickListener(object: RecyclerViewImagesAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {

                //Toast.makeText(applicationContext, "Clicked Item Id :" + productsImages.keys.toTypedArray()[position], Toast.LENGTH_LONG).show()
                val intent = Intent(applicationContext, ProductActivity::class.java)
                intent.putExtra("productId", productsImages.keys.toTypedArray()[position])
                startActivity(intent)
            }
        })

        val progressBar : ProgressBar = findViewById(R.id.homeProgressBar)
        progressBar.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
    }

    override fun onResume() {
        super.onResume()
        BottomNavigationViewHelper.Check.checkMenuItem(bottomNavigation,activityNum)
    }

    private fun initImageLoader(){
        val universalImageLoader = UniversalImageLoader(applicationContext)
        ImageLoader.getInstance().init(universalImageLoader.getConfig())
    }

    override fun onBackPressed() {

        if(backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel()
            super.finishAffinity()
            exitProcess(0)
        }else{
            backToast = Toast.makeText(applicationContext,"Press back again to exit",Toast.LENGTH_SHORT)
            backToast.show()
        }

        backPressedTime = System.currentTimeMillis()
    }
}
