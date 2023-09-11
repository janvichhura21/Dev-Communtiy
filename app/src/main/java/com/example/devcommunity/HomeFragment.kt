package com.example.devcommunity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devcommunity.dashboard.PostAdapter
import com.example.devcommunity.databinding.FragmentDetailBinding
import com.example.devcommunity.databinding.FragmentHomeBinding
import com.example.devcommunity.login.LoginViewModel
import com.example.devcommunity.model.Post
import com.example.devcommunity.post.PostViewModel

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var postAdapter: PostAdapter
    private val viewModel : PostViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(layoutInflater,R.layout.fragment_home, container, false)
        return binding.root

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postAdapter= PostAdapter(requireContext())
        viewModel.getUser()
        viewModel.fetchPosts()
        getPostData()
    }

    private fun getPostData() {

        viewModel.postData.observe(viewLifecycleOwner, Observer {
            val data: ArrayList<Post> = ArrayList()

                postAdapter.listitem=it



        })
        Handler(Looper.getMainLooper()).postDelayed({
            binding.shimmerViewContainer.visibility=View.GONE

            binding.recyclerView.adapter=postAdapter
            binding.recyclerView.layoutManager=LinearLayoutManager(requireContext())
        },3500)
    }
}