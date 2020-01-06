package io.github.frecycle

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.github.frecycle.models.Chat
import io.github.frecycle.models.User
import io.github.frecycle.util.MessageAdapter
import kotlin.collections.HashMap

class ChatActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var reference : DatabaseReference

    private lateinit var toolbar: Toolbar
    private lateinit var profilePhoto: ImageView
    private lateinit var username: TextView
    private lateinit var buttonSend: ImageButton
    private lateinit var textSend: EditText

    private lateinit var userId: String
    private lateinit var productId: String
    private lateinit var chats : ArrayList<Chat>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        userId = intent.getStringExtra("userId")
        productId = intent.getStringExtra("productId")

        initWidgets()
        setupFirebaseAuth()
        initChatPage()
    }

    private fun initChatPage() {
        val query: Query = reference.child("users").child(userId)

        query.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val profile_photo = dataSnapshot.getValue(User::class.java)!!.profile_photo;

                Glide.with(this@ChatActivity).load(profile_photo).into(profilePhoto)

                username.text = dataSnapshot.getValue(User::class.java)!!.name

                readMessage(auth.currentUser!!.uid, userId, profile_photo)

            }
            override fun onCancelled(e: DatabaseError) {
                Log.e("ChatActivity", e.message)
            }
        })

    }

    private fun initWidgets(){
        toolbar = findViewById(R.id.chatActivityToolBar)
        profilePhoto = findViewById(R.id.chatProfileImage)
        username = findViewById(R.id.chatUserName)
        buttonSend = findViewById(R.id.buttonSendMessage)
        textSend = findViewById(R.id.textSend)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }

        buttonSend.setOnClickListener{
            val message = textSend.text.toString()

            if (!message.equals("")){
                sendMessage(auth.currentUser!!.uid, userId, message, productId)
            }else{
                Toast.makeText(this@ChatActivity, getString(R.string.emtpy_message), Toast.LENGTH_SHORT).show()
            }

            textSend.setText("")
        }

    }

    private fun initRecyclerView(chats :ArrayList<Chat>, photoURL: String){
        val recyclerView : RecyclerView = findViewById(R.id.chatRecyclerView)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager
        val messageAdapter = MessageAdapter(this,chats, photoURL)
        recyclerView.adapter = messageAdapter
    }

    private fun setupFirebaseAuth() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database.reference
    }

    private fun sendMessage(sender: String, receiver: String, message: String, productId: String){

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap.put("productId", productId)
        hashMap.put("sender", sender)
        hashMap.put("receiver", receiver)
        hashMap.put("message", message)

        reference.child("chats").push().setValue(hashMap)

    }

    private fun readMessage(myId : String, userId: String, photoURL: String){
        chats = ArrayList()

        val query = reference.child("chats")

        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                chats.clear()

                for (snapShot: DataSnapshot in dataSnapshot.children){
                    val chat: Chat = snapShot.getValue(Chat::class.java)!!

                    if(chat.receiver.equals(myId) && chat.sender.equals(userId) && chat.productId.equals(productId) || chat.receiver.equals(userId) && chat.sender.equals(myId) && chat.productId.equals(productId)){
                        chats.add(chat)
                    }
                }

                initRecyclerView(chats, photoURL)

            }
            override fun onCancelled(e: DatabaseError) {
                Log.e("readMessage@Chat", e.message)
            }
        })
    }

}
