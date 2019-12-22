package io.github.frecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.github.frecycle.util.BottomNavigationViewHelper


class SearchActivity : AppCompatActivity() {
    private lateinit var bottomNavigation : BottomNavigationView
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

    override fun onBackPressed() {
        val startMainAcivity = Intent(applicationContext,HomeActivity::class.java)
        startMainAcivity.addCategory(Intent.CATEGORY_HOME)
        startMainAcivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMainAcivity)
    }

}
