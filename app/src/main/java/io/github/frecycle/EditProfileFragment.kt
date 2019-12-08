package io.github.frecycle


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
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
    private lateinit var userId : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        setupCitySpinner(view)
        setupFirebaseAuth()

        return view
    }

    private fun setupFirebaseAuth(){
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database.reference
        methods = FirebaseMethods(activity!!)
        userId = auth.currentUser!!.uid

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

        val city : Spinner = view!!.findViewById(R.id.editCity)
        city.setSelection(resources.getStringArray(R.array.city_array).indexOf(user.city))

        val phone : TextView = view!!.findViewById(R.id.editPhone)
        phone.text = user.phone.toString()

    }

    private fun saveProfileSettings(){
        val email = (view!!.findViewById(R.id.editEmail) as TextView).text



        reference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user : User = User()
                for (ds : DataSnapshot in dataSnapshot.child("users").children){
                    if (ds.key!!.equals(userId)){
                        user.email = ds.getValue(User::class.java)!!.email
                        user.phone = ds.getValue(User::class.java)!!.phone
                    }
                }

                // case 1: user did not change their email
                if(user.email.equals(email)){

                }
                // case 2 : user changed their email
                else{

                }


            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented")
            }
        })
    }

    private fun setupCitySpinner(view : View) {
        val citySpinner: Spinner = view.findViewById(R.id.editCity)
        val staticAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            view.context,
            R.array.city_array,
            R.layout.snippet_spinner_item
        )
        staticAdapter.setDropDownViewResource(R.layout.snippet_spinner_dropdown_item)
        citySpinner.adapter = staticAdapter
    }

}
