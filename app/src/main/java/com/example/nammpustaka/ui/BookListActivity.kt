package com.example.nammapustaka.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nammapustaka.adapter.BookAdapter
import com.example.nammapustaka.data.AppDatabase
import com.example.nammapustaka.databinding.ActivityBookListBinding

class BookListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookListBinding
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getDatabase(this)

        adapter = BookAdapter(emptyList()) { book ->
            val intent = Intent(this, BookDetailActivity::class.java)
            intent.putExtra("BOOK_ID", book.id)
            startActivity(intent)
        }

        // ✅ Grid layout — 2 columns
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = adapter

        db.bookDao().getAllBooks().observe(this) { list ->
            adapter.updateList(list)
            adapter = BookAdapter(emptyList()) { book ->
                val intent = Intent(this, BookDetailActivity::class.java)
                intent.putExtra("BOOK_ID", book.id)  // ← book.id correct ideyaa?
                startActivity(intent)
            }
        }
    }
}