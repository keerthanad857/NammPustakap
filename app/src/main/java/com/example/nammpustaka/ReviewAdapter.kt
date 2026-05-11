package com.example.nammapustaka.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nammapustaka.R
import com.example.nammapustaka.data.Review

class ReviewAdapter(private var list: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvStudentName: TextView = view.findViewById(R.id.tvReviewStudentName)
        val ratingBar: RatingBar    = view.findViewById(R.id.ratingBarItem)
        val tvReview: TextView      = view.findViewById(R.id.tvReviewText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvStudentName.text = item.studentName
        holder.ratingBar.rating   = item.rating
        holder.tvReview.text      = item.review   // ✅ review field
    }

    override fun getItemCount() = list.size

    fun updateList(newList: List<Review>) {
        list = newList
        notifyDataSetChanged()
    }
}