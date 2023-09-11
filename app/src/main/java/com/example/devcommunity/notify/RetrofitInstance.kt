package com.example.devcommunity.notify

import com.example.devcommunity.notify.Constants.Companion.BASE_URL
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

class RetrofitInstance {
    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api by lazy {
            retrofit.create(NotificationAPI::class.java)
        }
    }
}