package io.github.frecycle

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.github.frecycle.models.MessageListModel
import io.github.frecycle.models.Product
import io.github.frecycle.models.User
import io.github.frecycle.util.MessageListAdapter

class MessageListActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var reference : DatabaseReference

    private lateinit var models: ArrayList<MessageListModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)

        models = ArrayList()

        val toolbar: Toolbar = findViewById(R.id.messageListToolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }

        setupFirebaseAuth()
        loadUsers()
    }

    private fun initRecyclerView(models: ArrayList<MessageListModel>) {
        val recyclerView : RecyclerView = findViewById(R.id.recyclerViewMessages)
        val messagesUserAdapter = MessageListAdapter(this, models)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = messagesUserAdapter
    }

    private fun loadUsers() {
        val productRequesters : LinkedHashMap<String,ArrayList<String>> = LinkedHashMap()
        val currentUserRequests: LinkedHashMap<String,String> = LinkedHashMap()
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds: DataSnapshot in dataSnapshot.children){
                    if (ds.key.equals("products_requests")){

                        // received
                        ds.child(auth.currentUser!!.uid).children.forEach { products->
                            val productId = products.key.toString()
                            productRequesters[productId] = ArrayList<String>()
                            products.children.forEach{ userNAnswer ->
                                productRequesters[productId]!!.add(userNAnswer.key.toString())
                            }
                        }
                        // send
                        for(snapShot: DataSnapshot in ds.children){
                            snapShot.children.forEach { productId->
                                if (productId.hasChild(auth.currentUser!!.uid)){
                                    val productIdReq = productId.key.toString()
                                    //product-user
                                    currentUserRequests[productIdReq] = snapShot.key.toString()
                                }
                            }
                        }
                    }
                }
                //received
                for (entries: Map.Entry<String,List<String>> in productRequesters.entries){
                    val productId = entries.key
                    val users = entries.value

                    var product_name: String? = null
                    var product_photo: String? = null

                    for (ds: DataSnapshot in dataSnapshot.children){
                        if (ds.key.equals("products")){
                            product_name = ds.child(productId).getValue(Product::class.java)!!.product_name
                        }
                        if(ds.key.equals("products_photos")){
                            product_photo = ds.child(productId).children.first().value.toString()
                        }
                    }

                    for (userId : String in users){
                        var model4User: MessageListModel? = null
                        for (ds: DataSnapshot in dataSnapshot.children){
                            if (ds.key.equals("users")){
                                val username = ds.child(userId).getValue(User::class.java)!!.name
                                val profile_photo = ds.child(userId).getValue(User::class.java)!!.profile_photo
                                model4User = MessageListModel(product_name!!, username, userId, productId, profile_photo, product_photo!!, true)
                            }
                        }
                        models.add(model4User!!)
                    }
                }

                //send
                for (productId: String in currentUserRequests.keys){
                    var product_name: String? = null
                    var product_photo: String? = null

                    for (ds: DataSnapshot in dataSnapshot.children){
                        if (ds.key.equals("products")){
                            product_name = ds.child(productId).getValue(Product::class.java)!!.product_name
                        }
                        if(ds.key.equals("products_photos")){
                            product_photo = ds.child(productId).children.first().value.toString()
                        }
                    }


                    for (ds: DataSnapshot in dataSnapshot.children){
                        if (ds.key.equals("users")){
                            var model4User: MessageListModel? = null
                            val username = ds.child(currentUserRequests[productId]!!).getValue(User::class.java)!!.name
                            val profile_photo = ds.child(currentUserRequests[productId]!!).getValue(User::class.java)!!.profile_photo
                            model4User = MessageListModel(product_name!!, username, currentUserRequests[productId]!!, productId, profile_photo, product_photo!!, false)
                            models.add(model4User!!)
                        }

                    }
                }



                initRecyclerView(models)
            }

            override fun onCancelled(e: DatabaseError) {
                Log.e("MessageListActivity", "Users load cancelled!")
            }
        })
    }

    private fun setupFirebaseAuth() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database.reference
    }
}
