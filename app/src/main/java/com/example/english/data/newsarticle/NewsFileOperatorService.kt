package com.example.english.data.newsarticle

import android.app.Service
import android.content.Intent
import android.os.IBinder

class NewsFileOperatorService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}