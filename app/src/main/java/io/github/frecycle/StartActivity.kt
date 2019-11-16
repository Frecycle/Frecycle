package io.github.frecycle

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account.userEmailText
import kotlinx.android.synthetic.main.fragment_reset_pwd.*


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

        homeFragment = HomeFragment()
        searchFragment = SearchFragment()
        favoritesFragment = FavoritesFragment()
        myAccountFragment = AccountFragment()

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.relative_layout, homeFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_nav)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.relative_layout, homeFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }

                R.id.navigation_search -> {

                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.relative_layout, searchFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }

                R.id.navigation_favorites -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.relative_layout, favoritesFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }

                R.id.navigation_myAccount -> {
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
            loginProgressBar.visibility = View.VISIBLE
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Welcome ${auth.currentUser?.displayName.toString()}", Toast.LENGTH_LONG).show()
                    val intent = Intent(applicationContext, FeedActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener { exception ->
                loginProgressBar.visibility = View.INVISIBLE
                Toast.makeText(applicationContext, exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(applicationContext,"Email and Password cannot be empty.",Toast.LENGTH_LONG).show()
        }
    }

    fun resetPwdTextClicked(view: View){
        supportFragmentManager.beginTransaction()
            .replace(R.id.relative_layout, ResetPwdFragment())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    fun resetPassword(view : View){
        val resetMail = resetPwdMail.text.toString()

        if(resetMailValidate()){
            auth.sendPasswordResetEmail(resetMail).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(applicationContext, "Email sent!", Toast.LENGTH_LONG).show()

                    supportFragmentManager.beginTransaction()
                        .remove(supportFragmentManager.findFragmentById(R.id.relative_layout)!!)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .commit()

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.relative_layout, myAccountFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun resetMailValidate():Boolean{
        if (resetPwdMail.text.toString().isEmpty()){
            resetPwdMail.error = (getString(R.string.input_error_email))
            resetPwdMail.requestFocus()
            return false
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(resetPwdMail.text.toString()).matches()){
            resetPwdMail.error = (getString(R.string.input_error_email_invalid))
            resetPwdMail.requestFocus()
            return false
        }
        return true
    }
}

//TODO onBackPressed action