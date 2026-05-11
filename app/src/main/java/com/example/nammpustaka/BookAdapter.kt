package com.example.nammapustaka.adapter

import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nammapustaka.R
import com.example.nammapustaka.data.Book

class BookAdapter(
    private var books: List<Book>,
    private val onBookClick: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivBookCover: ImageView = itemView.findViewById(R.id.ivBookCover)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvAuthor: TextView = itemView.findViewById(R.id.tvAuthor)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.tvTitle.text = book.title
        holder.tvAuthor.text = "✍️ ${book.author}"
        holder.tvCategory.text = book.category

        // Category color
        val color = when (book.category.lowercase()) {
            "story" -> "#E91E63"
            "science" -> "#1D9E75"
            "history" -> "#BA7517"
            else -> "#534AB7"
        }
        holder.tvCategory.setBackgroundColor(Color.parseColor(color))

        // Image
        if (book.imageUri.isNotEmpty()) {
            holder.ivBookCover.setImageURI(Uri.parse(book.imageUri))
        } else {
            holder.ivBookCover.setImageResource(R.drawable.ic_book_placeholder)
        }

        holder.itemView.setOnClickListener { onBookClick(book) }
    }

    override fun getItemCount(): Int = books.size

    fun updateList(newList: List<Book>) {
        books = newList
        notifyDataSetChanged()
    }

    fun updateData(newList: List<Book>) {
        books = newList
        notifyDataSetChanged()
    }
}
