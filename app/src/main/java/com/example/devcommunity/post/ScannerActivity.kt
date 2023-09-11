package com.example.devcommunity.post

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.devcommunity.MainActivity
import com.example.devcommunity.R
import com.example.devcommunity.databinding.ActivityScannerBinding
import com.example.devcommunity.login.DetailFragment
import com.example.devcommunity.login.LoginViewModel
import com.example.devcommunity.model.Post
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class ScannerActivity : AppCompatActivity() {
    private lateinit var bottomSheetLayout: View
    lateinit var binding: ActivityScannerBinding
    private val viewModel:PostViewModel by viewModels ()
    var uri: Uri? = null
    var linkUri:String?=""
    companion object{
        val IMAGE_REQUEST_CODE=100
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_scanner)
        binding.root
        bottomSheetLayout = findViewById(R.id.bottomSheetLayout)
        binding.attachToLink.setOnClickListener {
            binding.bottom.visibility=View.GONE
           bottomSheetLayout.visibility=View.VISIBLE

        }
        val attachLinkButton: Button = findViewById(R.id.attachLinkButton)
        val textLink: TextView = findViewById(R.id.linkEditText)
        attachLinkButton.setOnClickListener {
            val editText=textLink.text.toString()
            binding.linkText.text= editText
            if (binding.linkText.text.isNotBlank()) {
                // Attach the link to the post or perform desired action
                // For example, update UI with the attached link
                linkUri=binding.linkText.text.toString()
                binding.linkText.visibility=View.VISIBLE
                binding.bottom.visibility=View.VISIBLE
                bottomSheetLayout.visibility = View.GONE
            }
        }
     /*   binding.linkEditText.text=intent.getStringExtra("link")
        if (binding.linkEditText.text!=null){
            binding.linkEditText.visibility=View.VISIBLE
            linkUri=binding.linkEditText.text.toString()
        }*/

        viewModel.getUser()
        getUser()
        setListeners()
        setPost()
    }

    private fun setPost() {
       binding.sendBtn.setOnClickListener {

           val post=Post().apply {

               id=FirebaseAuth.getInstance().currentUser?.uid.toString()
               viewModel.postPic.observe(this@ScannerActivity, Observer { image->
                   if (image!=null){
                       postPic=image
                       Log.d("kat",postPic)
                   }else{
                       Toast.makeText(this@ScannerActivity, "Image is not uploaded", Toast.LENGTH_SHORT).show()
                   }

               })
               viewModel.dp.observe(this@ScannerActivity, Observer {
                   profilePic=it
               })

               link=linkUri!!

               viewModel.username.observe(this@ScannerActivity, Observer {
                   username=it
               })
               postTxt=binding.writePost.text.toString()
               comments= emptyList()
               timestampField=System.currentTimeMillis()
               val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
               val time = dateFormat.format(Date(timestampField))
               Log.d("time",time)

           }


           viewModel.createNewPost(post)
           startActivity(Intent(this,MainActivity::class.java))
           finish()
       }
    }
    private fun getUser() {
        viewModel.dp.observe(this@ScannerActivity, Observer {
            Glide.with(this)
                .load(it)
                .into(binding.profileScanner)
        })
    }
   
    fun setListeners() {
        binding.selectImg.setOnClickListener {
            binding.cardPost.visibility=View.VISIBLE
                picImage()

        }

    }
    private fun picImage() {
        val intent= Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        intent.action= Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, DetailFragment.IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode== DetailFragment.IMAGE_REQUEST_CODE &&resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            uri = data?.data!!
            binding.imagePost.setImageURI(uri)
            viewModel.uploadImageToFirebase(uri!!)

        }
    }
    private fun showPostOptionsBottomSheet() {
        val bottomSheetFragment = PostOptionsBottomSheet()
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }


}