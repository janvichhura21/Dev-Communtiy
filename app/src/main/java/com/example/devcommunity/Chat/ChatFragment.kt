package com.example.devcommunity.Chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devcommunity.R
import com.example.devcommunity.community.ChatAdapter
import com.example.devcommunity.community.CommunityAdapter
import com.example.devcommunity.community.CommunityViewModel
import com.example.devcommunity.community.GroupMkAdapter
import com.example.devcommunity.databinding.FragmentChatBinding
import com.example.devcommunity.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ChatFragment : Fragment() {
    lateinit var binding:FragmentChatBinding
    lateinit var db: DatabaseReference
    lateinit var auth: FirebaseAuth
    private val viewModel: CommunityViewModel by viewModels()
    lateinit var myAdapter: CommunityAdapter
    var data = User()
    lateinit var list: ArrayList<User>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=
            DataBindingUtil.inflate(layoutInflater,R.layout.fragment_chat, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseDatabase.getInstance().getReference()
        auth = FirebaseAuth.getInstance()
        list = ArrayList()
        viewModel.getUser()


        // communityAdapter= CommunityAdapter(requireContext(),dataList)
        viewModel.resultData.observe(viewLifecycleOwner, Observer {

            if (it != null ) {
                myAdapter = CommunityAdapter(requireContext(), it)
                binding.addGrpRv.adapter = myAdapter
                binding.addGrpRv.layoutManager = LinearLayoutManager(requireContext())
            }


        })
    }

}