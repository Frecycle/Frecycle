package io.github.frecycle


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mikhaellopez.circularimageview.CircularImageView
import io.github.frecycle.models.User
import io.github.frecycle.util.FirebaseMethods
import io.github.frecycle.util.UniversalImageLoader

class EditProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var methods : FirebaseMethods

    private lateinit var profilePhoto : ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        setupFirebaseAuth()

        return view
    }

    private fun setupFirebaseAuth(){
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database.reference
        methods = FirebaseMethods(activity!!)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                initializeUserData(methods.getUserData(dataSnapshot))
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    private fun initializeUserData(user : User){
        val photoView : CircularImageView = view!!.findViewById(R.id.profilePhoto)
        UniversalImageLoader.setImage(user.profile_photo,photoView,null,"")

        val name : TextView = view!!.findViewById(R.id.editDisplayName)
        name.text = user.name

        val email : TextView = view!!.findViewById(R.id.editEmail)
        email.text = user.email

        val phone : TextView = view!!.findViewById(R.id.editPhone)
        phone.text = user.phone.toString()

    }

    private fun setProfileImage(){
        profilePhoto = view!!.findViewById(R.id.profilePhoto)!!
        val photoURL = "galeri14.uludagsozluk.com/761/lagertha-lothbrok_1042575_m.png"
        UniversalImageLoader.setImage(photoURL, profilePhoto, null, "https://")
    }



}
