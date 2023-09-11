package com.example.devcommunity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.devcommunity.Network.NetworkConnection
import com.example.devcommunity.dashboard.AppLifecycleTracker
import com.example.devcommunity.databinding.ActivityMainBinding
import com.example.devcommunity.login.LoginActivity
import com.example.devcommunity.login.LoginViewModel
import com.example.devcommunity.notify.FirebaseService
import com.example.devcommunity.post.ScannerActivity
import com.example.devcommunity.toolBar.TopActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

const val TOPIC = "/topics/myTopic2"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var viewModel: LoginViewModel
    var internet:Boolean = false
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        registerActivityLifecycleCallbacks(AppLifecycleTracker())
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
        viewModel.getUser()
        binding.scannerFragment.setOnClickListener {
            startActivity(Intent(this,ScannerActivity::class.java))
        }

        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

        binding.navView.setNavigationItemSelectedListener(this)

        binding.navView.removeHeaderView(binding.navView.getHeaderView(0))

        val headerLayout = layoutInflater.inflate(R.layout.header_layout, binding.navView, false)
            ///(headerView as ViewGroup)//.addView(headerLayout)
        binding.navView.addHeaderView(headerLayout)
        val profileImageView: ImageView = headerLayout.findViewById(R.id.setYourPic)
        val userName: TextView = headerLayout.findViewById(R.id.textView4)
        viewModel.profile.observe(this, Observer {
            Glide.with(this)
                .load(it)
                .into(profileImageView)
        })
        viewModel.username.observe(this, Observer {
            userName.text=it
        })
        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            FirebaseService.token = it
            Log.d("token",it)
        }
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        getNotification()
    }

    private fun getNotification() {
        binding.notification.setOnClickListener {
            startActivity(Intent(this,TopActivity::class.java))
        }
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        actionBarDrawerToggle.syncState()
        setSupportActionBar(binding.menuId)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d("Navigation", "Item Clicked: ${item.title}")
        when (item.itemId) {
            R.id.profile -> {
                Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show()
            }

            R.id.rateId -> {
                // Handle Gallery item click
            }

            R.id.helpId -> {
                // Handle Settings item click
            }

            R.id.logoutId -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        // Close the drawer after item click
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true

    }
}