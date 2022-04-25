package com.example.english.data.newslist.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [News::class], version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase(){

    abstract fun newsDao(): NewsDao

//    companion object {
//        @Volatile
//        private var INSTANCE: NewsDatabase? = null
//        fun getDatabase(context: Context): NewsDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    NewsDatabase::class.java,
//                    "news_database"
//                )
//                    .fallbackToDestructiveMigration()
//                    .build()
//                INSTANCE = instance
//                return instance
//            }
//        }
//    }
}