package com.example.english.data.word.word.room

import androidx.room.Database
import androidx.room.RoomDatabase

//@Database(entities = [Word::class], version = 4, exportSchema = true)
@Database(entities = [Word::class], version = 3)
abstract class WordDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
}