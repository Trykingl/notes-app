package com.notesapp

import com.notesapp.service.NoteService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class NoteServiceTest {
    
    private lateinit var noteService: NoteService
    private val testDataDir = "test-data"
    
    @BeforeEach
    fun setUp() {
        noteService = NoteService(testDataDir)
    }
    
    @AfterEach
    fun tearDown() {
        File(testDataDir).deleteRecursively()
    }
    
    @Test
    fun `create note should save note successfully`() = runBlocking {
        val note = noteService.createNote("Test Title", "Test Content", listOf("test"))
        
        assertNotNull(note.id)
        assertEquals("Test Title", note.title)
        assertEquals("Test Content", note.content)
        assertTrue(note.tags.contains("test"))
    }
    
    @Test
    fun `get all notes should return created notes`() = runBlocking {
        noteService.createNote("Title 1", "Content 1")
        noteService.createNote("Title 2", "Content 2")
        
        val notes = noteService.getAllNotes()
        
        assertEquals(2, notes.size)
        assertEquals("Title 1", notes[0].title)
        assertEquals("Title 2", notes[1].title)
    }
    
    @Test
    fun `get note by id should return correct note`() = runBlocking {
        val createdNote = noteService.createNote("Test", "Content")
        val retrievedNote = noteService.getNoteById(createdNote.id)
        
        assertNotNull(retrievedNote)
        assertEquals(createdNote.id, retrievedNote!!.id)
        assertEquals("Test", retrievedNote.title)
    }
    
    @Test
    fun `update note should modify existing note`() = runBlocking {
        val note = noteService.createNote("Old Title", "Old Content")
        val updatedNote = noteService.updateNote(note.id, "New Title", "New Content")
        
        assertNotNull(updatedNote)
        assertEquals("New Title", updatedNote!!.title)
        assertEquals("New Content", updatedNote.content)
        assertNotEquals(note.updatedAt, updatedNote.updatedAt)
    }
    
    @Test
    fun `delete note should remove note`() = runBlocking {
        val note = noteService.createNote("Test", "Content")
        
        val deleteResult = noteService.deleteNote(note.id)
        val retrievedNote = noteService.getNoteById(note.id)
        
        assertTrue(deleteResult)
        assertNull(retrievedNote)
    }
    
    @Test
    fun `search notes should find matching notes`() = runBlocking {
        noteService.createNote("Shopping List", "Milk, Eggs, Bread")
        noteService.createNote("Meeting Notes", "Discuss project timeline")
        
        val results = noteService.searchNotes("shopping")
        
        assertEquals(1, results.size)
        assertEquals("Shopping List", results[0].title)
    }
}