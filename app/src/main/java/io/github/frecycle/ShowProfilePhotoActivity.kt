package io.github.frecycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth

class ShowProfilePhotoActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_profile_photo)

        imageView = findViewById(R.id.profilePhotoView)
        //imageView.setImageURI(FirebaseAuth.getInstance().currentUser?.photoUrl)
        imageView.setImageResource(R.drawable.maggie)

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
