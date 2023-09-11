package com.example.devcommunity.post

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.devcommunity.model.Comment
import com.example.devcommunity.model.Post
import com.example.devcommunity.model.User
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.util.UUID

class PostViewModel : ViewModel() {
    val databaseReference = FirebaseDatabase.getInstance().reference
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val resultData = MutableLiveData<List<User>>()
    var firestore = FirebaseFirestore.getInstance()
    var username = MutableLiveData<String>()
    val commentList: MutableList<Comment> = mutableListOf()
    val commentValue = MutableLiveData<List<Comment>>()
    var dp = MutableLiveData<String>()
    var postPic = MutableLiveData<String>()
    var linkValue = MutableLiveData<String>()
    val postData=MutableLiveData<List<Post>>()
    val data: ArrayList<Comment> = ArrayList()
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

    fun createNewPost(post: Post) {

        val newPostId = databaseReference.child("posts").push().key ?: ""
       // post.id = newPostId
        val postName= hashMapOf(
            "id" to post.id,
            "postId" to newPostId,
            "username" to post.username,
            "postPic" to post.postPic,
            "postTxt" to post.postTxt,
            "profilePic" to post.profilePic,
            "comments" to post.comments,
            "link" to post.link,
            "timestampField" to post.timestampField
            // "comment" to arrayListOf(post.comment)
        )

        databaseReference.child("posts").child(newPostId).setValue(postName)
            .addOnSuccessListener {
                // Post creation successful
            }
            .addOnFailureListener {
                // Handle post creation failure
            }
    }
     fun fetchCommentsFromFirebase(postId: String) {
        val database: DatabaseReference = FirebaseDatabase.getInstance().reference
        val postCommentsRef = database.child("posts").child(postId).child("comments")

        postCommentsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                commentList.clear() // Clear existing comments
                for (commentSnapshot in snapshot.children) {
                    val comment = commentSnapshot.getValue(Comment::class.java)
                    comment?.let {
                        commentList.add(it)

                    }
                }
              commentValue.value=commentList

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    fun updatePostCommentsInDatabase(postId: String, comments: List<Comment>) {
        val database: DatabaseReference = FirebaseDatabase.getInstance().reference
        val postRef = database.child("posts").child(postId)

        postRef.child("comments").setValue(comments)
            .addOnSuccessListener {
                // Comments updated successfully
            }
            .addOnFailureListener {
                // Failed to update comments
            }
    }



    fun uploadImageToFirebase(fileUri: Uri) {
        if (fileUri != null) {
            val fileName = UUID.randomUUID().toString() + ".jpg"

            val refStorage =
                FirebaseStorage.getInstance().reference.child("images/${FirebaseAuth.getInstance().currentUser!!.uid}+/post/$fileName")

            refStorage.putFile(fileUri)
                .addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            val imageUrl = it.toString()
                            postPic.value = imageUrl


                        }
                    })

                ?.addOnFailureListener(OnFailureListener { e ->
                    print(e.message)
                })
        }
    }

     fun fetchPosts() {
        val databaseReference = FirebaseDatabase.getInstance().reference
        val postsReference = databaseReference.child("posts")

        postsReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val postsList = mutableListOf<Post>()

                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(Post::class.java)

                    post?.let {

                        postsList.add(it)

                        val sortedPostsList = postsList.sortedByDescending { it.timestampField }
                            postData.value=sortedPostsList


                        linkValue.value=it.link
                    }
                }

                // Update your RecyclerView or UI with the postsList
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}