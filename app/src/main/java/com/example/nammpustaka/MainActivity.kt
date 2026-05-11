package com.example.nammapustaka.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.nammapustaka.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnAddBook).setOnClickListener {
            startActivity(Intent(this, AddBookActivity::class.java))
        }

        findViewById<Button>(R.id.btnBookCatalog).setOnClickListener {
            startActivity(Intent(this, BookCatalogActivity::class.java))
        }

        findViewById<Button>(R.id.btnBookList).setOnClickListener {
            startActivity(Intent(this, BookListActivity::class.java))
        }

        findViewById<Button>(R.id.btnLeaderboard).setOnClickListener {
            startActivity(Intent(this, LeaderboardActivity::class.java))
        }

        findViewById<Button>(R.id.btnReviewCorner).setOnClickListener {
            startActivity(Intent(this, ReviewCornerActivity::class.java))
        }

        findViewById<Button>(R.id.btnBookSearch).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://books.google.com"))
            startActivity(intent)
        }

        // ✅ Transaction History button add maadidhe
        findViewById<Button>(R.id.btnTransactionHistory).setOnClickListener {
            startActivity(Intent(this, TransactionHistoryActivity::class.java))
        }
    }
}