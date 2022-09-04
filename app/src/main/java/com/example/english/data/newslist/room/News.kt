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

//    @ColumnInfo(name = "news_caption")
//    var caption: String = "No caption",

    @ColumnInfo(name = "source")
    var source: String = "",

    @ColumnInfo(name = "tag")
    var tag: String = "",


// progress state
    @ColumnInfo
    var progress: Int = 0,


    // add url and image column
//    @ColumnInfo(name = "tag")
//    val imageFile: String = "",

//    @ColumnInfo(name = "url")
//    val url: String = "",


    // date
    @ColumnInfo(name = "date")
    val date: String = "",

//@ColumnInfo(name = "end")
//var end = ""
)