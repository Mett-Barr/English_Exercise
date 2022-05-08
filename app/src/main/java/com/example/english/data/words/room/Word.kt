package com.example.english.data.words.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WordTable")
data class Word(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "word")
    var word: String = "",

    @ColumnInfo(name = "chinese")
    var chinese: String = "",
)