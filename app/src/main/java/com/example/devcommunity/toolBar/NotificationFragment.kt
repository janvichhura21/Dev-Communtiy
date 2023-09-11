package com.example.devcommunity.toolBar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devcommunity.R
import com.example.devcommunity.community.CommunityViewModel
import com.example.devcommunity.databinding.FragmentInviteBinding
import com.example.devcommunity.databinding.FragmentNotificationBinding
import com.google.firebase.auth.FirebaseAuth

class NotificationFragment : Fragment() {
    lateinit var binding: FragmentNotificationBinding
    lateinit var friendRequestAdapter: FriendRequestAdapter
    private val viewModel: CommunityViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(layoutInflater,R.layout.fragment_notification, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getInvitedFriends()
        getFriend()


    }



    private fun getFriend() {

        viewModel.invitedFriendsLiveData.observe(viewLifecycleOwner, Observer {
            Log.d("dataUser",it.toString())

            binding.usersRecyclerView.apply {

                friendRequestAdapter= FriendRequestAdapter(requireContext(),it,
                    acceptClickListener = { user ->
                        Toast.makeText(requireContext(), "accepted", Toast.LENGTH_SHORT).show()
                        viewModel.acceptFriendRequest(FirebaseAuth.getInstance().currentUser!!.uid,user,requireContext())

                                          /* Handle accept action */ },
                    declineClickListener = { user -> /* Handle decline action */ })
                adapter=friendRequestAdapter
                layoutManager=LinearLayoutManager(requireContext())
            }
        })



    }
}