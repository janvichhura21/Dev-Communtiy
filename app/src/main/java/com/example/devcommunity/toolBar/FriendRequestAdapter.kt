package com.example.devcommunity.toolBar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.devcommunity.databinding.ListItemNotificationBinding
import com.example.devcommunity.model.User

class FriendRequestAdapter(
    private val context: Context,
    private val userList: List<User>,
    private val acceptClickListener: (User) -> Unit,
    private val declineClickListener: (User) -> Unit
) : RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemNotificationBinding.inflate(inflater, parent, false)
        return FriendRequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class FriendRequestViewHolder(private val binding: ListItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.displayNameText.text = user.name
            binding.collageNameText.text = user.collage

            Glide.with(context)
                .load(user.profile)
                .into(binding.userProfileImage)

            binding.acceptButton.setOnClickListener { acceptClickListener(user)
            binding.acceptButton.visibility=View.GONE
                binding.declineButton.visibility=View.GONE
                binding.accepted.visibility=View.VISIBLE
            }
            binding.declineButton.setOnClickListener { declineClickListener(user) }
        }
    }
}
