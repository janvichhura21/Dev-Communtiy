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
import com.example.devcommunity.Network.NetworkConnection
import com.example.devcommunity.R
import com.example.devcommunity.databinding.ActivityCommentBinding
import com.example.devcommunity.databinding.ActivityMainBinding
import com.example.devcommunity.login.LoginViewModel
import com.example.devcommunity.model.Comment
import com.example.devcommunity.model.Post
import com.google.firebase.auth.FirebaseAuth

class CommentActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommentBinding
    lateinit var commentAdapter: CommentAdapter
    private val viewModel : LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_comment)
        binding.root
        commentAdapter=CommentAdapter(this)
        //getNetwork()
       viewModel.getPostDetail()
       viewModel.getComment()

       setData()
       getData()

    }
    private fun getNetwork() {
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected->

            if(isConnected){
                Log.d("con","connected")
               // binding.disconnected.visibility= View.GONE
                //binding.txt.visibility= View.GONE

            }
            else{
                Log.d("con","disconnected")
               // binding.disconnected.visibility= View.VISIBLE
              //  binding.txt.visibility= View.VISIBLE
            }
        })
    }

    private fun setData() {
        binding.postBtn.setOnClickListener{
       // binding.commentTxt.text= null
            binding.commentTxt.clearFocus()

            val post=Comment().apply {
                id=FirebaseAuth.getInstance().currentUser?.uid.toString()
                commentList=binding.commentTxt.text.toString()
                viewModel.postData.observe(this@CommentActivity, Observer {
                    it.forEach { data->
                        profilePic=data.profilePic
                        username=data.username
                    }

                })
            }
            viewModel.setComment(post)
            binding.commentTxt.text= null
        }
    }

    private fun getData() {
        viewModel.postData.observe(this, Observer {
            it.forEach { data->
                Glide.with(this)
                    .load(data.profilePic)
                    .into(binding.imageProfile2)
            }
          //  commentAdapter.listitem=it
          //  binding.cmtRv.adapter=commentAdapter
          //  binding.cmtRv.layoutManager=LinearLayoutManager(this)
        })
        viewModel.cmtData.observe(this, Observer {
          /*  it.forEach { data->
                Glide.with(this)
                    .load(data.profilePic)
                    .into(binding.imageProfile2)
            }*/
              commentAdapter.listitem=it
              binding.cmtRv.adapter=commentAdapter
              binding.cmtRv.layoutManager=LinearLayoutManager(this)
        })


    }
}