package com.notesapp

import com.notesapp.service.NoteService
import kotlinx.coroutines.runBlocking
import java.util.Scanner

fun main() = runBlocking {
    val noteService = NoteService()
    val scanner = Scanner(System.`in`)
    
    println("=== Notes App ===")
    
    while (true) {
        printMenu()
        when (scanner.nextLine().toIntOrNull()) {
            1 -> createNote(noteService, scanner)
            2 -> listNotes(noteService)
            3 -> searchNotes(noteService, scanner)
            4 -> updateNote(noteService, scanner)
            5 -> deleteNote(noteService, scanner)
            6 -> {
                println("Goodbye!")
                return@runBlocking
            }
            else -> println("Invalid option. Please try again.")
        }
    }
}

suspend fun printMenu() {
    println("\n--- Menu ---")
    println("1. Create Note")
    println("2. List Notes")
    println("3. Search Notes")
    println("4. Update Note")
    println("5. Delete Note")
    println("6. Exit")
    print("Choose an option: ")
}

suspend fun createNote(noteService: NoteService, scanner: Scanner) {
    print("Enter note title: ")
    val title = scanner.nextLine()
    
    print("Enter note content: ")
    val content = scanner.nextLine()
    
    print("Enter tags (comma separated): ")
    val tags = scanner.nextLine().split(",").map { it.trim() }.filter { it.isNotBlank() }
    
    val note = noteService.createNote(title, content, tags)
    println("Note created successfully with ID: ${note.id}")
}

suspend fun listNotes(noteService: NoteService) {
    val notes = noteService.getAllNotes()
    
    if (notes.isEmpty()) {
        println("No notes found.")
        return
    }
    
    println("\n--- Your Notes ---")
    notes.forEach { note ->
        println("""
            ID: ${note.id}
            Title: ${note.title}
            Content: ${note.content.take(50)}...
            Tags: ${note.tags.joinToString()}
            Created: ${note.createdAt}
            Updated: ${note.updatedAt}
            --------------------
        """.trimIndent())
    }
}

suspend fun searchNotes(noteService: NoteService, scanner: Scanner) {
    print("Enter search query: ")
    val query = scanner.nextLine()
    
    val notes = noteService.searchNotes(query)
    
    if (notes.isEmpty()) {
        println("No notes found matching '$query'")
        return
    }
    
    println("\n--- Search Results ---")
    notes.forEach { note ->
        println("""
            ID: ${note.id}
            Title: ${note.title}
            Content: ${note.content.take(50)}...
            --------------------
        """.trimIndent())
    }
}

suspend fun updateNote(noteService: NoteService, scanner: Scanner) {
    print("Enter note ID to update: ")
    val id = scanner.nextLine()
    
    print("Enter new title (press enter to keep current): ")
    val title = scanner.nextLine().takeIf { it.isNotBlank() }
    
    print("Enter new content (press enter to keep current): ")
    val content = scanner.nextLine().takeIf { it.isNotBlank() }
    
    val updatedNote = noteService.updateNote(id, title, content)
    
    if (updatedNote != null) {
        println("Note updated successfully!")
    } else {
        println("Note not found!")
    }
}

suspend fun deleteNote(noteService: NoteService, scanner: Scanner) {
    print("Enter note ID to delete: ")
    val id = scanner.nextLine()
    
    val success = noteService.deleteNote(id)
    
    if (success) {
        println("Note deleted successfully!")
    } else {
        println("Note not found!")
    }
}