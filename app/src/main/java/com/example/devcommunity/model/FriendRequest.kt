package com.example.devcommunity.model

data class FriendRequest(
    var id: String = "",
    var receiverEmail: String = "",
    var senderId: String = "",
    var status: String = ""
)
