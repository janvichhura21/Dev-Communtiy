package com.example.devcommunity.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.devcommunity.model.Comment
import com.example.devcommunity.model.Post
import com.example.devcommunity.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class LoginViewModel:ViewModel(){

    var db= FirebaseFirestore.getInstance()
    var docName=""
    var username=MutableLiveData<String>()
    val resultData=MutableLiveData<List<User>>()
    val postData=MutableLiveData<List<Post>>()
    val cmtData=MutableLiveData<List<Comment>>()
   fun setData(user: User){
       docName=user.collage
        var db= FirebaseFirestore.getInstance()
        val map= hashMapOf(
            "id" to user.id,
            "name" to user.name,
            "age" to user.age,
            "email" to user.email,
            "location" to user.location,
            "collage" to user.collage,
            "gender" to user.gender,
            "profile" to user.profile,

        )

        db.collection("Users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .set(map, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("janvi","Success")

            }.addOnFailureListener {
                Log.d("janvi",it.message.toString())
            }

    }

    fun getUser(){
        db.collection("Users")
            .whereEqualTo("id",FirebaseAuth.getInstance().currentUser?.uid.toString())
            .get()
            .addOnSuccessListener {
                val data=it.toObjects(User::class.java)
                data.forEach { user ->
                    username.value=user.name
                }
                resultData.value=data
            }
    }

    fun getPostDetail(){
        db.collection("Post")
            .get()
            .addOnSuccessListener {
                val data =it.toObjects(Post::class.java)
                postData.value=data
            }

    }

    fun getComment(){
        db.collection("Post")
            .document("KuGvyJ4gSOrgkiv0ADmi")
            .collection("Comment")
            .get()
            .addOnSuccessListener {
                val data =it.toObjects(Comment::class.java)
                cmtData.value=data
            }

    }
    fun setComment(post: Comment){
        val cmt= hashMapOf(
            "id" to post.id,
            "username" to post.username,
            "commentList" to  post.commentList,
            "profilePic" to post.profilePic,
        )
        db.collection("Post")
            //.whereEqualTo("id",FirebaseAuth.getInstance().currentUser?.uid.toString())
            .document("KuGvyJ4gSOrgkiv0ADmi")
            .collection("Comment")
            .document()
            .set(cmt, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("janvi","Success")

            }.addOnFailureListener {
                Log.d("janvi",it.message.toString())
            }


    }


}