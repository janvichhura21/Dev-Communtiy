package com.example.devcommunity.toolBar

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class NotificationViewModel: ViewModel() {
    val senderUserId = FirebaseAuth.getInstance().currentUser?.uid
    private fun sendFriendRequest(recipientUserId: String) {
        val requestId = FirebaseDatabase.getInstance().getReference("users/$recipientUserId/friendRequests").push().key
        val request = mapOf(
            "senderId" to senderUserId,
            "status" to "pending"
        )


        val recipientRef = FirebaseDatabase.getInstance().getReference("users/$recipientUserId/friendRequests/$requestId")
        recipientRef.setValue(request)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("sent","sent succesfully")
                    // Friend request sent successfully

                } else {
                    // Failed to send friend request
                }
            }
    }


}