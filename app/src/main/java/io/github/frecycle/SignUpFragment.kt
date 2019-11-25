package io.github.frecycle

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import io.github.frecycle.util.FirebaseMethods

class SignUpFragment : Fragment() {
    private lateinit var firebaseMethods : FirebaseMethods

    private lateinit var tvName : TextView
    private lateinit var tvEmail : TextView
    private lateinit var tvPhone : TextView
    private lateinit var tvPassword : TextView
    private lateinit var cbTermsOfUse : CheckBox


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_sign_up, container, false)

        firebaseMethods = FirebaseMethods(activity!!)

        tvName = view.findViewById(R.id.userFNameText)
        tvEmail = view.findViewById(R.id.userEmailText)
        tvPassword = view.findViewById(R.id.userPasswordText)
        tvPhone = view.findViewById(R.id.userPhoneText)
        cbTermsOfUse = view.findViewById(R.id.termsOfUseCheckBox)

        val btnSignUp: Button = view.findViewById(R.id.signUpButton)

        btnSignUp.setOnClickListener{

            if(validateSignUpInputs(tvName.text.toString(),tvEmail.text.toString(),tvPassword.text.toString(),tvPhone.text.toString())){
                firebaseMethods.registerNewEmail(tvName.text.toString(),tvPassword.text.toString(),tvEmail.text.toString(),tvPhone.text.toString().toLong(),
                    activity!!
                )
            }
        }

        return view
    }

    private fun validateSignUpInputs(name:String,email:String,password:String,phone:String):Boolean{

        if(name.isEmpty()){
            tvName.error = (getString(R.string.input_error_name))
            tvName.requestFocus()
            return false
        }

        if(email.isEmpty()){
            tvEmail.error = (getString(R.string.input_error_email))
            tvEmail.requestFocus()
            return false
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            tvEmail.error = (getString(R.string.input_error_email_invalid))
            tvEmail.requestFocus()
            return false
        }

        if(phone.isEmpty()){
            tvPhone.error = (getString(R.string.input_error_phone))
            tvPhone.requestFocus()
            return false
        }

        if(phone.length != 10){
            tvPhone.error = (getString(R.string.input_error_phone_invalid))
            tvPhone.requestFocus()
            return false
        }

        if(firebaseMethods.checkIfPhoneExists(phone.toLong())){
            tvPhone.error = (getString(R.string.input_error_phone_exists))
            tvPhone.requestFocus()
        }

        if(password.isEmpty()){
            tvPassword.error = getString(R.string.input_error_password)
            tvPassword.requestFocus()
            return false
        }

        if(password.length < 6){
            tvPassword.error = getString(R.string.input_error_password_length)
            tvPassword.requestFocus()
            return false
        }

        if(!cbTermsOfUse.isChecked){
            cbTermsOfUse.error = getString(R.string.input_error_checkBox_unchecked)
            cbTermsOfUse.requestFocus()
            return false
        }

        return true
    }



}
