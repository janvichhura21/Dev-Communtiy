package com.example.devcommunity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.devcommunity.Network.NetworkConnection
import com.example.devcommunity.databinding.ActivityMainBinding
import com.example.devcommunity.databinding.ActivitySplashBinding
import com.example.devcommunity.login.LoginViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var viewModel: LoginViewModel
    var internet:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.root
        viewModel= ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.getUser()
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
        binding.bottommenu.background=null
       // getNetwork()
        val navController= Navigation.findNavController(this,R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(binding.bottommenu,navController)
       setup()
        getData()
    }

    private fun getNetwork() {
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected->

            if(isConnected){
                binding.disconnected.visibility= View.GONE
                binding.txt.visibility=View.GONE

            }
            else{
                binding.disconnected.visibility= View.VISIBLE
                binding.txt.visibility=View.VISIBLE
            }
        })
    }
    private fun getData() {
        viewModel.resultData.observe(this, Observer {
            it.forEach { data->


                Glide.with(this)
                    .load(data.profile)
                    .into(binding.imageProfile2)
            }
        })
    }

    private fun setup() {
        actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.drawerLayout,R.string.name,R.string.name)
        actionBarDrawerToggle.syncState()
        setSupportActionBar(binding.menuId)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}