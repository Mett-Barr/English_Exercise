package com.example.english.article

import android.content.Context
import android.util.Log
import java.io.File
import java.lang.Exception

const val TEST_FILE_NAME = "test.txt"
const val TEST_ARTICLE = "qwe\nasd\n\nzxc\nwer\n\nsdf\nxcv"

fun fileExist(context: Context, fileName: String): Boolean =
    File(context.filesDir.path + "/" + fileName).exists()

fun fileExistTest(context: Context): Boolean =
    File(context.filesDir.path + "/" + TEST_FILE_NAME).exists()

fun saveFileTest(context: Context) =
    File(context.filesDir.path + "/" + TEST_FILE_NAME).createNewFile()

fun saveFileTest2(context: Context) {
    try{
        context.openFileOutput(TEST_FILE_NAME, Context.MODE_PRIVATE).use {
            it.write(defaultText.text.toByteArray())
        }
    }catch (e: Exception) {
        Log.d("!!!", "saveFileTest2: fail")
    }
}

fun readFileTest(context: Context) {
    var c = 0

    try {
        context.openFileInput(TEST_FILE_NAME).use { inputStream ->
//                it.bufferedReader().
//                while (it.bufferedReader().read() != -1) {
//                    c = it.bufferedReader().read()
//                    Log.d("!!!!", "readFileTest: ${(Char(c))}")
//                }
//                Log.d("!!!", "readFileTest: ${it.reader().buffered().readText()}")

//            repeat(3) {
//                Log.d("!!!", "readFileTest: $it")
//            }

//            while (inputStream.bufferedReader().ready()) {
//                Log.d("!!!123", "readFileTest: ${inputStream.bufferedReader().readLine()}")
//            }
//            while (string != null) {
//                Log.d("!!!123", "readFileTest: $string")
//                string = inputStream.bufferedReader().readLine()
//            }

            var i = 0
//            inputStream.reader().forEachLine {
//                Log.d("!!! $i", "readFileTest: $it")
//                i += 1
//            }

            var listString = inputStream.reader().readLines()

            for (line in listString) {
                Log.d("!!!", line)
            }

            inputStream.close()
        }
//        context.openFileInput(TEST_FILE_NAME).use {
//            Log.d("!!!", (it.bufferedReader().readLine()))
//        }
    } catch (e: Exception) {
        Log.d("!!!", "readFileTest: $e")
    }
}