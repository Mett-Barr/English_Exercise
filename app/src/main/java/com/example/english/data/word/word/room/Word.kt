package com.example.english.data.word.word.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WordTable")
data class Word(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "english")
    var english: String = "",

    @ColumnInfo(name = "chinese")
    var chinese: String = "",
)

object EmptyWord {
    val obj = Word(-1, "", "")
}