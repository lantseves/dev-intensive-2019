package ru.skillbranch.devintensive.models.data

import androidx.annotation.VisibleForTesting
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.models.ImageMessage
import ru.skillbranch.devintensive.models.TextMessage
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class Chat(
        val id: String,
        val title: String,
        val members: List<User> = listOf(),
        var messages: MutableList<BaseMessage> = mutableListOf(),
        var isArchived: Boolean = false
) {
    companion object {
        fun toChatArchive(archiveChats: List<Chat>) : ChatItem {
            archiveChats.sortedBy { it.lastMessageDate() }

            var counter = 0
            for(chat in archiveChats) {
                counter += chat.unreadableMessageCount()

            }
            return ChatItem( "archiveChats" ,
                    "" ,
                    "" ,
                    "Архив чатов" ,
                    archiveChats.last().lastMessageShort().first ,
                    counter ,
                    archiveChats.last().lastMessageDate()?.shortFormat(),
                    false,
                    ChatType.ARCHIVE ,
                    archiveChats.last().lastMessageShort().second)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun unreadableMessageCount(): Int {
        val msgsIsNotReaded = messages.map { !it.isReaded }
        return msgsIsNotReaded.size
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageDate(): Date? {
        val messagesDates = messages
                .map { it.date }
                .sorted()

        return messagesDates.lastOrNull()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageShort(): Pair<String?, String?> {
        return when (val msg = messages.lastOrNull()) {
            is TextMessage -> msg.text?.trim() to (msg.from.firstName ?: "").trim()
            is ImageMessage-> "user.firstName - отправил фото" to (msg.from.firstName ?: "").trim()
            else -> "Сообщений нет" to "@John_Doe"
        }
    }



    private fun isSingle(): Boolean = members.size == 1

    fun toChatItem(): ChatItem {
        return if (isSingle()) {
            val user = members.first()
            ChatItem(
                    id,
                    user.avatar,
                    Utils.toInitials(user.firstName, user.lastName) ?: "??",
                    "${user.firstName ?: ""} ${user.lastName ?: ""}",
                    lastMessageShort().first,
                    unreadableMessageCount(),
                    lastMessageDate()?.shortFormat(),
                    user.isOnline
            )
        } else {
            ChatItem(
                    id,
                    null,
                    "",
                    title,
                    lastMessageShort().first,
                    unreadableMessageCount(),
                    lastMessageDate()?.shortFormat(),
                    false,
                    ChatType.GROUP,
                    lastMessageShort().second
            )
        }
    }
}

enum class ChatType{
    SINGLE,
    GROUP,
    ARCHIVE
}
