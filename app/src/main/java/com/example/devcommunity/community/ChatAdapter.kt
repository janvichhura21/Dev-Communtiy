package com.example.devcommunity.community

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.devcommunity.databinding.ListItemMessageReceivedBinding
import com.example.devcommunity.databinding.ListItemMessageSentBinding
import com.example.devcommunity.model.MessageList
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatAdapter(
    val context: Context, val chatlist:List<MessageList>,
    private var senderPic: String?,
    private val receiverPic:String?
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val Receiver_Items = 1
    val SENT_Items = 2
    fun updateDisplayPic(newDisplayPic: String) {
        senderPic = newDisplayPic
        notifyDataSetChanged() // Notify the adapter that data has changed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 2) {
            return SendViewHolder(ListItemMessageSentBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        } else {
            return ReceiverViewHolder(ListItemMessageReceivedBinding.inflate(LayoutInflater.from(parent.context),parent,false))

        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentpostion = chatlist[position]
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentpostion.sender)) {
            return SENT_Items
        } else {
            return Receiver_Items
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val curentposition = chatlist[position]
        if (holder.javaClass == SendViewHolder::class.java) {
            val viewholder = holder as SendViewHolder
            holder.binding.messageText.text= curentposition.messageId
            Glide.with(context)
                .load(senderPic)
                .into(holder.binding.senderImage)

         //   holder.binding.senderImage
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val time = dateFormat.format(Date(curentposition.timestamp))
            holder.binding.timeText.text= time
        } else {
            val viewholder = holder as ReceiverViewHolder
            holder.binding.messageText2.text = curentposition.messageId
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val time = dateFormat.format(Date(curentposition.timestamp))
            Glide.with(context)
                .load(receiverPic)
                .into(holder.binding.receiverImage)
            holder.binding.timeText.text= time
        }
    }

    override fun getItemCount(): Int {
        return chatlist.size
    }

    class SendViewHolder(val binding: ListItemMessageSentBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    class ReceiverViewHolder(val binding: ListItemMessageReceivedBinding) :
        RecyclerView.ViewHolder(binding.root) {


    }

}