package com.example.english.translation.test

import com.example.english.translation.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.Request

fun test(): String {

    val client = OkHttpClient()

    val request = Request.Builder().url(BASE_URL).addHeader("Content-Length", "").build()

//    val response =

    client.newCall(request).execute().use { response -> return response.body!!.string() }


}