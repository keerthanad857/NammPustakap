package com.example.nammapustaka.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nammapustaka.R
import com.example.nammapustaka.adapter.BookAdapter
import com.example.nammapustaka.data.AppDatabase
import com.example.nammapustaka.data.Book

class BookCatalogActivity : AppCompatActivity() {

    private lateinit var adapter: BookAdapter
    private var allBooks: List<Book> = emptyList()
    private var selectedCategory: String = "All"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_catalog)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val searchBar = findViewById<EditText>(R.id.searchBar)
        val btnAll = findViewById<TextView>(R.id.btnAll)
        val btnStory = findViewById<TextView>(R.id.btnStory)
        val btnScience = findViewById<TextView>(R.id.btnScience)
        val btnHistory = findViewById<TextView>(R.id.btnHistory)

        // ✅ Grid Layout - 2 columns Digital Shelf
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        adapter = BookAdapter(emptyList()) { book ->
            val intent = Intent(this@BookCatalogActivity, BookDetailActivity::class.java)
            intent.putExtra("BOOK_ID", book.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        val db = AppDatabase.getDatabase(this)
        db.bookDao().getAllBooks().observe(this) { books ->
            allBooks = books
            applyFilter(searchBar.text.toString())
        }

        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                applyFilter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        fun updateCategoryButtons(selected: String) {
            val buttons = mapOf(
                "All" to btnAll,
                "Story" to btnStory,
                "Science" to btnScience,
                "History" to btnHistory
            )
            buttons.forEach { (category, btn) ->
                if (category == selected) {
                    btn.setBackgroundResource(R.drawable.category_selected)
                    btn.setTextColor(ContextCompat.getColor(this, R.color.white))
                } else {
                    btn.setBackgroundResource(R.drawable.category_unselected)
                    btn.setTextColor(ContextCompat.getColor(this, R.color.black))
                }
            }
        }

        btnAll.setOnClickListener {
            selectedCategory = "All"
            updateCategoryButtons("All")
            applyFilter(searchBar.text.toString())
        }
        btnStory.setOnClickListener {
            selectedCategory = "Story"
            updateCategoryButtons("Story")
            applyFilter(searchBar.text.toString())
        }
        btnScience.setOnClickListener {
            selectedCategory = "Science"
            updateCategoryButtons("Science")
            applyFilter(searchBar.text.toString())
        }
        btnHistory.setOnClickListener {
            selectedCategory = "History"
            updateCategoryButtons("History")
            applyFilter(searchBar.text.toString())
        }

        updateCategoryButtons("All")
    }

    private fun applyFilter(query: String) {
        val filtered = allBooks.filter { book ->
            val matchesCategory = selectedCategory == "All" ||
                    book.category.equals(selectedCategory, ignoreCase = true)
            val matchesSearch = query.isEmpty() ||
                    book.title.contains(query, true) ||
                    book.author.contains(query, true) ||
                    book.category.contains(query, true) ||
                    book.summary.contains(query, true)
            matchesCategory && matchesSearch
        }
        adapter.updateList(filtered)
    }
}