package com.example.devcommunity.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.sql.Date


data class Post(var id:String="",
                var postId: String = "",
                var username:String="",
                var profilePic:String="",
                var postPic:String="",
                var postTxt:String="",
                var comments: List<Comment> = emptyList(),
                var link:String="",
                var timestampField: Long = 0,

              //  var comment:  ArrayList<String> = arrayListOf()

)
