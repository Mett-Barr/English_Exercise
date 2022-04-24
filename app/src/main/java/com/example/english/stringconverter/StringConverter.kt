package com.example.english.stringconverter

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

class StringConverter {

    fun getArticle(string: String): String {


        return string
    }

    fun getParagraphsSize(context: Context, fileName: String): Int {
        var int = 0
        // 讀檔，看還需要路徑還什麼的，確認一下(下一行
        val fileStream = context.openFileInput(fileName, )
        val reader = InputStreamReader(fileStream, "utf-8")
        val bufferReader = BufferedReader(reader)

        var tmp = 0

        while (fileStream.available() != 0 ) {
//            Log.d("!!!", "getParagraphsSize: ${reader.read()}")
        }

        return int
    }


    // 用 InputStreamReader 從頭開始讀
    // 每遇到連續兩個(以上的)換行，存成一段
    fun getParagraphsList(context: Context, fileName: String): List<String> {
        // 段落暫存
        var paragraphs: String = ""

        // 文章存成段落list
        var list: List<String> = listOf("")

//        while ( /** 還未讀取完文章 **/ ) {
//            if
//        }

        return list
    }
}

data class Paragraphs(val string: String, val int: Int) {

}