package io.github.frecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import io.github.frecycle.util.BottomNavigationViewHelper
import io.github.frecycle.util.SelectionsPagerAdapter


class ProfileActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
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


        val ratingBar : RatingBar = findViewById(R.id.ratingBar)
        ratingBar.rating = 4.5F
        setupTopToolBar()

        bottomNavigation = findViewById(R.id.bottom_nav)

        // BottomNavigationView activity changer
        BottomNavigationViewHelper.setupBottomNavigationView(applicationContext,this, bottomNavigation)

        auth = FirebaseAuth.getInstance()
        initializeUserProfile()
    }

    private fun setupTopToolBar() {
        val toolbar : Toolbar = findViewById(R.id.profileToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    fun profilePhotoClicked(view: View) {
        val intent = Intent(applicationContext,ShowProfilePhotoActivity::class.java)
        startActivity(intent)
    }

    fun profileMenuClicked(view: View) {
        val intent = Intent(applicationContext,SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun initializeUserProfile(){
        val username : TextView = findViewById(R.id.profileNameText)
        username.text = auth.currentUser?.displayName
    }
}
