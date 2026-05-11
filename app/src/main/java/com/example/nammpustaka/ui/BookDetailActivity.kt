package com.example.nammapustaka.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.nammapustaka.R
import com.example.nammapustaka.data.AppDatabase
import com.example.nammapustaka.data.GeminiHelper
import com.example.nammapustaka.data.Transaction
import kotlinx.coroutines.launch
import java.time.LocalDate

class BookDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        val bookId = intent.getIntExtra("BOOK_ID", -1)
        val db = AppDatabase.getDatabase(this)

        // ✅ Review Corner
        val btnReviewCorner = findViewById<Button>(R.id.btnReviewCorner)
        btnReviewCorner.setOnClickListener {
            val intent = Intent(this, ReviewCornerActivity::class.java)
            intent.putExtra("BOOK_ID", bookId)
            startActivity(intent)
        }

        // ✅ Borrow
        val btnBorrow = findViewById<Button>(R.id.btnBorrow)
        btnBorrow.setOnClickListener {
            android.util.Log.d("DEBUG", "Borrow clicked! BookId: $bookId")
            lifecycleScope.launch {
                db.transactionDao().insertTransaction(
                    Transaction(
                        bookId = bookId,
                        memberId = 1,
                        date = LocalDate.now().toString(),
                        note = "Borrowed"
                    )
                )
                android.util.Log.d("DEBUG", "Transaction inserted!")
                runOnUiThread {
                    Toast.makeText(
                        this@BookDetailActivity,
                        "Book Borrowed! ✅",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // ✅ Return
        val btnReturn = findViewById<Button>(R.id.btnReturn)
        btnReturn.setOnClickListener {
            android.util.Log.d("DEBUG", "Return clicked! BookId: $bookId")
            lifecycleScope.launch {
                db.transactionDao().insertTransaction(
                    Transaction(
                        bookId = bookId,
                        memberId = 1,
                        date = LocalDate.now().toString(),
                        note = "Returned"
                    )
                )
                android.util.Log.d("DEBUG", "Transaction inserted!")
                runOnUiThread {
                    Toast.makeText(
                        this@BookDetailActivity,
                        "Book Returned! ✅",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        lifecycleScope.launch {
            val book = db.bookDao().getBookById(bookId)

            runOnUiThread {
                if (book != null) {
                    findViewById<TextView>(R.id.tvDetailTitle).text    = book.title
                    findViewById<TextView>(R.id.tvDetailAuthor).text   = "✍️ ${book.author}"
                    findViewById<TextView>(R.id.tvDetailCategory).text = book.category
                    findViewById<TextView>(R.id.tvDetailPages).text    = "📄 ${book.pages} pages"
                    findViewById<TextView>(R.id.tvDetailSummary).text  = "⏳ AI Summary load aagthide..."
                }
            }

            if (book != null) {
                val aiSummary = GeminiHelper.getBookSummary(book.title, book.author)
                runOnUiThread {
                    findViewById<TextView>(R.id.tvDetailSummary).text = aiSummary
                }
            }
        }
    }
}