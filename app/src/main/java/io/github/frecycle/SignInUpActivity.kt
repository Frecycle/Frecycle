package io.github.frecycle

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

import io.github.frecycle.util.FirebaseMethods

class SignInUpActivity : AppCompatActivity() {

    private lateinit var firebaseMethods : FirebaseMethods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_up)

        firebaseMethods = FirebaseMethods(this)

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
            .replace(R.id.userOperationsLayout, LoginFragment(),"loginFragment")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStack()
        }else{
            //TODO nohistory account activity erişilemediği için uygulamadan çıkıyor
            val backActivity = Intent(applicationContext,AccountActivity::class.java)
            startActivity(backActivity)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            finish()
        }
    }

}
