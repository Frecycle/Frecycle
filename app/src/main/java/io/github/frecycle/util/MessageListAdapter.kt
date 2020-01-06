package io.github.frecycle.util

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.frecycle.ChatActivity
import io.github.frecycle.R
import io.github.frecycle.models.MessageListModel

class MessageListAdapter : RecyclerView.Adapter<MessageListAdapter.MyViewHolder> {
    private var mContext: Context
    private var mMessageListModel: List<MessageListModel>

    constructor(mContext: Context, mMessageListModel: List<MessageListModel>){
        this.mContext = mContext
        this.mMessageListModel = mMessageListModel
    }

    class MyViewHolder : RecyclerView.ViewHolder {
        var profileImage : ImageView
        var username : TextView
        var productImage : ImageView
        var productName : TextView
        var receiveStatus: TextView

        constructor(itemView: View) : super(itemView) {
            username = itemView.findViewById(R.id.messagesUserName)
            profileImage = itemView.findViewById(R.id.messagesProfileImage)
            productName = itemView.findViewById(R.id.messagesProductName)
            productImage = itemView.findViewById(R.id.messagesProductImage)
            receiveStatus = itemView.findViewById(R.id.user_item_received_status)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.messages_user_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mMessageListModel.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model: MessageListModel = mMessageListModel[position]
        holder.username.text = model.user_name
        if (model.profile_photo.equals("None", true)){
            holder.profileImage.setImageResource(R.mipmap.ic_launcher_round)
            //TODO default image
        }else{
            Glide.with(mContext).load(model.profile_photo).into(holder.profileImage)
        }

        if(model.received){
            holder.receiveStatus.text = mContext.getString(R.string.received)
        }else{
            holder.receiveStatus.text = mContext.getString(R.string.sent)
        }


        holder.productName.text = model.product_name
        Glide.with(mContext).load(model.product_photo).into(holder.productImage)

        holder.itemView.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(mContext, ChatActivity::class.java)
                intent.putExtra("userId", model.userId)
                intent.putExtra("productId", model.productId)
                mContext.startActivity(intent)
            }
        })

    }


}