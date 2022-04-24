package com.example.english.data.newslist

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Query("SELECT * from News ORDER BY id")
    fun getNewsList(): Flow<List<News>>
}