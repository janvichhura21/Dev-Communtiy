package com.example.devcommunity.community

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.devcommunity.R
import com.example.devcommunity.databinding.ActivityChatBinding
import com.example.devcommunity.model.GroupChat
import com.example.devcommunity.model.MessageList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    lateinit var firebaseAuth:FirebaseAuth
    lateinit var db: DatabaseReference
    private var displayPic: String ?= null
    private val viewModel : CommunityViewModel by viewModels()
    lateinit var chatAdapter: ChatAdapter
    private var chatMessages = ArrayList<MessageList>()
    var SENT_ROOM:String?=null
    var RECIEVE_ROOM:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_chat)
        binding.root
        chatMessages= ArrayList()
        viewModel.getUserDetail()
        firebaseAuth=FirebaseAuth.getInstance()
        db= FirebaseDatabase.getInstance().getReference()
        val receiverUid=intent.getStringExtra("uid")
        val sentUid=firebaseAuth.currentUser?.uid
        SENT_ROOM=receiverUid+sentUid
        RECIEVE_ROOM=sentUid+receiverUid

        val name=intent.getStringExtra("name")
        val profile=intent.getStringExtra("profile")
        val collage=intent.getStringExtra("collage")
        binding.titleTextView.text=name
        binding.userName.text=name
        binding.collageName.text=collage
        Glide.with(this)
            .load(profile)
            .into(binding.imageProfile2)
        Glide.with(this)
            .load(profile)
            .into(binding.imageProfile)
        binding.messagesRecyclerView.apply {

            Log.d("jaanu", displayPic.toString())
            chatAdapter = ChatAdapter(
                this@ChatActivity,
                chatMessages,
                senderPic = displayPic,
                receiverPic = profile
            )

            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@ChatActivity)
            viewModel.dp1.observe(this@ChatActivity, Observer {
                displayPic=it
                chatAdapter.updateDisplayPic(displayPic!!)
            })
        }
        getMsg()
        binding.messagesRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
        binding.sendBtn.setOnClickListener {
            val messege=binding.editTextMessage.text.toString()
            val timestamp = System.currentTimeMillis()
            val messegeobject=MessageList(messege,sentUid!!,timestamp!!)
            db.child("chats").child(SENT_ROOM!!).child("messege")
                .push().setValue(messegeobject)
                .addOnSuccessListener {
                    db.child("chats").child(RECIEVE_ROOM!!).child("messege")
                        .push().setValue(messegeobject)
                }
            binding.editTextMessage.setText("")
        }


    }


    private fun getMsg() {
        db.child("chats").child(SENT_ROOM!!).child("messege")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    chatMessages.clear()
                    for (positionsnapshots in snapshot.children){
                        val currentuser=positionsnapshots.getValue(MessageList::class.java)
                        chatMessages.add(currentuser!!)
                        binding.messagesRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
                    }
                    chatAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


    }




}