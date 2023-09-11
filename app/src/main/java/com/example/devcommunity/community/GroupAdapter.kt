package com.example.devcommunity.community

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.devcommunity.R
import com.example.devcommunity.model.Group
import com.example.devcommunity.model.GroupChat
import com.example.devcommunity.model.User
import kotlin.random.Random

class GroupAdapter(
    val context: Context, var groups: List<Group>,
    private val onMessageItemClickListener: OnMessageItemClickListener
) : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {



    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewGroupName: TextView = itemView.findViewById(R.id.displayNameText)
        val bg: CardView = itemView.findViewById(R.id.imageCardView)
        val txt: TextView = itemView.findViewById(R.id.first_letter)
        val statusTextView: TextView = itemView.findViewById(R.id.last_message)
        // Add other views for displaying User data here

        fun bind(group: Group) {

            textViewGroupName.text = group.groupName
            group.items.forEach {
                it.value.forEach {

                }
            }
            val lastMessage: GroupChat? = group.groupChats.maxByOrNull { chat -> chat.timestamp }
            lastMessage?.let {
                statusTextView.text = it.text
                Log.d("GroupAdapter", "Last Message: ${it.text}, Timestamp: ${it.timestamp}")
            }

            // Bind other views with user data here
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return GroupViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groups[position]

        val backgroundColor = Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
       // holder.bg.setBackgroundColor(backgroundColor)
        holder.bg.setCardBackgroundColor(backgroundColor)
        holder.txt.text=group.groupName.substring(0, 1).toUpperCase()


        val lastMessage: GroupChat? = group.groupChats.maxByOrNull { chat -> chat.timestamp }
        lastMessage?.let {

            holder.statusTextView.text = it.text
        } ?: run {
            holder.statusTextView.text = "No messages yet"
        }


        holder.bind(group)
        holder.itemView.setOnClickListener {
            val intent= Intent(context, GroupActivity::class.java)
             intent.putExtra("gN",group.groupName)

            /* viewModel.resultData.observe(viewLifecycleOwner, Observer {
                 it?.forEach {
                     intent.putExtra("name", it.name)
                 }
             })*/
            context.startActivity(intent)
         //  onMessageItemClickListener.onMessageItemClick(group)
        }

    }

    override fun getItemCount(): Int {
        return groups.size
    }
    interface OnMessageItemClickListener {


        fun onMessageItemClick(message: Group)
    }

}
