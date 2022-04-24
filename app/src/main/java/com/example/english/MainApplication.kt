package com.example.english

import android.app.Application
import com.example.english.data.newslist.NewsDatabase

class MainApplication : Application() {

    val database: NewsDatabase by lazy { NewsDatabase.getDatabase(this) }
}