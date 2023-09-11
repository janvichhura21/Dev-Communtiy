package com.example.devcommunity.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.devcommunity.MainActivity
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
        firebaseAuth= FirebaseAuth.getInstance()
        getLogin()
    }
    private fun getLog(email:String,password:String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireContext(), "Unsuccess", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun getLogin() {

        binding.apply {

            Login.setOnClickListener {
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()

                if (email.isEmpty() && password.isEmpty()) {
                    layoutEmail.error = "required"
                    layoutPassword.error = "required"
                } else if (email.isEmpty() && password.isNotEmpty()) {
                    layoutEmail.error = "Please enter your email address"
                    if (password.length < 6) {
                        layoutPassword.setError("Your password must be at least 6 characters in length")
                    }
                } else if (password.isEmpty() && email.isNotEmpty()) {
                    layoutPassword.error = "Please enter password"
                    layoutEmail.error = null
                }



            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (password.length < 6) {
                    layoutPassword.setError("Your password must be at least 6 characters in length")
                }  else {
                    layoutEmail.error = null
                    layoutPassword.error = null
                    getLog(email, password)
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    intent.putExtra("emailid", email)
                    startActivity(intent)
                    //  activity?.finish()
                }

            }



            }

        }
    }

}