package com.example.devcommunity.model

import com.google.firebase.Timestamp


data class MessageList(
    val messageId: String = "",
    val sender: String = "",
    val timestamp: Long=0
)
