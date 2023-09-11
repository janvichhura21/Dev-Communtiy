package com.example.devcommunity.community

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devcommunity.R
import com.example.devcommunity.databinding.ActivityGroupBinding
import com.example.devcommunity.login.LoginViewModel
import com.example.devcommunity.model.Group
import com.example.devcommunity.model.GroupChat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GroupActivity : AppCompatActivity() {
    lateinit var binding:ActivityGroupBinding
    lateinit var firebaseAuth:FirebaseAuth
    lateinit var db: DatabaseReference
    lateinit var messageAdapter: MessageAdapter
     var groupName:String?=""
    var senderName:String?=""
    var displayPic:String?=""
    private val viewModel:LoginViewModel by viewModels()
    private var chatMessages = ArrayList<GroupChat>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_group)
        binding.root
        firebaseAuth=FirebaseAuth.getInstance()
        messageAdapter=MessageAdapter(this,chatMessages)
        viewModel.getUser()
        viewModel.getAllGroups()
        viewModel.getUserDetail()
         groupName=intent.getStringExtra("gN")
       binding.titleTextView.text=groupName
        binding.messagesRecyclerView.apply {
            adapter=messageAdapter
            layoutManager= LinearLayoutManager(this@GroupActivity)
        }
        db= FirebaseDatabase.getInstance().reference.child("group_chat").child(groupName!!)
        fetchMessages()
        binding.sendBtn.setOnClickListener {
            val message=binding.editTextMessage.text.toString()
            if (message.isNotEmpty()) {

                if (isUserInGroup(groupName!!)) {
                    sendMessage(message)
                    binding.editTextMessage.text.clear()
                }
            }
        }
    }
    private fun fetchMessages() {
        // Attach a ValueEventListener to the database reference to fetch messages
       db.addValueEventListener(object : ValueEventListener {
           val groups = mutableListOf<Group>()
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = mutableListOf<GroupChat>()
                for (messageSnapshot in snapshot.children) {
                    val groupName = messageSnapshot.key
                    val groupChats = mutableListOf<GroupChat>()
                    val senderName = messageSnapshot.child("senderName").getValue(String::class.java)
                    val text = messageSnapshot.child("text").getValue(String::class.java)
                    val groupNames = messageSnapshot.child("groupName").getValue(String::class.java)
                    val timestamp = messageSnapshot.child("timestamp").getValue(Long::class.java)
                    val profile = messageSnapshot.child("profilePic").getValue(String::class.java)
                    senderName?.let {
                        val message = GroupChat(groupNames!!,firebaseAuth.currentUser!!.uid,it, text ?: "",timestamp!!,profile!!)
                        messages.add(message)
                    }
                    val group = Group(groupName ?: "", HashMap(), groupChats)
                    groups.add(group)
                }

                updateGroups(groups)
                Log.d("msg", messages.toString())
                messageAdapter.messages = messages
                messageAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
            @SuppressLint("SuspiciousIndentation")
            private fun sendMessage(messageText: String) {
        // Get the current user's display name (you can customize this)
        val currentUser = FirebaseAuth.getInstance().currentUser
                viewModel.username1.observe(this, Observer {
                    Log.d("senderName",it)
                    senderName=it
                })
        val timestamp = System.currentTimeMillis()
        val messageId = db.push().key
     /*   val newMessage = GroupChat(
            groupName= groupName!!,
            senderId = currentUser?.uid ?: "",
            senderName = senderName?: "",
            text = messageText,
            timestamp = timestamp

        )*/
                viewModel.dp1
                    .observe(this, Observer {
                        displayPic=it
                    })
                val newMessage = hashMapOf(
                    "groupName" to groupName,
                    "senderId" to currentUser?.uid,
                    "senderName" to senderName,
                    "text" to messageText,
                "timestamp" to timestamp,
                    "profilePic" to displayPic,
                )
        if (messageId != null) {
            db.child(messageId).setValue(newMessage)
        }
    }
    private fun isUserInGroup(groupId: String): Boolean {
        // Your logic to check if the user is in the group goes here
        // For example, you can fetch the user's groups from the database and compare with the groupId

        if (groupId==groupName){
            return true
        }

        return false
        // Return true if the user is in the group, otherwise return false
         // Replace with your actual implementation
    }
    private fun updateGroups(groups: List<Group>) {
        // Update your RecyclerView adapter with the new list of groups
        messageAdapter.updateData(groups)
    }
}