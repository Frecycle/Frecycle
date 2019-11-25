package io.github.frecycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.github.frecycle.models.User
import io.github.frecycle.util.FirebaseMethods
import io.github.frecycle.util.UniversalImageLoader

class ShowProfilePhotoActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var methods : FirebaseMethods

    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_profile_photo)

        //imageView.setImageResource(R.drawable.maggie)

        setupFirebaseAuth()

    }


    private fun setupFirebaseAuth(){
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database.reference
        methods = FirebaseMethods(this)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                initializeProfilePhoto(methods.getUserData(dataSnapshot))
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    private fun initializeProfilePhoto(user : User){
        val progressBar : ProgressBar = findViewById(R.id.showProfilePhotoProgressBar)

        imageView = findViewById(R.id.profilePhotoView)
        UniversalImageLoader.setImage(user.profile_photo,imageView,progressBar,"")
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
        finish()
    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(0,0)
    }

}
