package io.github.frecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class FavoritesActivity : AppCompatActivity() {
    private lateinit var bottomNavigation : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        bottomNavigation = findViewById(R.id.bottom_nav)

        checkMenuItem()

        bottomNavigation.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                }

                R.id.navigation_search -> {
                    val intent = Intent(applicationContext, SearchActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                }

                R.id.navigation_favorites -> {

                }

                R.id.navigation_myAccount -> {
                    val intent = Intent(applicationContext, AccountActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                }

            }
            true
        }
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
    }

    override fun onResume() {
        super.onResume()
        checkMenuItem()
    }

    private fun checkMenuItem(){
        val menu : Menu =  bottomNavigation.menu
        val menuItem : MenuItem = menu.getItem(2)
        menuItem.isChecked = true
    }

}
