package com.example.devcommunity.dashboard

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.devcommunity.databinding.CommentItemBinding
import com.example.devcommunity.databinding.PostItemBinding
import com.example.devcommunity.model.Comment
import com.example.devcommunity.model.Post

class CommentAdapter (val context: Context): RecyclerView.Adapter<CommentAdapter.HomeViewHolder>() {
    var listitem: List<Comment> = listOf()
    val cmt=ArrayList<String>()
    var commentitem: List<String> = listOf()
        set(value) {
            notifyItemRangeRemoved(1, listitem.size)
            field = value
            notifyItemRangeInserted(1, listitem.size)
        }

    class HomeViewHolder(val binding: CommentItemBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(data:Comment){
            binding.lisitems=data


        }
    }

    private val differCallBack = object : DiffUtil.ItemCallback<Comment>(){
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }

    }
    val differ= AsyncListDiffer(this,differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(CommentItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }


    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val data=listitem[position]
        Glide.with(context)
            .load(data.profilePic)
            .into(holder.binding.imageProfile3)
        holder.binding.txt.text=data.username
       holder.binding.cmtPost.text=data.commentList
        holder.bind(data)

        /*data.comment.forEach {
            holder.binding.cmtPost.text=it
        }*/


    }

    override fun getItemCount(): Int {
        return listitem.size
    }
}


