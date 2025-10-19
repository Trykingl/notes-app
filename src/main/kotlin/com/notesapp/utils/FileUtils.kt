package com.notesapp.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.notesapp.models.Note
import java.io.File

object FileUtils {
    private val gson = Gson()
    
    fun writeNotesToFile(file: File, notes: List<Note>) {
        val json = gson.toJson(notes)
        file.writeText(json)
    }
    
    fun readNotesFromFile(file: File): List<Note> {
        return try {
            val json = file.readText()
            if (json.isBlank()) {
                emptyList()
            } else {
                val listType = object : TypeToken<List<Note>>() {}.type
                gson.fromJson<List<Note>>(json, listType) ?: emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}