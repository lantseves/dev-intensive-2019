package ru.skillbranch.devintensive.models.data

import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class User (
    val id: String,
    var firstName: String?,
    var lastName: String?,
    var avatar: String?,
    var rating: Int = 0,
    var respect: Int = 0,
    var lastVisit: Date? = Date(),
    var isOnline: Boolean = false

    ) {

    constructor(id: String , firstName: String? , lastName: String?): this(id , firstName , lastName , null)

    constructor(id: String) : this(id , "John" , "Doe")

    init {

        println("It's Alive!!!\n" +
                " ${if (lastName === "Doe") "His name id $firstName $lastName" else "And his name is $firstName $lastName"}\n")
    }

    companion object Factory {
        private var lastId : Int = - 1
        fun makeUser(fullName:String?): User {
            lastId++

            val (fistName, lastName) = Utils.parseFullName(fullName)

            return User(id = "$lastId", firstName = fistName, lastName = lastName)
        }
    }

    class Builder {

        var id: String? = null
            private set
        var firstName: String? = null
            private set
        var lastName: String? = null
            private set
        var avatar: String? = null
            private set
        var rating: Int = 0
            private set
        var respect: Int = 0
            private set
        var lastVisit: Date? = Date()
            private set
        var isOnline: Boolean = false


        fun id(id: String) = apply { this.id = id }
        fun firstName(firstName: String?) = apply { this.firstName = firstName }
        fun lastName(lastName: String?) = apply { this.lastName = lastName }
        fun avatar(avatar: String?) = apply { this.avatar = avatar }
        fun rating(rating: Int) = apply { this.rating = rating }
        fun respect(respect: Int) = apply { this.respect = respect }
        fun lastVisit(lastVisit: Date?) = apply { this.lastVisit = lastVisit }
        fun isOnline(isOnline: Boolean) = apply { this.isOnline = isOnline }

        fun build() = id?.let { User(it, firstName, lastName, avatar, rating, respect, lastVisit, isOnline) }
    }
}