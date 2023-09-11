package com.example.devcommunity.community

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.devcommunity.R
import com.example.devcommunity.model.FriendRequest
import com.example.devcommunity.model.Group
import com.example.devcommunity.model.GroupChat
import com.example.devcommunity.model.Post
import com.example.devcommunity.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class CommunityViewModel: ViewModel() {

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var username = MutableLiveData<String>()
    var dp = MutableLiveData<String>()
    var collages = MutableLiveData<String>()
    var username1 = MutableLiveData<String>()
    var dp1 = MutableLiveData<String>()
    var collages1 = MutableLiveData<String>()
    var fetchMessage = MutableLiveData<String>()
    var gN = MutableLiveData<String>()
    var data = MutableLiveData<User?>()
    val resultData = MutableLiveData<List<User>>()
    var firestore = FirebaseFirestore.getInstance()
    var dataChange = MutableLiveData<List<Group>>()
    private val _invitedUsers = MutableLiveData<List<User>>()
    val postData=MutableLiveData<List<Post>>()
    val acceptedInvitedFriendsLiveData = MutableLiveData<List<User>>()

    val invitedFriendsLiveData = MutableLiveData<List<User>>()
    private val _fetchedMessages = MutableLiveData<List<GroupChat>>()
    val fetchedMessages: LiveData<List<GroupChat>> = _fetchedMessages
     fun fetchMessages() {
       FirebaseDatabase.getInstance().reference.child("group_chat")
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val groups = mutableListOf<Group>()
                val fetchedMessages = mutableListOf<GroupChat>()
                for (groupSnapshot in snapshot.children) {
                    val groupNames = groupSnapshot.key
                    val groupChats = mutableListOf<GroupChat>()

                    for (messageSnapshot in groupSnapshot.children) {
                        val groupName = messageSnapshot.child("groupName").getValue(String::class.java)
                        val senderName = messageSnapshot.child("senderName").getValue(String::class.java)
                        val text = messageSnapshot.child("text").getValue(String::class.java)
                        val timestamp = messageSnapshot.child("timestamp").getValue(Long::class.java)

                        senderName?.let {
                            val message = GroupChat(groupName!!,firebaseAuth.currentUser!!.uid, it,
                                text ?: "", timestamp ?: 0L)
                            groupChats.add(message)
                           // fetchedMessages.add(message)
                        }
                    }

                    val group = Group(groupNames ?: "", HashMap(), groupChats)
                    groups.add(group)

                    fetchMessage.value= groups.toString()

                }

                _fetchedMessages.value = fetchedMessages
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    fun getUser() {
        firestore.collection("Users")
            .get()
            .addOnSuccessListener {

                val data = it.toObjects(User::class.java)
                data.forEach { user ->
                    username.value = user.name
                    dp.value = user.profile
                    collages.value = user.collage
                }
                resultData.value = data
            }
    }

    fun getUserDetail() {
        firestore.collection("Users")
            .whereEqualTo("id", FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                val data = it.toObjects(User::class.java)
                data.forEach { user ->
                    username1.value = user.name
                    dp1.value = user.profile
                    collages1.value = user.collage
                }
                resultData.value = data
            }
    }

    fun getAllGroups() {
        val database: DatabaseReference = FirebaseDatabase.getInstance().reference

        database.child("Group").get()
            .addOnSuccessListener { dataSnapshot ->
                val groups = ArrayList<Group>()

                dataSnapshot.children.forEach { groupSnapshot ->
                    val groupName = groupSnapshot.key.toString()
                    val items =
                        groupSnapshot.getValue(object :
                            GenericTypeIndicator<HashMap<String, ArrayList<User>>>() {})
                    val group = Group(groupName, items ?: hashMapOf())
                    gN.value=groupName
                    groups.add(group)

                }

                val sortedGroups = groups.sortedByDescending { it.groupName }
                dataChange.value = sortedGroups

            }
            .addOnFailureListener {
                // Handle the failure to fetch groups
            }
    }

    fun sendInvitation(senderUid: String, receiverEmail: String, user: User) {
        val invitationsRef = FirebaseDatabase.getInstance().getReference("invitations")
        val invitationKey = invitationsRef.push().key

        invitationKey?.let {
            /* val invitation = FriendRequest(
                id = it,
                senderId = senderUid,
                receiverEmail = receiverEmail,
                status = "pending"
            )*/

            val invitation = hashMapOf(
                "id" to it,
                "senderId" to senderUid,
                "receiverEmail" to receiverEmail,
                "status" to "pending",
                // "comment" to arrayListOf(post.comment)
            )
            invitationsRef.child(it).setValue(invitation)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Invitation sent successfully
                    } else {
                        // Failed to send invitation
                    }
                }
            val newUserList = _invitedUsers.value?.toMutableList() ?: mutableListOf()
            newUserList.add(user) // 'user' is the User object you invited
            _invitedUsers.value = newUserList
        }
    }

    fun getInvitedFriends(): LiveData<List<User>> {
        val invitationsRef = FirebaseDatabase.getInstance().getReference("invitations")

        // Retrieve invited friend requests from Realtime Database
        invitationsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val invitedFriends = mutableListOf<User>()
                for (friendRequestSnapshot in snapshot.children) {
                    val friendRequest = friendRequestSnapshot.getValue(FriendRequest::class.java)
                    if (friendRequest?.status == "pending" && friendRequest.receiverEmail == firebaseAuth.currentUser?.uid) {
                        val receiverEmail = friendRequest.senderId
                        // senderId.value=friendRequest.senderId

                        firestore.collection("Users")
                            .whereEqualTo("id", receiverEmail)
                            .get()
                            .addOnSuccessListener {
                                val data = it.toObjects(User::class.java)
                                Log.d("userId", data.toString())
                                invitedFriends.addAll(data)
                                invitedFriendsLiveData.value = invitedFriends
                            }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }

        })
        return invitedFriendsLiveData
    }
        fun acceptFriendRequest(yourUserId: String, user: User,context: Context) {
            // Update the status of the friend request in the invitations node
            val invitationsRef = FirebaseDatabase.getInstance().getReference("invitations")
            val query = invitationsRef.orderByChild("senderId").equalTo(user.id)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (friendRequestSnapshot in snapshot.children) {
                        val friendRequest =
                            friendRequestSnapshot.getValue(FriendRequest::class.java)

                        if (friendRequest?.status == "pending" && friendRequest.receiverEmail == firebaseAuth.currentUser?.uid) {
                            friendRequestSnapshot.ref.child("status").setValue("accepted")
                            if (friendRequest.status == "accepted") {

                                showNotification("Invitation Accepted", "Your invitation has been accepted.",context)
                                    .also {

                                    }
                                // Show a notification for accepted invitation

                            }

                            // You can also implement any additional logic here,
                            // like updating your own friend list, notifying the user, etc.

                            break  // Assuming you want to update only one friend request
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })

        }
    fun getAcceptedInvitedFriends(yourUserId: String): LiveData<List<User>> {
        val invitationsRef = FirebaseDatabase.getInstance().getReference("invitations")


        invitationsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val acceptedFriendsList = mutableListOf<User>()

                for (friendRequestSnapshot in snapshot.children) {
                    val friendRequest = friendRequestSnapshot.getValue(FriendRequest::class.java)

                    if (friendRequest?.status == "accepted" && friendRequest.receiverEmail == firebaseAuth.currentUser?.uid) {
                        val userId = friendRequest.senderId
                        firestore.collection("Users")
                            .whereEqualTo("id", userId)
                            .get()
                            .addOnSuccessListener {
                                val data = it.toObjects(User::class.java)
                                acceptedFriendsList.addAll(data)
                                acceptedInvitedFriendsLiveData.value = acceptedFriendsList
                            }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })

        return acceptedInvitedFriendsLiveData
    }
    fun fetchPosts() {
        val databaseReference = FirebaseDatabase.getInstance().reference
        val postsReference = databaseReference.child("posts").orderByChild("id").equalTo(firebaseAuth.currentUser?.uid)

        postsReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val postsList = mutableListOf<Post>()

                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(Post::class.java)
                    postsList.add(post!!)
                        postData.value=postsList

                }

                // Update your RecyclerView or UI with the postsList
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    fun showNotification(title: String, message: String, context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = firebaseAuth.currentUser!!.uid
            val channelName = "My Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(context, firebaseAuth.currentUser!!.uid)
            .setSmallIcon(R.drawable.baseline_notifications_24) // Replace with your notification icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationId = 1
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

}