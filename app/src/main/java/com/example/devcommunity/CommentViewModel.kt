package com.example.devcommunity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.devcommunity.model.Comment
import com.example.devcommunity.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class CommentViewModel : ViewModel() {
    val databaseReference = FirebaseDatabase.getInstance().reference
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>>
        get() = _comments
    val resultData = MutableLiveData<List<User>>()
    var firestore = FirebaseFirestore.getInstance()
    var username = MutableLiveData<String>()
    var dp = MutableLiveData<String>()
    fun fetchComments(itemId:String) {
        val commentsRef = databaseReference.child("comments").child(itemId)

        commentsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val commentsList = mutableListOf<Comment>()

                for (commentSnapshot in snapshot.children) {
                    val comment = commentSnapshot.getValue(Comment::class.java)
                    comment?.let {
                        commentsList.add(it)
                    }
                }

                _comments.value = commentsList
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
    fun addComment(postId:String,comments: Comment) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null && username != null) {
            val commentId = databaseReference.child("comments").child(postId).push().key
            val commentName= hashMapOf(
                "id" to userId,
                "commentId" to commentId,
                "username" to comments.username,
                "commentText" to comments.commentText,
                "profilePic" to comments.profilePic,
                "timestamp" to System.currentTimeMillis()
                // "comment" to arrayListOf(post.comment)
            )
            val comment = Comment(
                commentId = commentId!!,
                userId  = userId,
                 username = comments.username,
             commentText = comments.commentText,
             profilePic=comments.profilePic,
             timestamp = System.currentTimeMillis()
            )

            if (commentId != null) {
                databaseReference.child("comments").child(commentId).setValue(commentName)
                    .addOnSuccessListener {
                        // Comment added successfully
                    }
                    .addOnFailureListener {
                        // Failed to add comment
                    }
            }
        }
    }
    fun getUser() {
        firestore.collection("Users")
            .get()
            .addOnSuccessListener {

                val data = it.toObjects(User::class.java)
                data.forEach { user ->
                    if(firebaseAuth.currentUser?.uid==user.id){
                        username.value = user.name
                        dp.value=user.profile
                    }

                }
                resultData.value = data
            }
    }
}
