package io.github.frecycle


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener

class SignOutFragment : Fragment() {
    private lateinit var auth : FirebaseAuth
    private lateinit var authListener: AuthStateListener

    private lateinit var mProgressBar: ProgressBar
    private lateinit var tvSignOut: TextView
    private lateinit var tvSigningOut : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_sign_out, container, false)

        tvSignOut = view.findViewById(R.id.tvConfirmSignOut)
        mProgressBar = view.findViewById(R.id.progressBar)
        tvSigningOut = view.findViewById(R.id.tvSigningOut)

        val btnConfirmSignOut: Button = view.findViewById(R.id.btnConfirmSignOut)

        setupFirebaseAuth()

        btnConfirmSignOut.setOnClickListener {
            mProgressBar.visibility = View.VISIBLE
            tvSigningOut.visibility = View.VISIBLE
            auth.signOut()
            activity?.finish()
        }
        return view
    }

    private fun setupFirebaseAuth(){
        auth = FirebaseAuth.getInstance()
        authListener = AuthStateListener{ auth ->
        // çıkış yapılınca auth state değişir ve home activity'e gider
                val user = auth.currentUser
                if(user == null){
                    val intent =  Intent(activity?.applicationContext, HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
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
