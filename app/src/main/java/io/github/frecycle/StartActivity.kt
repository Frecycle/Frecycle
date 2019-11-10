package io.github.frecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_account.*


class StartActivity : AppCompatActivity() {

    private lateinit var homeFragment: HomeFragment
    private lateinit var favoritesFragment: FavoritesFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var myAccountFragment: AccountFragment

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        auth = FirebaseAuth.getInstance()

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_nav)

            homeFragment = HomeFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.relative_layout, homeFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()


        bottomNavigation.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {

                R.id.navigation_home -> {
                    homeFragment = HomeFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.relative_layout, homeFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }

                R.id.navigation_search -> {
                    searchFragment = SearchFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.relative_layout, searchFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }

                R.id.navigation_favorites -> {
                    favoritesFragment = FavoritesFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.relative_layout, favoritesFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }

                R.id.navigation_myAccount -> {
                    myAccountFragment = AccountFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.relative_layout, myAccountFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }

            }
            true


            /*       auth = FirebaseAuth.getInstance()
                   val currentUser = auth.currentUser

                   if(currentUser != null){
                       val intent = Intent(applicationContext,FeedActivity::class.java)
                       startActivity(intent)
                       finish()
                   }*/
        }
    }

    fun signUpTextClicked(view : View){
        val intent = Intent(applicationContext, SignUpActivity::class.java)
        startActivity(intent)
        onResume()
    }

    fun loginButtonClicked(view : View){
        val email = userEmailText.text.toString()
        val pass = passwordText.text.toString()
        if(!email.isBlank() && !pass.isEmpty()) {
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Welcome ${auth.currentUser?.displayName.toString()}", Toast.LENGTH_LONG).show()
                    val intent = Intent(applicationContext, FeedActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener { exception ->
                if (exception != null) {
                    Toast.makeText(applicationContext, exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }else{
            Toast.makeText(applicationContext,"Email and Password cannot be empty.",Toast.LENGTH_LONG).show()
        }
    }


}
