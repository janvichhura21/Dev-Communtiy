package com.example.devcommunity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.devcommunity.Chat.ChatFragment
import com.example.devcommunity.Chat.GroupFragment
import com.example.devcommunity.Chat.ViewPagerAdapter
import com.example.devcommunity.community.CommunityViewModel
import com.example.devcommunity.community.CommunityAdapter
import com.example.devcommunity.community.GroupActivity
import com.example.devcommunity.community.GroupAdapter
import com.example.devcommunity.databinding.FragmentCommunityBinding
import com.example.devcommunity.model.Group
import com.example.devcommunity.model.User
import com.example.devcommunity.toolBar.ContactFragment
import com.example.devcommunity.toolBar.InviteFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration


class CommunityFragment : Fragment(),GroupAdapter.OnMessageItemClickListener {
    lateinit var firebaseAuth: FirebaseAuth
   private lateinit var userListener: ListenerRegistration
    private val viewModel : CommunityViewModel by viewModels()
    lateinit var binding: FragmentCommunityBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(layoutInflater,R.layout.fragment_community, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUser()
        viewModel.getAllGroups()
        firebaseAuth= FirebaseAuth.getInstance()
       val adapter = activity?.supportFragmentManager?.let { ViewPagerAdapter(it) }
        adapter?.addFragment(GroupFragment(), "Community")
        adapter?.addFragment(ChatFragment(), "Personal")
        adapter?.addFragment(ContactFragment(), "Contact")

        binding.viewPager2.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager2)
        listenForUserOnlineStatus(firebaseAuth.currentUser?.uid!!)
    }


    override fun onMessageItemClick(message: Group) {

        val intent= Intent(context, GroupActivity::class.java)
        intent.putExtra("gN",message.groupName)
        viewModel.resultData.observe(viewLifecycleOwner, Observer {
            it?.forEach {
                intent.putExtra("name", it.name)
            }
        })
        startActivity(intent)
    }


    private fun listenForUserOnlineStatus(userId: String) {
        var db= FirebaseFirestore.getInstance()
        val userRef = db.collection("Users").document(userId)

        userListener = userRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Handle error
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val user = snapshot.toObject(User::class.java)
                if (user != null) {
                    // Update your UI based on the user's online status
                    val isOnline = user.online
                    Log.d("islogin",isOnline.toString())
                    // For example, display an "Online" indicator next to the user's name
                }
            }
        }
    }








}
