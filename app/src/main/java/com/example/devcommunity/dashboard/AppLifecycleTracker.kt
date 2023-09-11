package com.example.devcommunity.dashboard

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class AppLifecycleTracker : Application.ActivityLifecycleCallbacks {

    private var foregroundActivityCount = 0

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
        // Increment foregroundActivityCount when an activity starts
        foregroundActivityCount++
        // Set user online status to true when the app is in the foreground
        updateUserOnlineStatus(true)
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
        // Decrement foregroundActivityCount when an activity stops
        foregroundActivityCount--
        // If there are no foreground activities, set user online status to false
        if (foregroundActivityCount == 0) {
            updateUserOnlineStatus(false)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    private fun updateUserOnlineStatus(online: Boolean) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val db= FirebaseFirestore.getInstance()
            val userRef = db.collection("Users").document(FirebaseAuth.getInstance().currentUser?.uid!!)

            val data = hashMapOf(
                "online" to online
            )

            userRef.set(data, SetOptions.merge())
                .addOnSuccessListener {
                    // Update successful
                }
                .addOnFailureListener {
                    // Handle failure
                }
        }else {

        }
    }
}
