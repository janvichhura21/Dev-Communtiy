package com.example.devcommunity.toolBar

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devcommunity.R
import com.example.devcommunity.community.CommunityAdapter
import com.example.devcommunity.community.CommunityViewModel
import com.example.devcommunity.databinding.FragmentCommunityBinding
import com.example.devcommunity.databinding.FragmentInviteBinding
import com.example.devcommunity.model.FriendRequest
import com.example.devcommunity.model.User
import com.google.firebase.auth.FirebaseAuth

class InviteFragment : Fragment(),InviteAdapter.OnItemClickListener{
    lateinit var binding: FragmentInviteBinding
    lateinit var myAdapter: InviteAdapter
    private val viewModel: CommunityViewModel by viewModels()
    lateinit var arrayList: ArrayList<User>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(layoutInflater,R.layout.fragment_invite, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrayList= ArrayList()
        viewModel.getUser()
        getInviteFriends()

    }

    private fun getInviteFriends() {
        viewModel.resultData.observe(viewLifecycleOwner, Observer { userList->
            if (userList != null ) {
                arrayList.addAll(userList)
                myAdapter = InviteAdapter(requireContext(), userList,this)

                binding.inviteRv.adapter = myAdapter
                binding.inviteRv.layoutManager = LinearLayoutManager(requireContext())

                val collegeNames = userList.map { it.collage }.distinct()

                val spinner = binding.spinner
                val spinnerAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item)
                spinnerAdapter.add("Type")
                spinnerAdapter.addAll(collegeNames)

                spinner.adapter = spinnerAdapter

                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val selectedItem = parent?.getItemAtPosition(position).toString()
                        when (selectedItem) {
                            "Type" -> {
                                myAdapter.updateData(userList)
                            }
                            else -> {
                                val selectedCollege = spinner.selectedItem.toString()
                                val filteredUsers = userList.filter { it.collage == selectedCollege
                                }
                                Log.d("collage",filteredUsers.toString())

                                myAdapter.updateData(filteredUsers)
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Handle nothing selected case if needed
                    }
                }


            }

            val editText = binding.searchView
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val searchQuery = s.toString().trim()
                    val filteredList = userList.filter { item ->
                        item.name.contains(searchQuery, ignoreCase = true)
                        // Replace 'item.name' with the field you want to search in
                    }
                    myAdapter.updateData(filteredList)
                   // myAdapter.filter.filter(s.toString())
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        })
    }



    override fun ItemClick(item: User) {
        viewModel.sendInvitation(FirebaseAuth.getInstance().currentUser!!.uid,item.id,item)
    }


}