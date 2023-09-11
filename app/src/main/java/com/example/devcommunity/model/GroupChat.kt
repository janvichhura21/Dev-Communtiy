package com.example.devcommunity.model

data class GroupChat(
    val groupName: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val text: String = "",
    val timestamp: Long = 0L,
    val profilePic: String = "",
)