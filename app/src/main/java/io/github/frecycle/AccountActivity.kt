package io.github.frecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.github.frecycle.util.BottomNavigationViewHelper

class AccountActivity : AppCompatActivity() {
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var signInUpActivity: SignInUpActivity
    private var activityNum: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        activityNum = Integer.parseInt( intent.extras["activityNum"].toString())

        bottomNavigation = this.findViewById(R.id.bottom_nav)

        // BottomNavigationView activity changer
        BottomNavigationViewHelper.setupBottomNavigationView(applicationContext, this, bottomNavigation)

        val welcomeImage = findViewById<ImageView>(R.id.welcomeImage)
        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        val welcomeDescription = findViewById<TextView>(R.id.welcomeDescription)

        if(activityNum == 3){
            welcomeImage.setImageResource(R.drawable.ic_account_circle_background)
            welcomeText.text = getString(R.string.myAccount)
        }else{
            welcomeImage.setImageResource(R.drawable.ic_account_star_background)
            welcomeText.text = getString(R.string.fav)
            welcomeDescription.text = getString(R.string.should_login_to_continue)
        }

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

