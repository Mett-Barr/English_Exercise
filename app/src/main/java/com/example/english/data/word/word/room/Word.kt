package com.example.english.data.word.word.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WordTable")
data class Word(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "english")
    var english: String = "",

    @ColumnInfo(name = "chinese")
    var chinese: String = "",
)