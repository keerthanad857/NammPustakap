package com.example.nammapustaka.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.nammapustaka.R
import com.example.nammapustaka.data.AppDatabase
import com.example.nammapustaka.data.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class AddBookActivity : AppCompatActivity() {

    private var fetchedImageUrl: String = ""
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        val etTitle            = findViewById<EditText>(R.id.etTitle)
        val etAuthor           = findViewById<EditText>(R.id.etAuthor)
        val spinnerCategory    = findViewById<Spinner>(R.id.spinnerCategory)
        val etPages            = findViewById<EditText>(R.id.etPages)
        val etSummary          = findViewById<EditText>(R.id.etSummary)
        val btnSave            = findViewById<Button>(R.id.btnSave)
        val ivBookCoverPreview = findViewById<ImageView>(R.id.ivBookCoverPreview)

        val categories = listOf("Story", "Science", "History")
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categories
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = spinnerAdapter

        etTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                if (query.length >= 3) {
                    searchJob?.cancel()
                    searchJob = lifecycleScope.launch {
                        delay(800)
                        fetchBookImage(query, ivBookCoverPreview)
                    }
                }
            }
        })

        btnSave.setOnClickListener {
            val title    = etTitle.text.toString().trim()
            val author   = etAuthor.text.toString().trim()
            val category = spinnerCategory.selectedItem.toString()
            val pages    = etPages.text.toString().trim().toIntOrNull() ?: 0
            val summary  = etSummary.text.toString().trim()

            if (title.isEmpty() || author.isEmpty()) {
                Toast.makeText(this, "Title and Author required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val book = Book(
                title    = title,
                author   = author,
                category = category,
                summary  = summary,
                pages    = pages,
                imageUri = fetchedImageUrl
            )

            lifecycleScope.launch {
                AppDatabase.getDatabase(this@AddBookActivity).bookDao().insertBook(book)
                runOnUiThread {
                    Toast.makeText(this@AddBookActivity, "Book saved!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private suspend fun fetchBookImage(title: String, imageView: ImageView) {
        try {
            val encodedTitle = java.net.URLEncoder.encode(title, "UTF-8")
            val url = "https://www.googleapis.com/books/v1/volumes?q=$encodedTitle&maxResults=1"

            val response = withContext(Dispatchers.IO) {
                URL(url).readText()
            }

            val json = JSONObject(response)
            val items = json.optJSONArray("items")

            if (items != null && items.length() > 0) {
                val volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo")
                val imageLinks = volumeInfo.optJSONObject("imageLinks")
                val thumbnail = imageLinks?.optString("thumbnail") ?: ""

                if (thumbnail.isNotEmpty()) {
                    val httpsUrl = thumbnail.replace("http://", "https://")
                    fetchedImageUrl = httpsUrl

                    withContext(Dispatchers.Main) {
                        Glide.with(this@AddBookActivity)
                            .load(httpsUrl)
                            .placeholder(R.drawable.ic_book_placeholder)
                            .into(imageView)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}