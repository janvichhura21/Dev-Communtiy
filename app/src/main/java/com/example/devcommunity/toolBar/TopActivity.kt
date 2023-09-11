package com.example.devcommunity.toolBar

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import androidx.databinding.DataBindingUtil
import com.example.devcommunity.Chat.ChatFragment
import com.example.devcommunity.Chat.GroupFragment
import com.example.devcommunity.Chat.ViewPagerAdapter
import com.example.devcommunity.R
import com.example.devcommunity.community.MyBottomSheetFragment
import com.example.devcommunity.databinding.ActivityTopBinding
import com.example.devcommunity.model.addFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TopActivity : AppCompatActivity() {
    lateinit var binding:ActivityTopBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_top)
        binding.root
       // addFragment(R.id.frameTop,NotificationFragment())
        val adapter = supportFragmentManager?.let { ViewPagerAdapter(it) }
        adapter?.addFragment(NotificationFragment(), "Friend Request")
        adapter?.addFragment(InviteFragment(), "Invite People")
        adapter?.addFragment(AccountFragment(), "Profile")
        binding.viewPager2.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager2)

    }
}