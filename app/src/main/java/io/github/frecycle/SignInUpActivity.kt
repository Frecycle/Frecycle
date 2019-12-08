package io.github.frecycle

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

import io.github.frecycle.util.FirebaseMethods

class SignInUpActivity : AppCompatActivity() {

    private lateinit var firebaseMethods : FirebaseMethods

    private lateinit var loginFragment: LoginFragment
    private lateinit var signUpFragment: SignUpFragment
    private lateinit var resetPwdFragment: ResetPwdFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_up)

        firebaseMethods = FirebaseMethods(this)

        initFragments()

        changeFragment(loginFragment,null)
    }

    private fun initFragments(){
        loginFragment = LoginFragment()
        resetPwdFragment = ResetPwdFragment()
        signUpFragment = SignUpFragment()
    }

    fun signUpTextClicked(view : View){
        changeFragment(signUpFragment,"login_fragment")
    }

    fun resetPwdTextClicked(view: View){
        changeFragment(resetPwdFragment,"login_fragment")
    }

    private fun changeFragment(fragment: Fragment, backStack: String?){
        if(backStack != null){
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .replace(R.id.userOperationsLayout, fragment)
                .addToBackStack(backStack)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }else{
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .replace(R.id.userOperationsLayout, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }
    }


    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStack()
        }else{
            super.onBackPressed()
        }
    }
}
