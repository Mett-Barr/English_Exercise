package com.example.english

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.english.data.newslist.Repository
import com.example.english.data.newslist.room.News
import com.example.english.data.newslist.room.NewsObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

const val FILE_NAME = "News_"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    // text input (testing)
    var textFieldValue by mutableStateOf(TextFieldValue("123"))

    /** draft news */
    var currentNews by mutableStateOf(NewsObject.news)
    var draftTitle by mutableStateOf(TextFieldValue(""))
    var draftContent by mutableStateOf(TextFieldValue(""))

    val list = repository.newsList()



    /** current news */
    var currentFileName by mutableStateOf(FILE_NAME + "0")
    var currentTitle by mutableStateOf("Title")

    //    var currentContent by mutableStateOf(listOf(""))
    var currentContent = mutableListOf<String>()
        private set

    fun currentNews(news: News, context: Context) {
        currentFileName = FILE_NAME + news.id.toString()
        currentTitle = news.title
        getFile(currentFileName, context)
    }




    var animTest by mutableStateOf(false)

    val roomSize: Flow<Int> = repository.getSize()

    var newsListIsEmpty = repository.isEmpty()

    fun roomTest() {
        viewModelScope.launch {
        }
    }


    /** Repository operation */

//    var currentNews = NewsObject.blankNews

    // Insert
    fun addNews(context: Context) {
        viewModelScope.launch { suspendAddNews(context) }
    }

    private suspend fun suspendAddNews(context: Context) {

        // Room
        val caption = draftContent.text.split("\n")[0]
        val id = repository.addNews(News(0, draftTitle.text, caption))
        val fileName = FILE_NAME + id.toString()

        // add file
        addFile(fileName, context)
    }


    /** txt operation */

    private fun addFile(fileName: String, context: Context) {
        val fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        fos.write(draftTitle.text.toByteArray())
        fos.write(draftContent.text.toByteArray())
        fos.close()
    }

    private fun getFile(fileName: String, context: Context) {
        val fos = context.openFileInput(fileName)
        val reader = BufferedReader(InputStreamReader(fos))
        currentContent = reader.readLines().toMutableList()
    }


//
//    fun roomTest() {
//        viewModelScope.launch {
//            repeat(3) { repository.addNews(NewsObject.news) }
//            newsListIsEmpty.value  = repository.isEmpty()
//            Log.d("!!!", "roomTest: ${newsListIsEmpty.value}")
//        }
//    }
}