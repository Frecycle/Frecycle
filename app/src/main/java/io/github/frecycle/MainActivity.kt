package io.github.frecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser

        if(currentUser != null){
            val intent = Intent(applicationContext,FeedActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun singInClicked(view : View){
        val email = userEmailText.text.toString()
        val pass = passwordText.text.toString()
        if(!email.isBlank() && !pass.isEmpty()) {
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Welcome ${auth.currentUser?.email.toString()}",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(applicationContext, FeedActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener { exception ->
                if (exception != null) {
                    Toast.makeText(
                        applicationContext,
                        exception.localizedMessage.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }else{
            Toast.makeText(applicationContext,"Email and Password cannot be empty.",Toast.LENGTH_LONG).show()
        }
    }

    fun singUpClicked(view : View){
        val email = userEmailText.text.toString()
        val pass = passwordText.text.toString()
        if(!email.isBlank() && !pass.isEmpty()){
            auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val intent = Intent(applicationContext,FeedActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener { exception ->
                if(exception != null){
                    Toast.makeText(applicationContext,exception.localizedMessage.toString(),Toast.LENGTH_LONG).show()
                }
            }
        }else{
            Toast.makeText(applicationContext,"Email and Password cannot be empty.",Toast.LENGTH_LONG).show()
        }



    }
}
