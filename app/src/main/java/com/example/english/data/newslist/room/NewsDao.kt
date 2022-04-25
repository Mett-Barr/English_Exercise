package com.example.english.data.newslist.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Query("SELECT * from News ORDER BY id")
    fun getNewsList(): Flow<List<News>>

    @Query("SELECT COUNT(*) from News")
    fun getSize(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNews(news: News)
}