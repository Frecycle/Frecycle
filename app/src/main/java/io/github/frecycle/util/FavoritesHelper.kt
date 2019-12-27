package io.github.frecycle.util

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.github.frecycle.R
import java.text.SimpleDateFormat
import java.util.*

class FavoritesHelper {
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var reference : DatabaseReference

    companion object{
        val instance : FavoritesHelper by lazy { FavoritesHelper() }
    }

    private constructor(){
        this.auth = FirebaseAuth.getInstance()
        this.database = FirebaseDatabase.getInstance()
        this.reference = database.reference
    }

    fun addToFavorites(applicationContext: Context, button : ImageButton, productId : String){
        val currentDate = SimpleDateFormat("MMM dd, yyyy HH:mm")
        val saveCurrentDate = currentDate.format(Calendar.getInstance().time)
        button.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.favorite))

        reference.child("user_favorites").child(auth.currentUser!!.uid).child(productId).setValue(saveCurrentDate).addOnCompleteListener{
            OnCompleteListener<Void> { task ->
                if(task.isSuccessful){
                    Toast.makeText(applicationContext,applicationContext.getString(R.string.productFavorited), Toast.LENGTH_LONG).show()
                    Log.d("FavoritesHelper", "product id is added to 'user_favorites' database")
                }else{
                    button.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.grey))
                    Toast.makeText(applicationContext,"Error: " + task.exception.toString(),
                        Toast.LENGTH_LONG).show()
                    Log.e("FavoritesHelper", "Error: product id cannot be added to 'user_favorites' database")
                }
            }

        }
    }

    fun removeFromFavorites(applicationContext: Context, button : ImageButton, productId : String){
        button.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.grey))
        reference.child("user_favorites").child(auth.currentUser!!.uid).child(productId).removeValue().addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(applicationContext,applicationContext.getString(R.string.productUnfavorited),Toast.LENGTH_LONG).show()
                Log.d("FavoritesHelper", "product id is removed to 'user_favorites' database")
            }else{
                button.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.favorite))
                Toast.makeText(applicationContext,"Error: " + task.exception.toString(),Toast.LENGTH_LONG).show()
                Log.e("FavoritesHelper", "Error: product id cannot be removed to 'user_favorites' database")
            }
        }
    }

    fun isFavorited(applicationContext: Context, button : ImageButton, productId : String){
        val query : Query = reference.child("user_favorites")
        var flag = false

        if (auth.currentUser != null){
            query.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (ds in dataSnapshot.children){
                        ds.child(auth.currentUser!!.uid).children.forEach { x->
                            if(x.key.equals(productId)){
                                button.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.favorite))
                                return@forEach
                            } }
                    }

                }
                override fun onCancelled(p0: DatabaseError) {
                    Log.e("FavoritesHelper", p0.message)
                }


            })
        }
    }



}