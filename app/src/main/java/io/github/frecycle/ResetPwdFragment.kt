package io.github.frecycle


import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ResetPwdFragment : Fragment() {
    private lateinit var auth : FirebaseAuth
    private lateinit var resetMail : TextView
    private lateinit var resetPwdButton : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_reset_pwd, container, false)

        auth = FirebaseAuth.getInstance()

        resetMail = view.findViewById(R.id.resetPwdMail)
        resetPwdButton = view.findViewById(R.id.resetPwdButton)

        resetPwdButton.setOnClickListener {
            if(validateResetPwInput()){
                auth.sendPasswordResetEmail(resetMail.text.toString()).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        Toast.makeText(activity?.applicationContext, "Email sent!", Toast.LENGTH_LONG).show()
                        activity?.onBackPressed()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(activity?.applicationContext, exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
        return view
    }

    private fun validateResetPwInput():Boolean{
        if (resetMail.text.toString().isEmpty()){
            resetMail.error = (getString(R.string.input_error_email))
            resetMail.requestFocus()
            return false
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(resetMail.text.toString()).matches()){
            resetMail.error = (getString(R.string.input_error_email_invalid))
            resetMail.requestFocus()
            return false
        }
        return true
    }

}
