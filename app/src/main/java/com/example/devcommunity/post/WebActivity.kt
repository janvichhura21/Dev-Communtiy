package com.example.devcommunity.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.devcommunity.R

class WebActivity : AppCompatActivity() {
    private val viewModel:PostViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        viewModel.fetchPosts()
        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true // Enable JavaScript support if needed4

        val link=intent?.getStringExtra("link")
        Log.d("link",link.toString())
        webView.loadUrl(link!!)
       /* viewModel.linkValue.observe(this, Observer {
            webView.loadUrl(it)
        })*/

    }
}