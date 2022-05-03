package com.example.english

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.english.data.newslist.Repository
import com.example.english.data.newslist.room.News
import com.example.english.data.newslist.room.NewsObject
import com.example.english.stringconverter.StringConverter
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

    var currentContent = mutableStateListOf<TextFieldValue>()
    var currentContentCn = mutableStateListOf<TextFieldValue>()
    var currentContentTr = mutableStateListOf<TextFieldValue>()
    var currentContentWords = mutableStateListOf<TextFieldValue>()

    fun currentNews(news: News, context: Context) {
        currentFileName = FILE_NAME + news.id.toString()
        currentFileNameCn = FILE_NAME_CN + news.id.toString()
        currentTitle = news.title
        getFile(currentFileName, currentFileNameCn, context)
    }




    /** Repository operation */
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


    /** File operation */
    private fun addFile(fileName: String, fileNameCn: String, context: Context) {
        val fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        val list = StringConverter().stringToList(draftContent.text)
        list.forEach { fos.write(it.toByteArray()) }
        fos.close()

        val fosCn = context.openFileOutput(fileNameCn, Context.MODE_PRIVATE)
        repeat(list.size) { fosCn.write("\n".toByteArray())}
        fosCn.close()
    }

    private fun getFile(fileName: String, fileNameCn: String, context: Context) {
        val fos = context.openFileInput(fileName)
        val reader = BufferedReader(InputStreamReader(fos))
        val tmpList = reader.readLines()
        currentContent = emptyList<TextFieldValue>().toMutableStateList()
        tmpList.forEach {
            currentContent.add(TextFieldValue(it))
        }

        val fosCn = context.openFileInput(fileNameCn)
        val readerCn = BufferedReader(InputStreamReader(fosCn))
        val tmpListCn = readerCn.readLines()
        currentContentCn = emptyList<TextFieldValue>().toMutableStateList()
        tmpListCn.forEach {
            currentContentCn.add(TextFieldValue(it))
        }
//        currentContentCn = tmpListCn.toMutableStateList()
    }

    fun saveFile(fileName: String, fileNameCn: String, context: Context) {
        val fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        currentContent.forEach {
            fos.write(it.text.toByteArray())
        }
//        val list = StringConverter().stringToList(draftContent.text)
//        list.forEach { fos.write(it.toByteArray()) }
        fos.close()

        val fosCn = context.openFileOutput(fileNameCn, Context.MODE_PRIVATE)
        currentContentCn.forEach {
            fosCn.write(it.text.toByteArray())
        }
//        repeat(list.size) { fosCn.write("\n".toByteArray())}
        fosCn.close()
    }

    fun saveCurrentFile(context: Context) {
        val fos = context.openFileOutput(currentFileName, Context.MODE_PRIVATE)
        currentContent.forEach {
            fos.write(it.text.plus("\n").toByteArray())
        }
//        val list = StringConverter().stringToList(draftContent.text)
//        list.forEach { fos.write(it.toByteArray()) }
        fos.close()

        val fosCn = context.openFileOutput(currentFileNameCn, Context.MODE_PRIVATE)
        currentContentCn.forEach {
            fosCn.write(it.text.plus("\n").toByteArray())
        }
//        repeat(list.size) { fosCn.write("\n".toByteArray())}
        fosCn.close()
    }



    /** Navigation */
    fun newsPagePopBack() {

    }
}