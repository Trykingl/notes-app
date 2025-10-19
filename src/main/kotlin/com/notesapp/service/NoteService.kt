package com.notesapp.service

import com.notesapp.models.Note
import com.notesapp.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class NoteService(private val dataDirectory: String = "data") {
    
    private val notesFile = File("$dataDirectory/notes.json")
    
    init {
        File(dataDirectory).mkdirs()
    }
    
    suspend fun createNote(title: String, content: String, tags: List<String> = emptyList()): Note {
        return withContext(Dispatchers.IO) {
            val note = Note(title = title, content = content, tags = tags)
            val notes = loadNotes() + note
            saveNotes(notes)
            note
        }
    }
    
    suspend fun getAllNotes(): List<Note> {
        return withContext(Dispatchers.IO) {
            loadNotes()
        }
    }
    
    suspend fun getNoteById(id: String): Note? {
        return withContext(Dispatchers.IO) {
            loadNotes().find { it.id == id }
        }
    }
    
    suspend fun updateNote(id: String, title: String? = null, content: String? = null): Note? {
        return withContext(Dispatchers.IO) {
            val notes = loadNotes()
            val noteIndex = notes.indexOfFirst { it.id == id }
            
            if (noteIndex == -1) return@withContext null
            
            var updatedNote = notes[noteIndex]
            
            title?.let { updatedNote = updatedNote.updateTitle(it) }
            content?.let { updatedNote = updatedNote.updateContent(it) }
            
            val updatedNotes = notes.toMutableList().apply {
                set(noteIndex, updatedNote)
            }
            
            saveNotes(updatedNotes)
            updatedNote
        }
    }
    
    suspend fun deleteNote(id: String): Boolean {
        return withContext(Dispatchers.IO) {
            val notes = loadNotes()
            val filteredNotes = notes.filter { it.id != id }
            
            if (filteredNotes.size == notes.size) {
                return@withContext false
            }
            
            saveNotes(filteredNotes)
            true
        }
    }
    
    suspend fun searchNotes(query: String): List<Note> {
        return withContext(Dispatchers.IO) {
            val notes = loadNotes()
            notes.filter { 
                it.title.contains(query, ignoreCase = true) || 
                it.content.contains(query, ignoreCase = true) ||
                it.tags.any { tag -> tag.contains(query, ignoreCase = true) }
            }
        }
    }
    
    private fun loadNotes(): List<Note> {
        return if (notesFile.exists()) {
            FileUtils.readNotesFromFile(notesFile)
        } else {
            emptyList()
        }
    }
    
    private fun saveNotes(notes: List<Note>) {
        FileUtils.writeNotesToFile(notesFile, notes)
    }
}