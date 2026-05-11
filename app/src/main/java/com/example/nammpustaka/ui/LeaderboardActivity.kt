package com.example.nammapustaka.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nammapustaka.R
import com.example.nammapustaka.adapter.LeaderboardAdapter
import com.example.nammapustaka.data.AppDatabase
import com.example.nammapustaka.data.ReadingLog
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var adapter: LeaderboardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        val tvMonth = findViewById<TextView>(R.id.tvMonth)
        val tvTopStudent = findViewById<TextView>(R.id.tvTopStudent)
        val etStudentName = findViewById<EditText>(R.id.etStudentName)
        val etPagesRead = findViewById<EditText>(R.id.etPagesRead)
        val btnAddLog = findViewById<Button>(R.id.btnAddLog)
        val recycler = findViewById<RecyclerView>(R.id.recyclerLeaderboard)

        // Current month
        val currentMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())
        tvMonth.text = "Month: $currentMonth"

        adapter = LeaderboardAdapter(emptyList())
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        val db = AppDatabase.getDatabase(this)

        // Observe leaderboard
        lifecycleScope.launch {
            db.readingLogDao().getLeaderboardForMonth(currentMonth).collect { scores ->
                adapter.updateList(scores)
                if (scores.isNotEmpty()) {
                    tvTopStudent.text = "🥇 Top: ${scores[0].studentName} — ${scores[0].totalPages} pages"
                } else {
                    tvTopStudent.text = "Top Student: -"
                }
            }
        }

        // Add log
        btnAddLog.setOnClickListener {
            val name = etStudentName.text.toString().trim()
            val pages = etPagesRead.text.toString().trim().toIntOrNull()

            if (name.isEmpty() || pages == null) {
                Toast.makeText(this, "Name ಮತ್ತು Pages enter ಮಾಡಿ!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                db.readingLogDao().insertLog(
                    ReadingLog(
                        studentName = name,
                        pagesRead = pages,
                        month = currentMonth
                    )
                )
                runOnUiThread {
                    etStudentName.text.clear()
                    etPagesRead.text.clear()
                    Toast.makeText(this@LeaderboardActivity, "Log added!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}