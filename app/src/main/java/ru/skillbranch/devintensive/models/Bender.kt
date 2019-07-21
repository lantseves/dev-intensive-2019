package ru.skillbranch.devintensive.models

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

         if (!question.validationAnswer(answers)) {
            when(question) {
                Question.NAME -> return "Имя должно начинаться с заглавной буквы\n${question.question}" to status.color
                Question.PROFESSION-> return "Профессия должна начинаться со строчной буквы\n" +
                        question.question to status.color
                Question.MATERIAL -> return "Материал не должен содержать цифр\n" +
                        question.question to status.color
                Question.BDAY -> return "Год моего рождения должен содержать только цифры\n" +
                        question.question to status.color
                Question.SERIAL -> return "Серийный номер содержит только цифры, и их 7\n" +
                        question.question to status.color
                Question.IDLE -> return "игнорировать валидацию\n" +
                        question.question to status.color //игнорировать валидацию
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

            override fun validationAnswer(answers: String): Boolean { //"Имя должно начинаться с заглавной буквы"
                return if (answers.isNotBlank()) {
                    answers.trim()[0].isUpperCase()
                } else {
                    false
                }
            }

            override fun nextQuestion(): Question = PROFESSION
        },

        PROFESSION("Назови мою профессию?" , listOf("сгибальщик", "bender")) {

            override fun validationAnswer(answers: String): Boolean { //"Профессия должна начинаться со строчной буквы"
                return if(answers.isNotBlank()) {
                    answers.trim()[0].isLowerCase()
                } else {
                    false
                }
            }

            override fun nextQuestion(): Question = MATERIAL
        },

        MATERIAL("Из чего я сделан?" , listOf("металл", "дерево", "metal", "iron", "wood")) {

            override fun validationAnswer(answers: String): Boolean { //Материал не должен содержать цифр"
                return if (answers.isNotBlank()) {
                    !Regex("[0-9]+").containsMatchIn(answers.trim())
                } else {
                    false
                }
            }

            override fun nextQuestion(): Question = BDAY
        },

        BDAY("Когда меня создали?" , listOf("2993")) {

            override fun validationAnswer(answers: String): Boolean {
                return !Regex("\\D").containsMatchIn(answers.trim()) //"Год моего рождения должен содержать только цифры"
            }

            override fun nextQuestion(): Question = SERIAL
        },

        SERIAL("Мой серийный номер?" , listOf("2716057")) {

            override fun validationAnswer(answers: String): Boolean {
                return answers.trim().matches(Regex("^\\d{7}$")) //"Серийный номер содержит только цифры, и их 7"
            }

            override fun nextQuestion(): Question = IDLE
        },

        IDLE("На этом все, вопросов больше нет" , listOf()) {
            override fun validationAnswer(answers: String): Boolean {
                return true
            }

            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion(): Question

        abstract fun validationAnswer(answers: String):Boolean

    }
}
