package com.example.english.data.newslist.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "News")
data class News(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "news_title")
    var title: String = "No Title",

    @ColumnInfo(name = "source")
    var source: String = "",

    @ColumnInfo(name = "tag")
    var tag: String = "",


    @ColumnInfo
    var progress: Int = 0,


    @ColumnInfo(name = "url")
    val url: String = "",


    // date
    @ColumnInfo(name = "date")
    val date: String = "",

)