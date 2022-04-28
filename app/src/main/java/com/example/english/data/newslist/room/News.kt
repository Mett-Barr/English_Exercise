package com.example.english.data.newslist.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "News")
data class News(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "news_title")
    var title: String = "No Title",

    @ColumnInfo(name = "news_caption")
    var caption: String = "No caption",

    @ColumnInfo(name = "tag")
    var tag: String = "",
)