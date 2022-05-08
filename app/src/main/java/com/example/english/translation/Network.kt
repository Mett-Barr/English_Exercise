package com.example.english.translation

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.english.data.article.FileOperator
import com.example.english.translation.json.Translations
import com.google.gson.JsonObject
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

const val BASE_URL = "https://translation.googleapis.com/language/translate/v2/"
const val BASIC_SETTING = "?key=AIzaSyBjmgx_l-kWKBO1L2Bci7bCxvKM83BQLgY&source=en&target=zh-TW"
const val TRANSLATE_REQUEST = "&q="

//fun translate(context: Context) {
//    val queue = Volley.newRequestQueue(context)
//
//    val text =
//        "?key=AIzaSyBjmgx_l-kWKBO1L2Bci7bCxvKM83BQLgY&source=en&target=de&q=Hello%20world&q=My%20name%20is%20Jeff"
//
//    Log.d("!!!", "translateParagraph: translate")
//
//
//    val stringRequest = StringRequest(
//        Request.Method.POST, BASE_URL + text,
//        { response ->
//            Log.d("!!!", response)
//            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
//            val adapter: JsonAdapter<Translations> = moshi.adapter(Translations::class.java)
//            val translations = adapter.fromJson(response)
//        },
//        {
//            Log.d("!!!", it.toString())
//        }
//    )
//
//    // Add the request to the RequestQueue.
//    queue.add(stringRequest)
//}


fun translateArticle(fileNum: String, content: List<String>, context: Context) {
    val queue = Volley.newRequestQueue(context)

    var text = BASIC_SETTING
    content.forEach {
        text += TRANSLATE_REQUEST + it
    }

    val stringRequest = StringRequest(
        Request.Method.POST, BASE_URL + text,
        { response ->
            Log.d("!!!", response)
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val adapter: JsonAdapter<Translations> = moshi.adapter(Translations::class.java)
            val translations = adapter.fromJson(response)
            if (translations != null) {
                translations.data.translations.forEach {
                    it.translatedText += "\n"
                }
                FileOperator.addFileByList(fileNum, translations.data.translations, context)
            }
//            Log.d("!!!", translations.toString())
        },
        {
            Log.d("!!!", it.toString())
        }
    )

    queue.add(stringRequest)
}


fun translateParagraph(context: Context, paragraph: String) {
    val queue = Volley.newRequestQueue(context)

    val text = BASIC_SETTING + TRANSLATE_REQUEST + paragraph

    Log.d("!!!", "translateParagraph: translateParagraph")

    val stringRequest = StringRequest(
        Request.Method.POST, BASE_URL + text,
        { response ->
            Log.d("!!!", response)
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val adapter: JsonAdapter<Translations> = moshi.adapter(Translations::class.java)
            val translations = adapter.fromJson(response)
//            Log.d("!!!", translations.toString())
        },
        {
//            Log.d("!!!", it.toString())
        }
    )


    // Add the request to the RequestQueue.
    queue.add(stringRequest)
}


fun translate(context: Context, file: String) {
    val queue = Volley.newRequestQueue(context)

    val text =
        "?key=AIzaSyBjmgx_l-kWKBO1L2Bci7bCxvKM83BQLgY&source=en&target=de&q=Hello%20world&q=My%20name%20is%20Jeff"

    val stringRequest = StringRequest(
        Request.Method.POST, BASE_URL + text,
        { response ->
            Log.d("!!!", response)
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val adapter: JsonAdapter<Translations> = moshi.adapter(Translations::class.java)
            val translations = adapter.fromJson(response)
//            Log.d("!!!", translations.toString())
        },
        {
//            Log.d("!!!", it.toString())
        }
    )

    val obje = JsonObject()

    // Add the request to the RequestQueue.
    queue.add(stringRequest)
}