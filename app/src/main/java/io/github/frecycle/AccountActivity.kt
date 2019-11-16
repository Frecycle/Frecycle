package io.github.frecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*

class AccountActivity : AppCompatActivity() {
    private lateinit var bottomNavigation : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

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
                    val intent = Intent(applicationContext, FavoritesActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                }

                R.id.navigation_myAccount -> {

                }

            }
            true
        }
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
    }

    fun accountLoginClicked(view: View) {
        val intent = Intent(applicationContext, UserOperationsActivity::class.java)
        startActivity(intent)
        onResume()
    }

    override fun onResume() {
        super.onResume()
        checkMenuItem()
    }

    private fun checkMenuItem(){
        val menu : Menu =  bottomNavigation.menu
        val menuItem : MenuItem = menu.getItem(3)
        menuItem.isChecked = true
    }

}
