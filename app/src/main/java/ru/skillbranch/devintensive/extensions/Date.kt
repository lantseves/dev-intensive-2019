package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String ="HH:mm:ss dd.MM.yy"):String {
    val  dateFormat = SimpleDateFormat(pattern , Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value:Int, units: TimeUnits = TimeUnits.SECOND):Date {
    var time = this.time

    time += when(units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }

    this.time = time
    return this
}



fun Date.humanizeDiff(date: Date = Date()): String {
    val raznost = (date.time - this.time)
    return if (raznost >= 0) {
        when (raznost) {
            in 0L * SECOND..1L * SECOND -> "только что"
            in 1L * SECOND..45L * SECOND -> "несколько секунд назад"
            in 45L * SECOND..75L * SECOND -> "минуту назад"
            in 75L * SECOND..45L * MINUTE -> "${raznost / MINUTE} ${getNumForm("минуту;минуты;минут" , raznost / MINUTE)} назад"
            in 45L * MINUTE..75L * MINUTE -> "час назад"
            in 75L * MINUTE..22L * HOUR -> "${raznost / HOUR} ${getNumForm("час;часа;часов" , raznost / HOUR)} назад"
            in 22L * HOUR..26L * HOUR -> "день назад"
            in 26L * HOUR..360L * DAY -> "${raznost / DAY} ${getNumForm("день;дня;дней" , raznost / DAY)} назад"
            else -> "более года назад"
        }
    } else {
        val absraznost = abs(raznost)
        when (absraznost) {
            in 0L * SECOND..1L * SECOND -> "только что"
            in 1L * SECOND..45L * SECOND -> "через несколько секунд"
            in 45L * SECOND..75L * SECOND -> "через минуту"
            in 75L * SECOND..45L * MINUTE -> "через ${absraznost / MINUTE} ${getNumForm("минуту;минуты;минут" , absraznost / MINUTE)}"
            in 45L * MINUTE..75L * MINUTE -> "через час"
            in 75L * MINUTE..22L * HOUR -> "через ${absraznost / HOUR} ${getNumForm("час;часа;часов" , absraznost / HOUR)}"
            in 22L * HOUR..26L * HOUR -> "через день"
            in 26L * HOUR..360L * DAY -> "через ${absraznost / DAY} ${getNumForm("день;дня;дней" , absraznost / DAY)}"
            else -> "более чем через год"
        }
    }

}

private fun getNumForm(pluralforms:String, value: Long):String {
    val forms = pluralforms.split(";")
    when (value % 10) {
        1L -> if (value % 100L != 11L)
            return forms[0]
        2L, 3L, 4L -> return if (value % 100 !in 12..14)
            forms[1]
        else forms[2]
    }
    return forms[2]
}

enum class TimeUnits {
    SECOND {
        override fun plural(value: Int): String {
            return "$value " + getNumForm("секунду;секунды;секунд" , value = value.toLong())
        }
    },
    MINUTE {
        override fun plural(value: Int): String {
            return "$value " + getNumForm("минуту;минуты;минут" , value = value.toLong())
        }
    },
    HOUR {
        override fun plural(value: Int): String {
            return "$value " + getNumForm("час;часа;часов" , value = value.toLong())
        }
    },
    DAY {
        override fun plural(value: Int): String {
            return "$value " + getNumForm("день;дня;дней" , value = value.toLong())
        }
    };

    abstract fun plural(value: Int): String

}