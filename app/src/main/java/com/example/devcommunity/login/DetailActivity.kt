package com.example.devcommunity.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.devcommunity.R
import com.example.devcommunity.model.addFragment

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        addFragment(R.id.frame1,DetailFragment())
    }
}