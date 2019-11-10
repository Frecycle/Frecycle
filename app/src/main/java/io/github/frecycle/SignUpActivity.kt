package io.github.frecycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser

        if(currentUser != null){

        }
    }

    fun signUpButtonClicked(view : View){
        val name = userFNameText.text.toString()
        val email = userEmailText.text.toString()
        val password = userPasswordText.text.toString()
        val phone = userPhoneText.text.toString()

        if(!validateInputs(name,email,password,phone)){
            return
        }

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful){

                auth.currentUser?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build()) // set display name

                // save the current user to database
                val user = User(name,email,phone)
                FirebaseDatabase.getInstance().getReference("users")
                    .child(auth.currentUser!!.uid)
                    .setValue(user)

                Toast.makeText(applicationContext,"Registration successful!", Toast.LENGTH_LONG).show()

                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun validateInputs(name:String,email:String,password:String,phone:String):Boolean{

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

}


