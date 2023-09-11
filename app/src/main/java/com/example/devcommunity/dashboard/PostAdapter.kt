package com.example.devcommunity.dashboard

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isNotEmpty
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.devcommunity.databinding.PostItemBinding
import com.example.devcommunity.login.DetailActivity
import com.example.devcommunity.model.Post
import com.example.devcommunity.post.WebActivity

class PostAdapter (val context: Context): RecyclerView.Adapter<PostAdapter.HomeViewHolder>() {
    var listitem: List<Post> = listOf()

        set(value) {
            notifyItemRangeRemoved(1, listitem.size)
            field = value
            notifyItemRangeInserted(1, listitem.size)
        }
    class HomeViewHolder(val binding: PostItemBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(data:Post){
            binding.lisitems=data
        }
    }

    private val differCallBack = object : DiffUtil.ItemCallback<Post>(){
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }

    }
    val differ= AsyncListDiffer(this,differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(PostItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val data=listitem[position]

        Glide.with(context)
            .load(data.profilePic)
            .into(holder.binding.imageProfile2)

        if (data.postPic.isEmpty()){
            holder.binding.postPic.visibility=View.GONE
        }else{
            Glide.with(context)
                .load(data.postPic)
                .into(holder.binding.postPic)
        }
        holder.binding.postTxt.maxLines=2
        holder.binding.seemore.setOnClickListener {
            holder.binding.postTxt.maxLines=1000
            holder.binding.seemore.visibility=View.GONE
            holder.binding.seeless.visibility=View.VISIBLE
        }
        holder.binding.seeless.setOnClickListener {
            holder.binding.postTxt.maxLines=2
            holder.binding.seemore.visibility=View.VISIBLE
            holder.binding.seeless.visibility=View.GONE
        }
        holder.binding.like.setOnClickListener {
            holder.binding.liked.visibility=View.VISIBLE
            holder.binding.like.visibility=View.GONE
            holder.binding.likes1.visibility=View.VISIBLE
            holder.binding.likes.visibility=View.GONE
        }
        holder.binding.liked.setOnClickListener {
            holder.binding.like.visibility=View.VISIBLE
            holder.binding.liked.visibility=View.GONE
            holder.binding.likes.visibility=View.VISIBLE
            holder.binding.likes1.visibility=View.GONE
        }
        holder.binding.comment.setOnClickListener {
            val intent= Intent(context, CommentActivity::class.java)
            intent.putExtra("itemId",data.postId)
           // intent.putExtra("url",data.postPic)
         //   intent.putExtra("name",data.name)
           // intent.putExtra("price",data.price)*/
            context.startActivity(intent)
        }

        val textValue=holder.binding.linkView.text
        if (data.link.isNotEmpty()){
            holder.binding.apply {
                linkBtn.visibility=View.VISIBLE
                linkBtn.setOnClickListener {
                    val intent=Intent(context,WebActivity::class.java)
                    intent.putExtra("link",data.link)

                    context.startActivity(intent)
                }
            }
        }
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return listitem.size
    }
}