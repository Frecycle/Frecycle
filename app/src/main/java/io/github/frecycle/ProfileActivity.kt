package io.github.frecycle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.*
import com.mikhaellopez.circularimageview.CircularImageView
import io.github.frecycle.models.Product
import io.github.frecycle.models.User
import io.github.frecycle.util.BottomNavigationViewHelper
import io.github.frecycle.util.FirebaseMethods
import io.github.frecycle.util.SelectionsPagerAdapter
import io.github.frecycle.util.UniversalImageLoader


class ProfileActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var authListener: AuthStateListener
    private lateinit var database : FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var methods: FirebaseMethods

    private val activityNum: Int = 3

    private lateinit var onOfferFragment: OnOfferFragment
    private lateinit var recycledFragment: RecycledFragment

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var selectionsPagerAdapter: SelectionsPagerAdapter
    private lateinit var bottomNavigation : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        selectionsPagerAdapter = SelectionsPagerAdapter(supportFragmentManager)

        viewPager = findViewById(R.id.viewPager)

        onOfferFragment = OnOfferFragment()
        recycledFragment = RecycledFragment()

        selectionsPagerAdapter.addFragment(onOfferFragment, "ON OFFER")
        selectionsPagerAdapter.addFragment(recycledFragment, "RECYCLED")
        //selectionsPagerAdapter.addFragment(CommentsFragment(), "COMMENTS")

        viewPager.adapter = selectionsPagerAdapter

        tabLayout = findViewById(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)

        setupTopToolBar()
        setupFirebaseAuth()
        loadProducts()

        bottomNavigation = findViewById(R.id.bottom_nav)

        // BottomNavigationView activity changer
        BottomNavigationViewHelper.setupBottomNavigationView(applicationContext,this, bottomNavigation)
    }

    private fun loadProducts() {
        val productsOnOffer = ArrayList<String>()
        val productsRecycled = ArrayList<String>()
        val productOnOfferImages : LinkedHashMap<String,String> = LinkedHashMap()
        val productRecycledImages : LinkedHashMap<String,String> = LinkedHashMap()
        reference.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val productsOfUser : ArrayList<String> = ArrayList()
                // get current user's products
                for (ds: DataSnapshot in dataSnapshot.children) {
                    if (ds.key.equals("user_products")) {
                        try {
                            ds.child(auth.currentUser!!.uid).children.forEach { product->
                                productsOfUser.add(product.value.toString())
                            }
                        }catch ( e: NullPointerException){
                            Log.e("loadProducts: ", e.message)
                        }
                    }
                }
                // get products' state feature
                for (ds: DataSnapshot in dataSnapshot.children) {
                    if (ds.key.equals("products")) {
                        for (productId: String in productsOfUser){
                            try {
                                val state = ds.child(productId).getValue(Product::class.java)?.state
                                if(state == 0){
                                    productsOnOffer.add(productId)
                                }else if(state == 100){
                                    productsRecycled.add(productId)
                                }else{
                                    Log.e("loadProducts: ", "There is a product that has an unknown state!!!")
                                }
                            }catch (e: NullPointerException){
                                Log.e("loadProducts: ", e.message)
                            }
                        }
                    }
                }

                // get products' photos
                for (ds: DataSnapshot in dataSnapshot.children) {
                    if (ds.key.equals("products_photos")) {
                        for (productId: String in productsOnOffer){
                            productOnOfferImages[productId] = ds.child(productId).children.first().value.toString()
                        }
                        for (productId: String in productsRecycled){
                            productRecycledImages[productId] = ds.child(productId).children.first().value.toString()
                        }
                    }
                }

                onOfferFragment.initRecyclerView(onOfferFragment.view!!,productOnOfferImages)
                recycledFragment.initRecyclerView(recycledFragment.view!!, productRecycledImages)

            }
            override fun onCancelled(e: DatabaseError) {
                Log.e("loadProducts: ", e.message)
            }
        })
    }

    private fun setupFirebaseAuth(){
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database.reference
        methods = FirebaseMethods(this)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                initializeUserProfile(methods.getUserData(dataSnapshot))
            }

            override fun onCancelled(e: DatabaseError) {
                Log.e("setupFirebaseAuth", e.message)
            }
        })
    }

    private fun setupTopToolBar() {
        val toolbar : Toolbar = findViewById(R.id.profileToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val settingsMenu: ImageButton =  findViewById(R.id.settingsButton)
        settingsMenu.setOnClickListener {
            val intent = Intent(applicationContext,SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    fun profilePhotoClicked(view: View) {
        val intent = Intent(applicationContext,ShowProfilePhotoActivity::class.java)
        startActivity(intent)
    }

    private fun initializeUserProfile(user : User){
        val photoView : CircularImageView = findViewById(R.id.profilePhoto)
        UniversalImageLoader.setImage(user.profile_photo,photoView,null,"")

        val username : TextView = findViewById(R.id.profileNameText)
        username.text = user.name

        val ratingBar : RatingBar = findViewById(R.id.ratingBar)
        ratingBar.rating = user.rank

        val voterNumber : TextView = findViewById(R.id.voterNumber)
        voterNumber.text = "(" + user.voter.toString() + ")"

        val progressBar : ProgressBar = findViewById(R.id.profileProgressBar)
        progressBar.visibility = View.GONE

    }

    override fun onResume() {
        super.onResume()
        BottomNavigationViewHelper.Check.checkMenuItem(bottomNavigation, activityNum)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
    }

    override fun onBackPressed() {
        val startMainActivity = Intent(applicationContext,HomeActivity::class.java)
        startMainActivity.addCategory(Intent.CATEGORY_HOME)
        startMainActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMainActivity)
    }
}
