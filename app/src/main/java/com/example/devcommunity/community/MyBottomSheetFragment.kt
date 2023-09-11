package com.example.devcommunity.community

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devcommunity.CommunityFragment
import com.example.devcommunity.R
import com.example.devcommunity.databinding.ActivityChatBinding
import com.example.devcommunity.model.Group
import com.example.devcommunity.model.MessageList
import com.example.devcommunity.model.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

class MyBottomSheetFragment : BottomSheetDialogFragment(), GroupMkAdapter.ItemClickListener {
    lateinit var db: DatabaseReference
    lateinit var auth: FirebaseAuth
    private val viewModel: CommunityViewModel by viewModels()
    lateinit var myAdapter: GroupMkAdapter
    var data = User()
    lateinit var list: ArrayList<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
        db = FirebaseDatabase.getInstance().getReference()
        auth = FirebaseAuth.getInstance()
        list = ArrayList()
        viewModel.getUser()
        val recyclerViewOptions = view.findViewById<RecyclerView>(R.id.addGrpRv)

        // communityAdapter= CommunityAdapter(requireContext(),dataList)
        viewModel.resultData.observe(viewLifecycleOwner, Observer {

            if (it != null) {
                myAdapter = GroupMkAdapter(requireContext(), it, this)
                recyclerViewOptions.adapter = myAdapter
                recyclerViewOptions.layoutManager = LinearLayoutManager(requireContext())
            }


        })
        return view
    }


    override fun onItemClick(item: User) {
        val btn = requireView().findViewById<Button>(R.id.addBtn)
        val grpName = view?.findViewById<EditText>(R.id.editTextGroupName)
        val editGrpName = grpName?.text.toString()

        list.add(item)
        btn.setOnClickListener {
            createGroup(editGrpName, list)
           dismiss()

        }

    }

    private fun createGroup(groupName: String, item: ArrayList<User>) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserUid != null) {
            val database: DatabaseReference = FirebaseDatabase.getInstance().reference
            val listKey = database.push().key

            val hashMap = hashMapOf<String, Any>(
                groupName to item
            )
            // If the key is not null, set the list values under the generated key in the database
            listKey?.let {
                database.child("Group").child(groupName)
                    .setValue(hashMap)
                    .addOnSuccessListener {

                        // List added successfully
                    }
                    .addOnFailureListener {
                        // Handle list addition failure
                    }
            }
        }


    }



}
