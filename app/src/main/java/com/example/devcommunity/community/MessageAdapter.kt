package com.example.devcommunity.community

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.devcommunity.R
import com.example.devcommunity.model.Group
import com.example.devcommunity.model.GroupChat
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageAdapter(val context: Context,private var _messages: List<GroupChat>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    var messages: List<GroupChat>
        get() = _messages
        set(value) {
            _messages = value
            notifyDataSetChanged()
        }
    private var groups = mutableListOf<Group>()

    // Rest of your adapter code

    fun updateData(newGroups: List<Group>) {
        groups.clear()
        groups.addAll(newGroups)
        notifyDataSetChanged()
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val senderTextView: TextView = itemView.findViewById(R.id.userName)
        val messageTextView: TextView = itemView.findViewById(R.id.messageText2)
        val timeText: TextView = itemView.findViewById(R.id.timeText)
        val image: CircleImageView = itemView.findViewById(R.id.receiverImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_received_grp, parent, false)
        return MessageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.senderTextView.text = message.senderName
        Log.d("senderName",message.senderName)
        holder.messageTextView.text = message.text
        Log.d("MessageName",message.text)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val time = dateFormat.format(Date(message.timestamp))
        Log.d("time",time)
        holder.timeText.text= time
        Glide.with(context)
            .load(message.profilePic)
            .into(holder.image)
    }

    override fun getItemCount(): Int = messages.size
}
