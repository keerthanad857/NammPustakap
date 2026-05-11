package com.example.nammapustaka.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nammapustaka.R
import com.example.nammapustaka.adapter.TransactionAdapter
import com.example.nammapustaka.data.AppDatabase
import kotlinx.coroutines.launch

class TransactionHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_history)

        val db = AppDatabase.getDatabase(this)
        val recycler = findViewById<RecyclerView>(R.id.recyclerTransactions)
        val tvEmpty = findViewById<TextView>(R.id.tvEmptyTransactions)

        val adapter = TransactionAdapter(emptyList())
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        lifecycleScope.launch {
            db.transactionDao().getAllTransactions().collect { list ->
                if (list.isEmpty()) {
                    tvEmpty.visibility = View.VISIBLE
                    recycler.visibility = View.GONE
                } else {
                    tvEmpty.visibility = View.GONE
                    recycler.visibility = View.VISIBLE
                    adapter.updateList(list)
                }
            }
        }
    }
}