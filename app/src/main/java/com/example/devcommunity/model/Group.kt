package com.example.devcommunity.model

data class Group(var groupName:String="",
                 val items: HashMap<String, ArrayList<User>> = hashMapOf(),
                 var groupChats: List<GroupChat> = emptyList()
)