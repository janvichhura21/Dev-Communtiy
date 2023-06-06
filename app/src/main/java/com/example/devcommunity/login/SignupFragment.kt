package com.example.devcommunity.login

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isEmpty
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.devcommunity.R
import com.example.devcommunity.databinding.FragmentSignupBinding
import com.example.devcommunity.model.replaceFragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import java.io.IOException
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class SignupFragment : Fragment() {
    lateinit var binding: FragmentSignupBinding
    lateinit var auth:FirebaseAuth
    var loc:String=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(layoutInflater,R.layout.fragment_signup, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth= FirebaseAuth.getInstance()

        binding.cgToLogin.setOnClickListener {
            requireActivity().replaceFragment(R.id.frame,LoginFragment())
        }
        getLogin()

    }

    private fun getLog(email:String,password:String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
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

            logIn.setOnClickListener {
                val email=edtEmail.text.toString()
                val password=edtPassword.text.toString()
                val confirmPassword=ConfirmPassword.text.toString()
                if (email.isEmpty() && password.isEmpty() && confirmPassword.isEmpty()){
                    layoutEmail.error="required"
                    layoutPassword.error="required"
                    layoutConfirmPassword.error="required"
                }
                 else if (email.isEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                     layoutEmail.error="Please enter your email address"
                    if (password.length < 6){
                        layoutPassword.setError("Your password must be at least 6 characters in length")
                    }
                    else if (password!=confirmPassword){
                        layoutConfirmPassword.setError("Password should be match")
                        layoutEmail.error=null
                        layoutPassword.error=null
                    }
                    else if (password==confirmPassword){
                        layoutPassword.error=null
                        layoutConfirmPassword.error=null
                    }
                 }

                 else if (password.isEmpty() && email.isNotEmpty() && confirmPassword.isNotEmpty()){
                     layoutPassword.error="Please enter password"
                    layoutConfirmPassword.error=null
                    layoutEmail.error=null
                 }

                 else if (confirmPassword.isEmpty()&& password.isNotEmpty() && email.isNotEmpty()){
                     layoutConfirmPassword.error="Please enter confirm password"
                    if (password.length < 6){
                        layoutPassword.setError("Your password must be at least 6 characters in length")
                    }

                    else if (password!=confirmPassword){
                        layoutConfirmPassword.setError("Password should be match")
                        layoutEmail.error=null
                        layoutPassword.error=null
                    }
                    else if (password==confirmPassword){
                        layoutPassword.error=null
                        layoutEmail.error=null
                    }

                 }
                else if(email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                    if (password.length < 6){
                        layoutPassword.setError("Your password must be at least 6 characters in length")
                    }
                    else if (password!=confirmPassword){
                        layoutConfirmPassword.setError("Password should be match")
                        layoutEmail.error=null
                        layoutPassword.error=null
                    }
                    else{
                        layoutEmail.error=null
                        layoutPassword.error=null
                        layoutConfirmPassword.error=null
                        getLog(email, password)
                        val intent=Intent(requireActivity(),DetailActivity::class.java)
                        intent.putExtra("emailid",email)
                        startActivity(intent)
                      //  activity?.finish()
                    }

                }


            }

        }
    }

}