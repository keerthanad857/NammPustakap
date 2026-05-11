package com.example.nammapustaka.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete

@Dao
interface ReviewDao {

    // ✅ Insert review
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review)

    // ✅ Get all reviews
    @Query("SELECT * FROM reviews ORDER BY id DESC")
    suspend fun getAllReviews(): List<Review>

    // ✅ Get reviews for a specific book
    @Query("SELECT * FROM reviews WHERE bookId = :bookId ORDER BY id DESC")
    suspend fun getReviewsByBookId(bookId: Int): List<Review>

    // ✅ Get reviews by student name
    @Query("SELECT * FROM reviews WHERE studentName = :studentName")
    suspend fun getReviewsByStudent(studentName: String): List<Review>

    // ✅ Get average rating for a book
    @Query("SELECT AVG(rating) FROM reviews WHERE bookId = :bookId")
    suspend fun getAverageRating(bookId: Int): Float?

    // ✅ Delete a review
    @Delete
    suspend fun deleteReview(review: Review)

    // ✅ Delete review by id
    @Query("DELETE FROM reviews WHERE id = :reviewId")
    suspend fun deleteReviewById(reviewId: Int)
}