package ru.skillbranch.devintensive.extensions

fun String.truncate(value: Int = 16): String {
    var result:String = this.trimEnd()
        if (result.length > value) {
            result = result.substring(0, value).trimEnd() + "..."
        }
    return result
}

fun String.stripHtml(): String{
    val htmlRegex = Regex("(<.*?>)|(&[^ а-я]{1,4}?;)")
    val spaceRegex = Regex(" {2,}")
    return this.replace(htmlRegex, "").replace(spaceRegex, " ")
}