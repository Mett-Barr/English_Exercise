package com.example.english.data.word.wordlist.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wordlist")
data class WordList(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val wordList: String = ""
)
