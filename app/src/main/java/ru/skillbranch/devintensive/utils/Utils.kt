package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName: String?):Pair<String? ,String?>{
        val parts : List<String>? = fullName?.trim()?.split(" ")

        return if (parts != null) {

            val fistName = if (parts.getOrNull(0) != "") parts.getOrNull(0) else null
            val lastName = if (parts.getOrNull(1) != "") parts.getOrNull(1) else null

            fistName to lastName

        } else {
            null to null
        }
    }

    private val alfavit: Map<String, String> = hashMapOf("а" to "a", "б" to "b", "в" to "v", "г" to "g", "д" to "d", "е" to "e", "ё" to "e", "ж" to "zh", "з" to "z", "и" to "i",
        "й" to "i", "к" to "k", "л" to "l", "м" to "m", "н" to "n", "о" to "o", "п" to "p", "р" to "r", "с" to "s", "т" to "t", "у" to "u", "ф" to "f", "х" to "h", "ц" to "c",
        "ч" to "ch", "ш" to "sh", "щ" to "sh'", "ъ" to "", "ы" to "i", "ь" to "", "э" to "e", "ю" to "yu", "я" to "ya",

        "А" to "A", "Б" to "B", "В" to "V", "Г" to "G", "Д" to "D", "Е" to "E", "Ё" to "E", "Ж" to "Zh", "З" to "Z", "И" to "I",
        "Й" to "I", "К" to "K", "Л" to "L", "М" to "M", "Н" to "N", "О" to "O", "П" to "P", "Р" to "R", "С" to "S", "Т" to "T", "У" to "U", "Ф" to "F", "Х" to "H", "Ц" to "C",
        "Ч" to "Ch", "Ш" to "Sh", "Щ" to "Sh'", "Ъ" to "", "Ы" to "I", "Ь" to "", "Э" to "E", "Ю" to "Yu", "Я" to "Ya")

    fun transliteration(payload: String , divider: String = " "): String {
        val parts : List<String> = payload.trim().split(" ")
        var result = ""

        for (str in parts) {
            for (i in 0 until str.length) {
                if (alfavit.containsKey(str[i].toString())) {
                    result += alfavit[str[i].toString()]
                }else {
                    result += str[i].toString()
                }
            }
            result += divider
        }

        result = result.trim(divider[0])

        return result
    }

    fun toInitials(firstName: String?, lastName: String?): String? {

        val firstChar = firstName?.trim()?.getOrNull(0)?.toUpperCase()
        val lastChar = lastName?.trim()?.getOrNull(0)?.toUpperCase()

        return if (firstChar == null && lastChar == null) {
            null
        } else {
            var result: String? = ""

            if (firstChar.toString() != "" && firstChar != null) {
                result += firstChar
            }

            if (lastChar.toString() != "" && lastChar != null) {
                result += lastChar
            }

            if (result == "") {
                result = null
            }

            result
        }
    }

    private val anExcepton : List<String> = listOf("enterprise", "features", "topics", "collections",
            "trending", "events", "marketplace", "pricing", "nonprofit", "customer-stories", "security", "login", "join")

    fun isValidateUrlGithub(s: String):Boolean {
        if (s.isEmpty()) return true

        //Проверка исключений
        val exp = s.replace(Regex("""(https://)?(www.)?github.com/""") , "")

        if (anExcepton.contains(exp)) return false

        return Regex("""(https://)?(www.)?github.com/(\w*[^/])""").containsMatchIn(s)
    }
}
