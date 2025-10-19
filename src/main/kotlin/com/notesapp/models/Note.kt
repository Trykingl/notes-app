package com.notesapp.models

import java.time.LocalDateTime
import java.util.UUID

data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val tags: List<String> = emptyList()
) {
    fun updateContent(newContent: String): Note {
        return this.copy(
            content = newContent,
            updatedAt = LocalDateTime.now()
        )
    }
    
    fun updateTitle(newTitle: String): Note {
        return this.copy(
            title = newTitle,
            updatedAt = LocalDateTime.now()
        )
    }
    
    fun addTag(tag: String): Note {
        return this.copy(
            tags = tags + tag,
            updatedAt = LocalDateTime.now()
        )
    }
}