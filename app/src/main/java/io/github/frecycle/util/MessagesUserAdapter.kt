package io.github.frecycle.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.frecycle.R
import io.github.frecycle.models.MessageModel
import io.github.frecycle.models.Product
import io.github.frecycle.models.User

class MessagesUserAdapter : RecyclerView.Adapter<MessagesUserAdapter.MyViewHolder> {
    private var mContext: Context
    private var mMessageModel: List<MessageModel>

    constructor(mContext: Context, mMessageModel: List<MessageModel>){
        this.mContext = mContext
        this.mMessageModel = mMessageModel
    }

    class MyViewHolder : RecyclerView.ViewHolder {
        var profileImage : ImageView
        var username : TextView
        var productImage : ImageView
        var productName : TextView

        constructor(itemView: View) : super(itemView) {
            username = itemView.findViewById(R.id.messagesUserName)
            profileImage = itemView.findViewById(R.id.messagesProfileImage)
            productName = itemView.findViewById(R.id.messagesProductName)
            productImage = itemView.findViewById(R.id.messagesProductImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.messages_user_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mMessageModel.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model: MessageModel = mMessageModel[position]
        holder.username.text = model.user_name
        if (model.profile_photo.equals("None", true)){
            holder.profileImage.setImageResource(R.mipmap.ic_launcher_round)
            //TODO default image
        }else{
            Glide.with(mContext).load(model.profile_photo).into(holder.profileImage)
        }

        holder.productName.text = model.product_name
        Glide.with(mContext).load(model.product_photo).into(holder.productImage)

    }


}