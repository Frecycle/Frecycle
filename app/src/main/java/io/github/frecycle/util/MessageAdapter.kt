package io.github.frecycle.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import io.github.frecycle.R
import io.github.frecycle.models.Chat


class MessageAdapter : RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    val MSG_TYPE_LEFT: Int = 0
    val MSG_TYPE_RIGHT: Int = 1

    private var mContext: Context
    private var mChat: List<Chat>
    private var photoURL: String

    constructor(mContext: Context, mChat: List<Chat>, photoURL: String){
        this.mContext = mContext
        this.mChat = mChat
        this.photoURL = photoURL
    }

    class MyViewHolder : RecyclerView.ViewHolder {
        var profileImage : ImageView
        var show_message : TextView


        constructor(itemView: View) : super(itemView) {
            show_message = itemView.findViewById(R.id.show_message)
            profileImage = itemView.findViewById(R.id.chatProfilePhoto)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        if (viewType == MSG_TYPE_RIGHT){
            val view: View = LayoutInflater.from(mContext).inflate(R.layout.snippet_chat_item_right, parent, false)
            return MyViewHolder(view)
        }else{
            val view: View = LayoutInflater.from(mContext).inflate(R.layout.snippet_chat_item_left, parent, false)
            return MyViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return mChat.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val chat: Chat = mChat[position]

        holder.show_message.text = chat.message

        if (photoURL.equals("None")){
            holder.profileImage.setImageResource(R.mipmap.ic_launcher_round)
            //TODO default image
        }else{
            Glide.with(mContext).load(photoURL).into(holder.profileImage)
        }

    }

    override fun getItemViewType(position: Int): Int {
        val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid
        if(mChat[position].sender.equals(currentUserUid)){
            return MSG_TYPE_RIGHT
        }else{
            return MSG_TYPE_LEFT
        }
    }
}