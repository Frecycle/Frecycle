package io.github.frecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.userEmailText
import kotlinx.android.synthetic.main.fragment_reset_pwd.*
import kotlinx.android.synthetic.main.fragment_sign_up.*

class UserOperationsActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var loginFragment: LoginFragment
    private lateinit var signUpFragment: SignUpFragment
    private lateinit var resetPwdFragment: ResetPwdFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_operations)

        auth = FirebaseAuth.getInstance()
        loginFragment = LoginFragment()
        resetPwdFragment = ResetPwdFragment()
        signUpFragment = SignUpFragment()

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
            .replace(R.id.userOperationsLayout, loginFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    fun signUpTextClicked(view : View){
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
            .replace(R.id.userOperationsLayout, signUpFragment)
            .addToBackStack("login_fragment")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    fun resetPwdTextClicked(view: View){
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
            .replace(R.id.userOperationsLayout, resetPwdFragment)
            .addToBackStack("login_fragment")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
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
            Toast.makeText(applicationContext,"Email and Password cannot be empty.", Toast.LENGTH_LONG).show()
        }
    }

    fun signUpButtonClicked(view : View){
        val name = userFNameText.text.toString()
        val email = userEmailText.text.toString()
        val password = userPasswordText.text.toString()
        val phone = userPhoneText.text.toString()

        if(!validateSignUpInputs(name,email,password,phone)){
            return
        }

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful){

                auth.currentUser?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build()) // set display name

                // save the current user to DATABASE
                val user = User(name,email,phone)
                FirebaseDatabase.getInstance().getReference("users")
                    .child(auth.currentUser!!.uid)
                    .setValue(user)

                Toast.makeText(applicationContext,"Registration successful!", Toast.LENGTH_LONG).show()

                onBackPressed()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun resetPwdButtonClicked(view : View){
        val resetMail = resetPwdMail.text.toString()

        if(validateResetPwInput()){
            auth.sendPasswordResetEmail(resetMail).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(applicationContext, "Email sent!", Toast.LENGTH_LONG).show()
                    onBackPressed()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validateSignUpInputs(name:String,email:String,password:String,phone:String):Boolean{

        if(name.isEmpty()){
            userFNameText.error = (getString(R.string.input_error_name))
            userFNameText.requestFocus()
            return false
        }

        if(email.isEmpty()){
            userEmailText.error = (getString(R.string.input_error_email))
            userEmailText.requestFocus()
            return false
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            userEmailText.error = (getString(R.string.input_error_email_invalid))
            userEmailText.requestFocus()
            return false
        }

        if(phone.isEmpty()){
            userPhoneText.error = (getString(R.string.input_error_phone))
            userPhoneText.requestFocus()
            return false
        }

        if(phone.length != 10){
            userPhoneText.error = (getString(R.string.input_error_phone_invalid))
            userPhoneText.requestFocus()
            return false
        }

        if(password.isEmpty()){
            userPasswordText.error = getString(R.string.input_error_password)
            userPasswordText.requestFocus()
            return false
        }

        if(password.length < 6){
            userPasswordText.error = getString(R.string.input_error_password_length)
            userPasswordText.requestFocus()
            return false
        }

        if(!termsOfUseCheckBox.isChecked){
            termsOfUseCheckBox.error = getString(R.string.input_error_checkBox_unchecked)
            termsOfUseCheckBox.requestFocus()
            return false
        }

        return true
    }

    private fun validateResetPwInput():Boolean{
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

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStack()
        }else{
            super.onBackPressed()
        }
    }
}
