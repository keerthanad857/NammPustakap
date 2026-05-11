package com.example.nammapustaka.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nammapustaka.R
import com.example.nammapustaka.data.Transaction

class TransactionAdapter(private var list: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvBookId: TextView = view.findViewById(R.id.tvTransactionBookId)
        val tvMemberId: TextView = view.findViewById(R.id.tvTransactionMemberId)
        val tvDate: TextView = view.findViewById(R.id.tvTransactionDate)
        val tvNote: TextView = view.findViewById(R.id.tvTransactionNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvBookId.text = "📚 Book ID: ${item.bookId}"
        holder.tvMemberId.text = "👤 Member ID: ${item.memberId}"
        holder.tvDate.text = "📅 ${item.date}"
        holder.tvNote.text = if (!item.note.isNullOrEmpty()) "📝 ${item.note}" else ""
    }

    override fun getItemCount() = list.size

    fun updateList(newList: List<Transaction>) {
        list = newList
        notifyDataSetChanged()
    }
}