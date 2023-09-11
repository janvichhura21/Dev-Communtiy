package com.example.devcommunity.model

data class Comment(
    var commentId: String = "",
    var userId: String = "",
    var username: String = "",
    var commentText: String = "",
    var profilePic:String="",
    var timestamp: Long = 0L
)
