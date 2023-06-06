package com.example.devcommunity.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.devcommunity.R
import com.example.devcommunity.databinding.FragmentLoginBinding
import com.example.devcommunity.model.replaceFragment
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    lateinit var firebaseAuth: FirebaseAuth

    lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(layoutInflater,R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cgToIn.setOnClickListener {
            requireActivity().replaceFragment(R.id.frame,SignupFragment())
        }
    }
}