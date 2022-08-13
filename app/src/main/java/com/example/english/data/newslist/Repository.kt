package com.example.english.data.newslist

import com.example.english.data.newslist.room.News
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun newsList(): Flow<List<News>>

    fun getSize(): Flow<Int>

    fun isEmpty(): Boolean

    suspend fun addNews(news: News): Long

    suspend fun deleteNews(news: News)

    suspend fun updateProgress(news: News)
}