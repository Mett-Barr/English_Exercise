package com.example.english.data.newslist

import com.example.english.data.newslist.room.News
import com.example.english.data.newslist.room.NewsDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count

class DefaultRepository(private val dao: NewsDao) : Repository {
    override fun newsList(): Flow<List<News>> {
        return dao.getNewsList()
    }

    override fun getSize(): Flow<Int> {
        return dao.getSize()
    }

    override fun isEmpty(): Boolean {
        return false
    }

    override suspend fun addNews(news: News): Long = dao.addNews(news)
}