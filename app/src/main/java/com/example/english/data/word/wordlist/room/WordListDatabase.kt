package com.example.english.data.word.wordlist.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.english.data.word.wordlist.WordListConverter

@Database(entities = [WordList::class], version = 3, exportSchema = false)
@TypeConverters(WordListConverter::class)
abstract class WordListDatabase: RoomDatabase() {
    abstract fun wordListDao(): WordListDao
}