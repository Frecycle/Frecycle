package io.github.frecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import io.github.frecycle.util.SelectionsPagerAdapter


class ProfileActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var selectionsPagerAdapter: SelectionsPagerAdapter

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
}
