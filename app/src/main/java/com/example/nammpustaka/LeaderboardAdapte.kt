package com.example.nammapustaka.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nammapustaka.R
import com.example.nammapustaka.data.StudentScore

class LeaderboardAdapter(private var list: List<StudentScore>) :
    RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvRank: TextView = view.findViewById(R.id.tvRank)
        val tvName: TextView = view.findViewById(R.id.tvStudentName)
        val tvPages: TextView = view.findViewById(R.id.tvPages)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leaderboard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvRank.text = "#${position + 1}"
        holder.tvName.text = item.studentName
        holder.tvPages.text = "${item.totalPages} pages"
    }

    override fun getItemCount() = list.size

    fun updateList(newList: List<StudentScore>) {
        list = newList
        notifyDataSetChanged()
    }
}