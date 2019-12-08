package io.github.frecycle

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.*
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.mikhaellopez.circularimageview.CircularImageView
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

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var selectionsPagerAdapter: SelectionsPagerAdapter
    private lateinit var bottomNavigation : BottomNavigationViewEx

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        selectionsPagerAdapter =
            SelectionsPagerAdapter(supportFragmentManager)

        viewPager = findViewById(R.id.viewPager)

        selectionsPagerAdapter.addFragment(OnOfferFragment(), "ON OFFER")
        selectionsPagerAdapter.addFragment(RecycledFragment(), "RECYCLED")
        selectionsPagerAdapter.addFragment(CommentsFragment(), "COMMENTS")

        viewPager.adapter = selectionsPagerAdapter


        tabLayout = findViewById(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)


        setupTopToolBar()
        setupFirebaseAuth()

        bottomNavigation = findViewById(R.id.bottom_nav)

        // BottomNavigationView activity changer
        BottomNavigationViewHelper.setupBottomNavigationView(applicationContext,this, bottomNavigation)
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

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
}
