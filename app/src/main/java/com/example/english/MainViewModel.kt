package com.example.english

import android.content.Context
import androidx.compose.runtime.*
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
const val FILE_NAME_CN = "News_CN_"

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
    private var currentFileName by mutableStateOf(FILE_NAME + "0")
    var currentFileNameCn by mutableStateOf(FILE_NAME_CN + "0")

    var currentTitle by mutableStateOf("Title")

    //    var currentContent by mutableStateOf(listOf(""))
    var currentContent = mutableStateListOf<String>()
    var currentContentCn = mutableStateListOf<String>()

    fun currentNews(news: News, context: Context) {
        currentFileName = FILE_NAME + news.id.toString()
        currentFileNameCn = FILE_NAME_CN + news.id.toString()
        currentTitle = news.title
        getFile(currentFileName, currentFileNameCn, context)
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
        val fileNameCn = FILE_NAME_CN + id.toString()

        // add file
        addFile(fileName, fileNameCn, context)
    }


    /** txt operation */

    private fun addFile(fileName: String, fileNameCn: String, context: Context) {
        val fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)
//        fos.write(draftTitle.text.toByteArray())
        fos.write(draftContent.text.toByteArray())
        fos.close()


        val fosCn = context.openFileOutput(fileNameCn, Context.MODE_PRIVATE)
        val listSize = draftContent.text.lines().size
        repeat(listSize) {
            fosCn.write("\n".toByteArray())
        }
//        fosCn.write(listSize.toString().toByteArray())

//        fosCn.write(draftTitle.text.toByteArray())
//        fosCn.write(draftContent.text.toByteArray())
        fosCn.close()
    }

    private fun getFile(fileName: String, fileNameCn: String, context: Context) {
        val fos = context.openFileInput(fileName)
        val reader = BufferedReader(InputStreamReader(fos))
        val tmpList = reader.readLines()
//        currentContent = reader.readLines().toMutableStateList()
        tmpList.forEach {
//            if (it != "") {
//            }
            currentContent.add(it)
        }

        val fosCn = context.openFileInput(fileNameCn)
        val readerCn = BufferedReader(InputStreamReader(fosCn))
        val tmpListCn = readerCn.readLines()
//        currentContent = reader.readLines().toMutableStateList()
//        currentContentCn.add("CN")
        tmpListCn.forEach {
//            if (it != "") {
//            }
                currentContentCn.add(it)
        }

    }

//    private fun fileParagraphs()


//
//    fun roomTest() {
//        viewModelScope.launch {
//            repeat(3) { repository.addNews(NewsObject.news) }
//            newsListIsEmpty.value  = repository.isEmpty()
//            Log.d("!!!", "roomTest: ${newsListIsEmpty.value}")
//        }
//    }
}