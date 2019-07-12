package ru.skillbranch.devintensive.models

import android.graphics.Color
import android.widget.TableRow
import java.io.Serializable
import java.security.KeyStore

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answers: String): Pair<String, Triple<Int, Int, Int>> {

         if (!validationAnswer(answers)) {
            when(question) {
                Question.NAME -> return "Имя должно начинаться с заглавной буквы" to status.color
                Question.PROFESSION-> return "Профессия должна начинаться со строчной буквы" to status.color
                Question.MATERIAL -> return "Материал не должен содержать цифр" to status.color
                Question.BDAY -> return "Год моего рождения должен содержать только цифры" to status.color
                Question.SERIAL -> return "Серийный номер содержит только цифры, и их 7" to status.color
                Question.IDLE -> return "игнорировать валидацию" to status.color //игнорировать валидацию
            }
        }

        return if (question.answers.contains(answers.toLowerCase())) {
            question = question.nextQuestion()
            "Отлично - ты справился\n${question.question}" to status.color
        } else {
            status = status.nextStatus()

            if ( status != Status.NORMAL) {
                "Это неправильный ответ\n${question.question}" to status.color
            } else {
                question = Question.NAME
                "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
            }
        }
    }

    private fun validationAnswer(answers: String , question: Question): Boolean {
        return when(question) {
            Question.NAME -> answers.trim()[0].isUpperCase() //"Имя должно начинаться с заглавной буквы"
            Question.PROFESSION -> answers.trim()[0].isLowerCase() //"Профессия должна начинаться со строчной буквы"
            Question.MATERIAL -> !Regex("[0-9]+").containsMatchIn(answers.trim()) //Материал не должен содержать цифр"
            Question.BDAY -> !Regex("\\D").containsMatchIn(answers.trim()) //"Год моего рождения должен содержать только цифры"
            Question.SERIAL -> answers.trim().matches(Regex("^\\d{7}$")) //"Серийный номер содержит только цифры, и их 7"
            Question.IDLE -> true
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            }
            else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?" , listOf("Бендер", "bender")) {
            override fun nextQuestion(): Question = PROFESSION
        },

        PROFESSION("Назови мою профессию?" , listOf("сгибальщик", "bender")) {
            override fun nextQuestion(): Question = MATERIAL
        },

        MATERIAL("Из чего я сделан?" , listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion(): Question = BDAY
        },

        BDAY("Когда меня создали?" , listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
        },

        SERIAL("Мой серийный номер?" , listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
        },

        IDLE("На этом все, вопросов больше нет" , listOf()) {
            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion(): Question

    }
}
