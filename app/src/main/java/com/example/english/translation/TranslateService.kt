package com.example.english.translation

import android.app.Service
import android.content.Intent
import android.os.IBinder

class TranslateService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val file: String? = intent?.getStringExtra("file")
        file?.also {
            val fileTr = translate(this, it)

        }

        return super.onStartCommand(intent, flags, startId)
    }
}