package io.github.frecycle.util

import android.R
import android.app.Activity
import android.content.Context
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import io.github.frecycle.models.User


class FirebaseMethods {
    private lateinit var auth : FirebaseAuth
    private lateinit var authListener : FirebaseAuth.AuthStateListener
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var context: Context
    private lateinit var user_id : String


    constructor(context: Context) {
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference

        this.context = context

        if(auth.currentUser != null){
            user_id = auth.currentUser!!.uid
        }
    }

    //TODO result değişkeni pass-by-value olarak değiştiği için bir düzeltme şart
    fun checkIfPhoneExists(phone : Long):Boolean{

        val query: Query = databaseReference
            .child("users")
            .orderByChild("phone")
            .equalTo(phone.toString())

        var result : Boolean = false
        var num: Int = 5

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                loop@for (data in dataSnapshot.children) {
                     Log.d("ABUZERonChange",data.exists().toString())
                    if (data.exists()){
                        result = true
                        break@loop
                    }
                }

                Log.d("ABUZERafterFor",result.toString())
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
            Log.d("ABUZERafterReturn",result.toString())
        return result
    }

    fun registerNewEmail(name :  String, password : String, email : String , phone : Long, activity : Activity){

        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    user_id = auth.currentUser!!.uid
                    auth.currentUser?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build()) // set display name

                    addNewUser(user_id,name,email,phone)

                    sendVerificationMail()

                    Toast.makeText(context,"Registration successful. Please check your email!", Toast.LENGTH_LONG).show()

                    activity.onBackPressed()

                }
            }.addOnFailureListener { exception ->
                Toast.makeText(context,exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
            }
    }

    private fun sendVerificationMail() {
        val user : FirebaseUser? = auth.currentUser

        if(user != null){
            user.sendEmailVerification()
                .addOnFailureListener { exception ->
                        Toast.makeText(context,exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
                }
        }
    }

    fun addNewUser(user_id : String, name : String, email : String, phone: Long){
        val user : User = User(user_id,name,email,phone,0.0f,"","")

        databaseReference.child("users")
            .child(user_id)
            .setValue(user)

    }

    fun getUserData(dataSnapshot: DataSnapshot) : User {
        val user = User()

        for (ds : DataSnapshot in dataSnapshot.children){
            if(ds.key.equals("users")){
                try {
                    user.name = ds.child(user_id).getValue(User::class.java)!!.name

                    user.city = ds.child(user_id).getValue(User::class.java)!!.city

                    user.email = ds.child(user_id).getValue(User::class.java)!!.email

                    user.phone = ds.child(user_id).getValue(User::class.java)!!.phone

                    user.profile_photo = ds.child(user_id).getValue(User::class.java)!!.profile_photo

                    user.rank = ds.child(user_id).getValue(User::class.java)!!.rank

                    user.user_id = ds.child(user_id).getValue(User::class.java)!!.user_id

                }catch ( e: NullPointerException){
                    Log.d("FirebaseMethods: ", e.message)
                }
            }
        }
        return user
    }


}