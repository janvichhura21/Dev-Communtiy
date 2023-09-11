package com.example.devcommunity.community

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.devcommunity.R
import com.example.devcommunity.databinding.ListItemUserBinding
import com.example.devcommunity.model.User

class CommunityAdapter(val context: Context, val userlist:List<User>,
                       ): RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder>() {
    class CommunityViewHolder(val binding: ListItemUserBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(data: User){
            binding.lisitems=data
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        return CommunityViewHolder(ListItemUserBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        val data=userlist[position]
        holder.binding.displayNameText.text=data.name
        if (data.online){
            holder.binding.status.text= "online"
            holder.binding.status.setTextColor(ContextCompat.getColor(context, R.color.online_text_color))
            holder.binding.onlineViewer.visibility=View.VISIBLE
        } else
            holder.binding.status.text= "last seen recently"

       Glide.with(context)
            .load(data.profile)
            .into(holder.binding.imageCardView)
       // holder.binding.
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




}