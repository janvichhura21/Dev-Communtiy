package com.example.devcommunity.model

data class Post(var id:String="",
                var username:String="",
                var profilePic:String="",
                var postPic:String="",
                var postTxt:String="",
                var comment:ArrayList<String> = arrayListOf()
)
