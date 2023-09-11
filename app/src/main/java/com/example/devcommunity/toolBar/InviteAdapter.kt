package com.example.devcommunity.toolBar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.devcommunity.R
import com.example.devcommunity.community.GroupMkAdapter
import com.example.devcommunity.databinding.InviteFrndBinding
import com.example.devcommunity.databinding.ListItemUserBinding
import com.example.devcommunity.model.User
import java.util.Locale

class InviteAdapter(val context: Context,var userlist:List<User>,
                    private val itemClickListener: OnItemClickListener
                       ): RecyclerView.Adapter<InviteAdapter.InviteViewHolder>(),Filterable {

    private var users = userlist
    private var filteredUsers = userlist
    fun updateData(newData: List<User>) {
        userlist = newData
        notifyDataSetChanged() // Notify the adapter of the data change
    }


    private val sharedPreferences = context.getSharedPreferences("invite_states", Context.MODE_PRIVATE)
    class InviteViewHolder(val binding: InviteFrndBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(data: User){
            binding.lisitems=data
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InviteViewHolder {
        return InviteViewHolder(InviteFrndBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: InviteViewHolder, position: Int) {
        val data=userlist[position]
        holder.binding.friendNameTextView.text=data.name
        holder.binding.friendCollageTextView.text=data.collage

       Glide.with(context)
            .load(data.profile)
            .into(holder.binding.dp)
        val userId = data.id
        val isInvited = sharedPreferences.getBoolean(userId, false)

        if (isInvited) {
            // User is already invited
            setInvitedState(holder.binding.inviteButton)
        } else {
            // User is not invited
           setNotInvitedState(holder.binding.inviteButton)
        }

      holder.binding.inviteButton.setOnClickListener {
          val newInvitedState = !isInvited
          sharedPreferences.edit().putBoolean(userId, newInvitedState).apply()

          if (newInvitedState) {
              setInvitedState(it)
              itemClickListener.ItemClick(data)
          } else{
             // setNotInvitedState(it)
          }

      }

    }

    override fun getItemCount(): Int {
        return userlist.size
    }
    interface OnItemClickListener {
        fun ItemClick(item: User)
    }
    private fun setInvitedState(button: View) {
        button.setBackgroundColor(ContextCompat.getColor(button.context, android.R.color.transparent))
        val txt = button.findViewById<TextView>(R.id.inviteButton)
        txt.setTextColor(context.resources.getColor(R.color.black))
        txt.text = "Invited"
    }

    private fun setNotInvitedState(button: View) {
        // Set initial state of the invite button
        //button.setBackgroundResource(android.R.color.transparent) // Set your background drawable
        val txt = button.findViewById<TextView>(R.id.inviteButton)
        val btn = button.findViewById<Button>(R.id.inviteButton)
        btn.setBackgroundColor(context.resources.getColor(R.color.primary_color))
        txt.setTextColor(context.resources.getColor(R.color.white))
        txt.text = "Invite"
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint.toString()
                val filteredList = if (query.isEmpty()) {
                    users
                } else {
                    val filtered =users.filter { user ->
                        user.name.toLowerCase(Locale.getDefault()).contains(query)
                    }
                    Log.d("Filter", "Filtered List: $filtered")


                    filtered
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                filteredUsers = results?.values as? List<User> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }


}