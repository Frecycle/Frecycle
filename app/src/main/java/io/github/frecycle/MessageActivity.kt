package io.github.frecycle

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.github.frecycle.models.MessageModel
import io.github.frecycle.models.Product
import io.github.frecycle.models.User
import io.github.frecycle.util.MessagesUserAdapter

class MessageActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var reference : DatabaseReference

    private lateinit var models: ArrayList<MessageModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        models = ArrayList()

        setupFirebaseAuth()
        loadUsers()
    }

    private fun initRecyclerView(models: ArrayList<MessageModel>) {
        val recyclerView : RecyclerView = findViewById(R.id.recyclerViewMessages)
        val messagesUserAdapter = MessagesUserAdapter(this, models)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = messagesUserAdapter
    }

    private fun loadUsers() {
        val productRequesters : LinkedHashMap<String,ArrayList<String>> = LinkedHashMap()
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds: DataSnapshot in dataSnapshot.children){
                    if (ds.key.equals("products_requests")){
                        ds.child(auth.currentUser!!.uid).children.forEach { products->
                            val users = ArrayList<String>()
                            val productId = products.key.toString()
                            productRequesters[productId] = users
                            products.children.forEach{ userNAnswer ->
                                productRequesters[productId]!!.add(userNAnswer.key.toString())
                            }
                        }
                    }
                }

                for (entries: Map.Entry<String,List<String>> in productRequesters.entries){
                    val productId = entries.key
                    val users = entries.value

                    val model = MessageModel()

                    for (ds: DataSnapshot in dataSnapshot.children){
                        if (ds.key.equals("products")){
                            model.product_name = ds.child(productId).getValue(Product::class.java)!!.product_name
                        }
                        if(ds.key.equals("products_photos")){
                            model.product_photo = ds.child(productId).children.first().value.toString()
                        }
                    }

                    for (userId : String in users){
                        for (ds: DataSnapshot in dataSnapshot.children){
                            if (ds.key.equals("users")){
                                model.user_name = ds.child(userId).getValue(User::class.java)!!.name
                                model.profile_photo = ds.child(userId).getValue(User::class.java)!!.profile_photo
                            }
                        }
                        models.add(model)
                    }
                }
                initRecyclerView(models)
            }

            override fun onCancelled(e: DatabaseError) {
                Log.e("MessageActivity", "Users load cancelled!")
            }
        })
    }

    private fun setupFirebaseAuth() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database.reference
    }
}
