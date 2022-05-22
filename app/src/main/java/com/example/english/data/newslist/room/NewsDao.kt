package com.example.english.data.newslist.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Query("SELECT * from News ORDER BY id")
    fun getNewsList(): Flow<List<News>>

    @Query("SELECT COUNT(*) from News")
    fun getSize(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNews(news: News): Long

    @Delete
    suspend fun deleteNews(mews: News)
}