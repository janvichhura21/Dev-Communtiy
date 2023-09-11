package com.example.devcommunity.toolBar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.devcommunity.R
import com.example.devcommunity.community.CommunityViewModel
import com.example.devcommunity.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth


class AccountFragment : Fragment() {
    lateinit var binding:FragmentAccountBinding
    private val viewModel: CommunityViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(layoutInflater,R.layout.fragment_account, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserDetail()
        viewModel.fetchPosts()
        viewModel.getAcceptedInvitedFriends(FirebaseAuth.getInstance().currentUser!!.uid)
        getUserDetails()
    }


    private fun getUserDetails() {
       viewModel.username1.observe(viewLifecycleOwner, Observer {
           binding.userName.text=it
       })
        viewModel.acceptedInvitedFriendsLiveData.observe(viewLifecycleOwner, Observer {
            binding.noOfFollowing.text=it.size.toString()

        })
        viewModel.postData.observe(viewLifecycleOwner, Observer {
            binding.noOfPost.text=it.size.toString()
        })
        viewModel.dp1.observe(viewLifecycleOwner, Observer {
            Glide.with(requireContext())
                .load(it)
                .into(binding.bigProfilePic)
            Glide.with(requireContext())
                .load(it)
                .into(binding.profile)
        })
    }
}