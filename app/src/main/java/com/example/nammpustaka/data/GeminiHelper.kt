package com.example.nammapustaka.data

import com.google.ai.client.generativeai.GenerativeModel

object GeminiHelper {

    private const val API_KEY = "AIzaSyDIIL5SQ4NNHp7W3azh0ISZEVQEe4xbnCE" // nim key illi haki

    private val model = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = API_KEY
    )

    suspend fun getBookSummary(title: String, author: String): String {
        return try {
            val prompt = "Give a short 3 line summary of the book '$title' by $author"
            val response = model.generateContent(prompt)
            response.text ?: "No summary available"
        } catch (e: Exception) {
            "Summary load aagalilla: ${e.message}"
        }
    }
}