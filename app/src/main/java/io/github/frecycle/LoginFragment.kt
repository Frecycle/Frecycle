package io.github.frecycle

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private lateinit var auth : FirebaseAuth
    private lateinit var authListener: FirebaseAuth.AuthStateListener

    private lateinit var email : String
    private lateinit var password : String
    private lateinit var loginProgressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_login, container, false)

        val btnLogin: Button = view.findViewById(R.id.loginButton)

        setupFirebaseAuth()

        btnLogin.setOnClickListener{
            email = view.findViewById<TextView>(R.id.userEmailText).text.toString()
            password = view.findViewById<TextView>(R.id.passwordText).text.toString()
            loginProgressBar = view.findViewById(R.id.loginProgressBar)

            if(!email.isBlank() && !password.isEmpty()) {
                loginProgressBar.visibility = View.VISIBLE

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity?.applicationContext, "Welcome ${auth.currentUser?.displayName.toString()}", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener { exception ->
                    loginProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(activity?.applicationContext, exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(activity?.applicationContext,"Email and Password cannot be empty.", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    private fun setupFirebaseAuth(){
        auth = FirebaseAuth.getInstance()
        authListener = FirebaseAuth.AuthStateListener { auth ->

            val user = auth.currentUser
            if (user != null) {
                val intent = Intent(activity?.applicationContext, ProfileActivity::class.java)
                // It will totally clears all previous activity(s) and start new activity.
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                activity?.finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authListener)
    }

    override fun onPause() {
        super.onPause()
        auth.removeAuthStateListener(authListener)
    }

}