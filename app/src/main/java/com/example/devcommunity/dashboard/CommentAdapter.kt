package com.example.devcommunity.dashboard



import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.devcommunity.R
import com.example.devcommunity.model.Comment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommentAdapter(private  val context: Context, var comments: List<Comment>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.txt)
        val commentTextView: TextView = itemView.findViewById(R.id.cmtPost)
        val time: TextView = itemView.findViewById(R.id.times)
        val profile: ImageView = itemView.findViewById(R.id.imageProfile3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item, parent, false)
        return CommentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.usernameTextView.text = comment.username
        holder.commentTextView.text = comment.commentText
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val time = dateFormat.format(Date(comment.timestamp))
        Log.d("time",time)
        holder.time.text= time
        Glide.with(context)
            .load(comment.profilePic)
            .into(holder.profile)
    }

    override fun getItemCount(): Int = comments.size
}



