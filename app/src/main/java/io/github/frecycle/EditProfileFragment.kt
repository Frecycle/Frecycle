package io.github.frecycle


import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.mikhaellopez.circularimageview.CircularImageView
import io.github.frecycle.models.User
import io.github.frecycle.util.FirebaseMethods
import io.github.frecycle.util.UniversalImageLoader
import java.lang.NullPointerException

class EditProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var methods : FirebaseMethods
    private lateinit var userId : String
    private lateinit var user : User

    //widgets
    private lateinit var tvDisplayName : TextView
    private lateinit var tvEditEmail : TextView
    private lateinit var spEditCity : Spinner
    private lateinit var tvEditPhone : TextView
    private lateinit var profilePhoto: ImageView
    private lateinit var checkmark : ImageButton

    private val GALLERY_PICK = 1
    private var imageUri : Uri? = null
    private var downloadPhotoURL: String = "None"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        setupCitySpinner(view)
        setupFirebaseAuth()
        initializeWidgets(view)
        initializeListeners()

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
        profilePhoto = view.findViewById(R.id.profilePhotoEdit)
    }

    fun initializeListeners(){
        profilePhoto.setOnClickListener{ openGallery() }
    }

    private fun setupFirebaseAuth(){
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database.reference
        storageReference = FirebaseStorage.getInstance().reference
        methods = FirebaseMethods(activity!!)
        userId = auth.currentUser!!.uid

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                initializeUserData(methods.getUserData(dataSnapshot))
            }

            override fun onCancelled(p0: DatabaseError) {
               Log.e("EditProfieFragment", p0.message)
            }
        })
    }

    // sets widgets with data coming from database
    private fun initializeUserData(user : User){
        try {
            val photoView : CircularImageView = view!!.findViewById(R.id.profilePhotoEdit)
            UniversalImageLoader.setImage(user.profile_photo,photoView,null,"")

            this.user = user

            tvDisplayName.text = user.name
            tvEditEmail.text = user.email
            spEditCity.setSelection(resources.getStringArray(R.array.city_array).indexOf(user.city))
            tvEditPhone.text = user.phone.toString()
        }catch (e: NullPointerException){
            Log.e("EditProfileFragment", "NullPointerException :(")
        }
    }

    private fun saveProfileSettings(){
        val name = tvDisplayName.text.toString()
        val phone = tvEditPhone.text.toString()
        val city = spEditCity.selectedItem.toString()
        val progressBar: ProgressBar = activity!!.findViewById(R.id.editProfileProgressBar)

         if(validateData(name,phone,city)){
             progressBar.visibility = View.VISIBLE
             val query = reference.child("users").child(auth.currentUser!!.uid)

             query.addListenerForSingleValueEvent(object : ValueEventListener {
                 override fun onDataChange(dataSnapshot: DataSnapshot) {

                     if (user.name.equals(name) && user.phone.toString().equals(phone) && user.city.equals(city) && imageUri == null) {
                         Toast.makeText(context!!.applicationContext, getString(R.string.nothing_changed),Toast.LENGTH_SHORT).show()
                         progressBar.visibility = View.INVISIBLE

                     }else{

                         if(imageUri != null){
                             val filePath = storageReference.child("user_photos").child(auth.currentUser!!.uid + ".jpg")

                                 filePath.putFile(imageUri!!).continueWithTask { task ->
                                     if (!task.isSuccessful){
                                         throw task.exception!!
                                     }
                                     downloadPhotoURL = filePath.downloadUrl.toString()
                                     filePath.downloadUrl
                                 }.addOnCompleteListener { task ->
                                     if(task.isSuccessful){
                                         downloadPhotoURL = task.result.toString()
                                         query.child("profile_photo").setValue(downloadPhotoURL)
                                     }
                                 }
                         }

                         query.child("name").setValue(name)
                         query.child("phone").setValue(phone.toLong())
                         query.child("city").setValue(city)

                         Toast.makeText(context!!.applicationContext, getString(R.string.data_changed),Toast.LENGTH_SHORT).show()
                         progressBar.visibility = View.INVISIBLE
                         activity!!.finish()
                     }
                 }
                 override fun onCancelled(p0: DatabaseError) {
                     Log.e("EditProfile:saveSttngs", p0.message)
                 }
             })
         }
    }

    private fun validateData(name: String, phone: String, city: String):Boolean {
        if(phone.isEmpty()){
            tvEditPhone.error = (getString(R.string.input_error_phone))
            tvEditPhone.requestFocus()
            return false
        }

        if(phone.length != 10){
            tvEditPhone.error = (getString(R.string.input_error_phone_invalid))
            tvEditPhone.requestFocus()
            return false
        }

        if(methods.checkIfPhoneExists(phone.toLong())){
            tvEditPhone.error = (getString(R.string.input_error_phone_exists))
            tvEditPhone.requestFocus()
        }

        return true
    }

    private fun openGallery(){
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent,GALLERY_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GALLERY_PICK && resultCode == Activity.RESULT_OK && data != null){
            imageUri = data.data
            profilePhoto.setImageURI(imageUri)
        }
    }

/*    private fun checkIfEmailExists(email: String){
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
    }*/

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
