package com.example.devcommunity.community

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.devcommunity.R
import com.example.devcommunity.databinding.ListItemUserBinding
import com.example.devcommunity.databinding.ListUserAgBinding
import com.example.devcommunity.model.MessageList
import com.example.devcommunity.model.User

class GroupMkAdapter(val context: Context, val userlist:List<User>,
                     private val itemClickListener: ItemClickListener
                       ): RecyclerView.Adapter<GroupMkAdapter.CommunityGrpViewHolder>() {
    val items=HashMap<String,Any>()
    val list=ArrayList<String>()
    private val messages = mutableListOf<MessageList>()
    fun addMessage(message: MessageList) {
        messages.add(message)
        notifyDataSetChanged()
    }
    class CommunityGrpViewHolder(val binding: ListUserAgBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(data: User){
            binding.lisitems=data
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityGrpViewHolder {
        return CommunityGrpViewHolder(ListUserAgBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: CommunityGrpViewHolder, position: Int) {
        val data=userlist[position]
        holder.binding.displayNameText.text=data.name
        Glide.with(context)
            .load(data.profile)
            .into(holder.binding.userProfileImage)
        holder.binding.add.setOnClickListener {
            it.setBackgroundColor(
                ContextCompat.getColor(
                    it.context,
                    android.R.color.transparent

                )
            )
            val txt=it.findViewById<TextView>(R.id.add)
            txt.text="added"
            items["add"]=data
            itemClickListener.onItemClick(data)

        }

        holder.itemView.setOnClickListener {
            val intent= Intent(context, ChatActivity::class.java)
            intent.putExtra("name",data.name)
            intent.putExtra("uid",data.id)
            intent.putExtra("profile",data.profile)
            intent.putExtra("collage",data.collage)
            context.startActivity(intent)

        }



    }

    override fun getItemCount(): Int {
        return userlist.size
    }



    interface ItemClickListener {
        fun onItemClick(item: User)
    }
}