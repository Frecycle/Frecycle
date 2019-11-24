package io.github.frecycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import io.github.frecycle.util.BottomNavigationViewHelper

class SearchActivity : AppCompatActivity() {
    private lateinit var bottomNavigation : BottomNavigationViewEx
    private val activityNum : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        bottomNavigation = findViewById(R.id.bottom_nav)

        // BottomNavigationView activity changer
        BottomNavigationViewHelper.setupBottomNavigationView(applicationContext,this, bottomNavigation)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
    }


    override fun onResume() {
        super.onResume()
        BottomNavigationViewHelper.Check.checkMenuItem(bottomNavigation,activityNum)
    }


}
