package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.models.data.ChatType
import ru.skillbranch.devintensive.repositories.ChatRepository

class MainViewModel: ViewModel() {
    private val query = mutableLiveData("")
    private val chatRepository = ChatRepository
    private val chats = Transformations.map(chatRepository.loadChats()) { chats ->
        val result = chats.filter { !it.isArchived }
                .map { it.toChatItem() }
                .sortedBy { it.id.toInt() }.toMutableList()

        val archive = chats.filter { it.isArchived }
                .sortedBy { it.lastMessageDate() }

        if (archive.isNotEmpty()) {
            var counter = 0
            for(chat in archive) {
                counter += chat.unreadableMessageCount()

            }
            result.add(0 , ChatItem( "archive" ,
                    "" ,
                    "" ,
                    "" ,
                    archive.last().lastMessageShort().first ,
                    counter ,
                    archive.last().lastMessageDate()?.shortFormat(),
                    false,
                    ChatType.ARCHIVE ,
                    archive.last().lastMessageShort().second))
        }

        return@map result
    }

    private val archiveChats = Transformations.map(chatRepository.loadChats()) { chats ->
        return@map chats.filter { it.isArchived }
                .map { it.toChatItem() }
                .sortedBy { it.id.toInt() }
    }

    fun  getArchiveChatsData() :LiveData<List<ChatItem>> {
        return archiveChats
    }

    fun getChatData() : LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()

        val filterF = {
            val queryStr = query.value!!
            val chatItem = chats.value!!

            result.value = if (queryStr.isEmpty()) chatItem
            else chatItem.filter { it.title.contains(queryStr , true) }
        }


        result.addSource(chats){filterF.invoke()}
        result.addSource(query){filterF.invoke()}
        return result
    }


    fun addToArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))
    }

    fun restoreFromArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }

    fun handleSearchQuery(s: String) {
        query.value = s
    }
}