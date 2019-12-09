package io.github.frecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import io.github.frecycle.util.BottomNavigationViewHelper

class AccountActivity : AppCompatActivity() {
    private lateinit var bottomNavigation: BottomNavigationViewEx
    private lateinit var signInUpActivity: SignInUpActivity
    private val activityNum: Int = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        bottomNavigation = this.findViewById(R.id.bottom_nav)

        // BottomNavigationView activity changer
        BottomNavigationViewHelper.setupBottomNavigationView(applicationContext, this, bottomNavigation)

        signInUpActivity = SignInUpActivity()
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }

    override fun onResume() {
        super.onResume()
        BottomNavigationViewHelper.Check.checkMenuItem(bottomNavigation, activityNum)
    }

    fun accountLoginClicked(view: View) {
        val intent = Intent(applicationContext, SignInUpActivity::class.java)
        startActivity(intent)
    }

    fun accountSignUpClicked(view: View){
        val intent = Intent(applicationContext,signInUpActivity::class.java)
        startActivity(intent)

       // TODO sign up sayfası yüklenecek
    }

    override fun onBackPressed() {
        val startMainAcivity = Intent(applicationContext,HomeActivity::class.java)
        startMainAcivity.addCategory(Intent.CATEGORY_HOME)
        startMainAcivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMainAcivity)
    }

}

