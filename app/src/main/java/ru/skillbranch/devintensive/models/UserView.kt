package ru.skillbranch.devintensive.models

class UserView(
    val id: String,
    val fullName: String,
    val nickName: String,
    var avatar: String? = null,
    var status: String = "Offline",
    val initials: String?
) {

    fun printMe() {
        println("""
            $id
            $fullName
            $nickName
            $avatar
            $status
            $initials
        """.trimIndent())
    }
}