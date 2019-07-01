package ru.skillbranch.devintensive.models

import java.util.*

class Chat(
    val id:String,
    val members: MutableList<User> = mutableListOf(),
    val messages: MutableList<BaseMessage> = mutableListOf(),
    val date:Date = Date()
) {

}
