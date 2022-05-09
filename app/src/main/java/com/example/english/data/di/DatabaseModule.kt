package com.example.english.data.di

import android.content.Context
import androidx.room.Room
import com.example.english.data.newslist.DefaultRepository
import com.example.english.data.newslist.Repository
import com.example.english.data.newslist.room.NewsDao
import com.example.english.data.newslist.room.NewsDatabase
import com.example.english.data.word.word.DefaultWordRepository
import com.example.english.data.word.word.WordRepository
import com.example.english.data.word.word.room.WordDao
import com.example.english.data.word.word.room.WordDatabase
import com.example.english.data.word.wordlist.DefaultWordListRepository
import com.example.english.data.word.wordlist.WordListRepository
import com.example.english.data.word.wordlist.room.WordListDao
import com.example.english.data.word.wordlist.room.WordListDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): NewsDatabase {
        return Room.databaseBuilder(
            appContext,
            NewsDatabase::class.java,
            "news_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(newsDB: NewsDatabase): NewsDao {
        return newsDB.newsDao()
    }

    @Provides
    @Singleton
    fun getRepository(newsDao: NewsDao) = DefaultRepository(newsDao) as Repository





    @Provides
    @Singleton
    fun provideWordDatabase(@ApplicationContext appContext: Context): WordDatabase {
        return Room.databaseBuilder(
            appContext,
            WordDatabase::class.java,
            "word_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWordDao(wordDB: WordDatabase): WordDao {
        return wordDB.wordDao()
    }

    @Provides
    @Singleton
    fun getWordRepository(wordDao: WordDao) = DefaultWordRepository(wordDao) as WordRepository





    @Provides
    @Singleton
    fun provideWordListDatabase(@ApplicationContext appContext: Context): WordListDatabase {
        return Room.databaseBuilder(
            appContext,
            WordListDatabase::class.java,
            "word_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWordListDao(wordListDB: WordListDatabase): WordListDao {
        return wordListDB.wordListDao()
    }

    @Provides
    @Singleton
    fun getWordListRepository(wordListDao: WordListDao) = DefaultWordListRepository(wordListDao) as WordListRepository
}