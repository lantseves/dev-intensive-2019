package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName: String?):Pair<String? ,String?>{
        //TODO FIX ME!!!
        val parts : List<String>? = fullName?.split(" ")

        val fistName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)

        return fistName to lastName
    }

    fun transliteration(payload: String , divider: String = " "): String {
        //TODO FIX ME!!!
        return " "
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        //TODO FIX ME!!!
        return " "
    }


}