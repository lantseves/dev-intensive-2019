package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository

class ArchiveViewModel : ViewModel() {
    private val chatRepository = ChatRepository
    private val archiveChat = Transformations.map(chatRepository.loadChats()) {
        return@map it.filter { chat -> chat.isArchived }
                .map { chat ->  chat.toChatItem() }
                .sortedBy { chat -> chat.id }
    }

    fun getArchiveChat() : LiveData<List<ChatItem>> {
        return archiveChat
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
}