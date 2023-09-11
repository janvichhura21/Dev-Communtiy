package com.example.devcommunity.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.devcommunity.model.Comment
import com.example.devcommunity.model.Group
import com.example.devcommunity.model.Post
import com.example.devcommunity.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import java.text.SimpleDateFormat
import java.util.*

class LoginViewModel:ViewModel(){

    var db= FirebaseFirestore.getInstance()
    var docName=""
    var username=MutableLiveData<String>()
    var timestamp=MutableLiveData<String?>()
    var profile=MutableLiveData<String>()
    val resultData=MutableLiveData<List<User>>()
    var gN = MutableLiveData<String>()
    var username1 = MutableLiveData<String>()
    var dp1 = MutableLiveData<String?>()
    var firestore = FirebaseFirestore.getInstance()
    var dataChange = MutableLiveData<List<Group>>()
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
            "online" to user.online,

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

    fun getProfile(senderId:String){
        db.collection("Users")
            .document(senderId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userId=documentSnapshot.getString("id")
                    if (senderId==userId){
                        val profilePicUrl = documentSnapshot.getString("profile")
                        Log.d("userId",userId.toString())
                      //  dp1.value=profilePicUrl
                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.d("janvi",exception.message.toString())

            }
    }
    fun getUserDetail() {
        firestore.collection("Users")
            .whereEqualTo("id", FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                val data = it.toObjects(User::class.java)
                data.forEach { user ->
                    username1.value = user.name
                    dp1.value = user.profile
                }
                resultData.value = data
            }
    }
    fun getAllGroups() {
        val database: DatabaseReference = FirebaseDatabase.getInstance().reference

        database.child("Group").get()
            .addOnSuccessListener { dataSnapshot ->
                val groups = ArrayList<Group>()

                dataSnapshot.children.forEach { groupSnapshot ->
                    val groupName = groupSnapshot.key.toString()
                    val items =
                        groupSnapshot.getValue(object :
                            GenericTypeIndicator<HashMap<String, ArrayList<User>>>() {})
                    val group = Group(groupName, items ?: hashMapOf())
                    gN.value=groupName
                    groups.add(group)

                }

                val sortedGroups = groups.sortedByDescending { it.groupName }
                dataChange.value = sortedGroups

            }
            .addOnFailureListener {
                // Handle the failure to fetch groups
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
                    profile.value=user.profile

                    Log.d("janviC", profile.toString())
                }
                resultData.value=data
            }
    }

    private fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(date)
    }



}