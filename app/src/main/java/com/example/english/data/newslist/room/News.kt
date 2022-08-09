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

    @ColumnInfo(name = "news_caption")
    var caption: String = "No caption",

    @ColumnInfo(name = "tag")
    var tag: String = "",


    // add url and image column
//    @ColumnInfo(name = "tag")
//    val imageFile: String = "",

//    @ColumnInfo(name = "url")
//    val url: String = "",

// progress state
//    @ColumnInfo
//    var progss: Int = 0,

    // date
//@ColumInfo(name = "start")
//val start: String

//@ColumnInfo(name = "end")
//var end = ""
)