package com.example.devcommunity.Chat

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devcommunity.R
import com.example.devcommunity.community.CommunityAdapter
import com.example.devcommunity.community.CommunityViewModel
import com.example.devcommunity.community.GroupActivity
import com.example.devcommunity.community.GroupAdapter
import com.example.devcommunity.community.MyBottomSheetFragment
import com.example.devcommunity.databinding.FragmentGroupBinding
import com.example.devcommunity.model.Group
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference


class GroupFragment : Fragment() ,GroupAdapter.OnMessageItemClickListener{
    lateinit var binding:FragmentGroupBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var db: DatabaseReference
    var groupName:String? = null
    lateinit var myAdapter: CommunityAdapter
    lateinit var groupAdapter: GroupAdapter
    private val viewModel : CommunityViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=
            DataBindingUtil.inflate(layoutInflater,R.layout.fragment_group, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUser()
        viewModel.getAllGroups()
        viewModel.fetchMessages()

        getGrp()
        getAllGrp()
    }

     private fun getAllGrp() {

          viewModel.dataChange.observe(viewLifecycleOwner, Observer {

              if (it != null) {
                 groupAdapter = GroupAdapter(requireContext(),it,this)

                  binding.chatsRecyclerView.adapter=groupAdapter
                  binding.chatsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                  groupAdapter.notifyDataSetChanged()
              }
          })
      }

      private fun getGrp() {
          binding.addGrp.setOnClickListener {
              val bottomSheetFragment = MyBottomSheetFragment()
              bottomSheetFragment.show(activity?.supportFragmentManager!!, bottomSheetFragment.tag)
          }
      }



    override fun onMessageItemClick(message: Group) {

        val intent= Intent(requireActivity(), GroupActivity::class.java)
       intent.putExtra("gN",message.groupName)

        viewModel.resultData.observe(viewLifecycleOwner, Observer {
            it?.forEach {
                intent.putExtra("name", it.name)
            }
        })
        startActivity(intent)
    }
}