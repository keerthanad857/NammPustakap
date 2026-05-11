package com.example.nammapustaka.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nammapustaka.R
import com.example.nammapustaka.adapter.ReviewAdapter
import com.example.nammapustaka.data.AppDatabase
import com.example.nammapustaka.data.Review
import kotlinx.coroutines.launch
import java.util.Locale

class ReviewCornerActivity : AppCompatActivity() {

    private lateinit var adapter: ReviewAdapter
    private lateinit var tvAvgRating: TextView
    private lateinit var ratingBarAvg: RatingBar
    private var bookId: Int = -1
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_corner)

        bookId = intent.getIntExtra("BOOK_ID", -1)
        db = AppDatabase.getDatabase(this)

        tvAvgRating       = findViewById(R.id.tvAvgRating)
        ratingBarAvg      = findViewById(R.id.ratingBarAvg)
        val etStudentName = findViewById<EditText>(R.id.etReviewStudentName)
        val ratingBarInput= findViewById<RatingBar>(R.id.ratingBarInput)
        val etReviewText  = findViewById<EditText>(R.id.etReviewText)
        val btnSubmit     = findViewById<Button>(R.id.btnSubmitReview)
        val recycler      = findViewById<RecyclerView>(R.id.recyclerReviews)

        adapter = ReviewAdapter(emptyList())
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        // ✅ Initial load
        loadReviews()

        // ✅ Submit
        btnSubmit.setOnClickListener {
            val name       = etStudentName.text.toString().trim()
            val rating     = ratingBarInput.rating
            val reviewText = etReviewText.text.toString().trim()

            if (name.isEmpty() || rating == 0f || reviewText.isEmpty()) {
                Toast.makeText(this, "ಎಲ್ಲಾ fields fill ಮಾಡಿ!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                db.reviewDao().insertReview(
                    Review(
                        bookId      = bookId,
                        studentName = name,
                        rating      = rating,
                        review      = reviewText   // ✅ correct field name
                    )
                )
                loadReviews()

                runOnUiThread {
                    etStudentName.text.clear()
                    ratingBarInput.rating = 0f
                    etReviewText.text.clear()
                    Toast.makeText(
                        this@ReviewCornerActivity,
                        "Review submitted!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // ✅ Separate function — reuse maadabahudu
    private fun loadReviews() {
        lifecycleScope.launch {
            val reviews: List<Review> = db.reviewDao().getReviewsByBookId(bookId)
            val avg: Float            = db.reviewDao().getAverageRating(bookId) ?: 0f

            adapter.updateList(reviews)

            // ✅ Locale explicit, no concatenation
            tvAvgRating.text = if (avg > 0f) {
                String.format(Locale.getDefault(), "Average: %.1f", avg)
            } else {
                "Average: -"
            }
            ratingBarAvg.rating = avg
        }
    }
}