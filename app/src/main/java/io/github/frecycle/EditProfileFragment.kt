package io.github.frecycle


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
    private lateinit var user : User

    //widgets
    private lateinit var tvDisplayName : TextView
    private lateinit var tvEditEmail : TextView
    private lateinit var spEditCity : Spinner
    private lateinit var tvEditPhone : TextView
    private lateinit var checkmark : ImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        setupCitySpinner(view)
        setupFirebaseAuth()
        initializeWidgets(view)

        checkmark.setOnClickListener{
            saveProfileSettings()
        }

        return view
    }

    private fun initializeWidgets(view: View){
        tvDisplayName = view.findViewById(R.id.editDisplayName)
        tvEditEmail = view.findViewById(R.id.editEmail)
        spEditCity = view.findViewById(R.id.editCitySpinner)
        tvEditPhone = view.findViewById(R.id.editPhone)
        checkmark = view.findViewById(R.id.saveProfileChanges)
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

    // sets widgets with data coming from database
    private fun initializeUserData(user : User){
        val photoView : CircularImageView = view!!.findViewById(R.id.profilePhoto)
        UniversalImageLoader.setImage(user.profile_photo,photoView,null,"")

        this.user = user

        tvDisplayName.text = user.name
        tvEditEmail.text = user.email
        spEditCity.setSelection(resources.getStringArray(R.array.city_array).indexOf(user.city))
        tvEditPhone.text = user.phone.toString()

    }

    private fun saveProfileSettings(){
        val email = tvEditEmail.text.toString()
        val phone = tvEditPhone.text.toString()

        reference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // case 1: user made a change to their email
                if(!user.email.equals(email)){
                    checkIfEmailExists(email)
                }
                // case 2 : user changed their phone
                if(user.phone.equals(phone)){

                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented")
            }
        })
    }

    /***
     * Email veritabanında varsa ekliyor
     */
    private fun checkIfEmailExists(email: String){
        val query : Query = reference.child("users")
            .orderByChild("email")
            .equalTo(user.email)

        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(!dataSnapshot.exists()){
                    // update email
                    methods.updateEmail(email)
                    Toast.makeText(activity,"Saved email",Toast.LENGTH_SHORT).show()
                }

                for(singleSnapshot : DataSnapshot in dataSnapshot.children){
                    if(singleSnapshot.exists()){
                        Toast.makeText(activity,"That email already exists",Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    private fun setupCitySpinner(view : View) {
        val citySpinner: Spinner = view.findViewById(R.id.editCitySpinner)
        val staticAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            view.context,
            R.array.city_array,
            R.layout.snippet_spinner_item
        )
        staticAdapter.setDropDownViewResource(R.layout.snippet_spinner_dropdown_item)
        citySpinner.adapter = staticAdapter
    }

}
