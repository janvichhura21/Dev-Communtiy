package com.example.devcommunity.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.devcommunity.CommentViewModel
import com.example.devcommunity.Network.NetworkConnection
import com.example.devcommunity.R
import com.example.devcommunity.databinding.ActivityCommentBinding
import com.example.devcommunity.databinding.ActivityMainBinding
import com.example.devcommunity.login.LoginViewModel
import com.example.devcommunity.model.Comment
import com.example.devcommunity.model.Post
import com.example.devcommunity.post.PostViewModel
import com.google.firebase.auth.FirebaseAuth

class CommentActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommentBinding
    lateinit var commentAdapter: CommentAdapter
    private val viewModel : PostViewModel by viewModels()
    var itemId:String?=""
    private val commentList: MutableList<Comment> = mutableListOf()
    lateinit var data:ArrayList<Comment>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_comment)
        binding.root

        data= ArrayList()
        commentAdapter=CommentAdapter(this,emptyList())
       setData()
       getData()
        itemId = intent.getStringExtra("itemId")
        viewModel.getUser()
        viewModel.fetchCommentsFromFirebase(itemId!!)
        viewModel.dp.observe(this, Observer {
            Glide.with(this)
                .load(it)
                .into(binding.imageProfile2)


        })
      //  viewModel.fetchComments(itemId!!)

    }


    private fun setData() {
        binding.postBtn.setOnClickListener{


            binding.commentTxt.clearFocus()
            var newComment =binding.commentTxt.text.toString()
            if (newComment.isNotEmpty()) {
                val comment = Comment().apply {
                    commentText = newComment
                    commentId = itemId!!
                    userId = FirebaseAuth.getInstance().currentUser!!.uid
                    timestamp = System.currentTimeMillis()
                    viewModel.dp.observe(this@CommentActivity, Observer {
                        profilePic = it
                    })
                    viewModel.username.observe(this@CommentActivity, Observer {
                        username = it
                    })
                }
                data.add(comment)
                viewModel.updatePostCommentsInDatabase(itemId!!, data)
                binding.commentTxt.text = null
            }
        }
    }

    private fun getData() {

        viewModel.commentValue.observe(this, Observer {

                binding.cmtRv.adapter=commentAdapter
               // commentList.clear()
               commentList.addAll(it)
            commentAdapter.comments = commentList
            commentAdapter.notifyDataSetChanged()
                binding.cmtRv.layoutManager=LinearLayoutManager(this)


        })


    }
}