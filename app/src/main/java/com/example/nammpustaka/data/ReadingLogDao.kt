package com.example.nammapustaka.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: ReadingLog)

    @Query("""
        SELECT studentName, SUM(pagesRead) as totalPages 
        FROM reading_log 
        WHERE month = :month 
        GROUP BY studentName 
        ORDER BY totalPages DESC
    """)
    fun getLeaderboardForMonth(month: String): Flow<List<StudentScore>>
}

data class StudentScore(
    val studentName: String,
    val totalPages: Int
)