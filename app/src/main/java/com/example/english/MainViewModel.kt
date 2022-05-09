package com.example.english

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.english.data.article.FILE_NAME
import com.example.english.data.article.FILE_NAME_CN
import com.example.english.data.article.FILE_NAME_Tr
import com.example.english.data.article.FileOperator
import com.example.english.data.newslist.Repository
import com.example.english.data.newslist.room.News
import com.example.english.data.word.word.WordRepository
import com.example.english.data.word.word.room.Word
import com.example.english.data.word.wordlist.WordListConverter
import com.example.english.data.word.wordlist.WordListRepository
import com.example.english.data.word.wordlist.room.EmptyWordListItem
import com.example.english.data.word.wordlist.room.WordIndex
import com.example.english.data.word.wordlist.room.WordList
import com.example.english.data.word.wordlist.room.WordListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val wordRepository: WordRepository,
    private val wordListRepository: WordListRepository
) : ViewModel() {


    /** draft news */
//    var currentNews by mutableStateOf(NewsObject.news)
    var draftTitle by mutableStateOf(TextFieldValue(""))
    var draftContent by mutableStateOf(TextFieldValue(""))

    val list = repository.newsList()


    /** current news */
    private var currentFileName by mutableStateOf(FILE_NAME + "0")
    private var currentFileNameCn by mutableStateOf(FILE_NAME_CN + "0")
    private var currentFileNameTr by mutableStateOf(FILE_NAME_Tr + "0")

    var currentTitle by mutableStateOf("Title")

    var currentContent = mutableStateListOf<TextFieldValue>()
    var currentContentCn = mutableStateListOf<TextFieldValue>()
    var currentContentTr = mutableStateListOf<TextFieldValue>()
    var currentContentWords = mutableStateListOf<TextFieldValue>()

    fun currentNews(news: News, context: Context) {
        currentFileName = FILE_NAME + news.id.toString()
        currentFileNameCn = FILE_NAME_CN + news.id.toString()
        currentFileNameTr = FILE_NAME_Tr + news.id.toString()
        currentTitle = news.title
        getFile(currentFileName, currentFileNameCn, currentFileNameTr, context)
    }

    fun removeCurrentParagraph(index: Int) {
        currentContent.removeAt(index)
        currentContentCn.removeAt(index)
        currentContentTr.removeAt(index)
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
        addFile(id, context)
    }


    /** File operation */

    private fun addFile(fileNum: Long, context: Context) {
        viewModelScope.launch {
            FileOperator.addFile(fileNum = fileNum.toString(), draftContent = draftContent.text, context = context)
        }
    }

    private fun getFile(fileName: String, fileNameCn: String, fileNameTr: String, context: Context) {
        viewModelScope.launch {
            currentContent = FileOperator.getFile(fileName, context)
            currentContentCn = FileOperator.getFileCn(fileNameCn, context)
            currentContentTr = FileOperator.getFile(fileNameTr, context)
        }
    }

//    fun saveFile(fileName: String, fileNameCn: String, context: Context) {
//
//        FileOperator.saveFile(fileName, currentContent, context)
//
//        FileOperator.saveFileCn(fileNameCn, currentContentCn, context)
//    }

    fun saveCurrentFile(context: Context) {
        viewModelScope.launch {
            FileOperator.saveCurrentFile(
                currentFileName = currentFileName,
                currentFileNameCn = currentFileNameCn,
                currentFileNameTr = currentFileNameTr,

                currentContent = currentContent,
                currentContentCn = currentContentCn,
                currentContentTr = currentContentTr,

                context = context
            )
        }
    }


    /** Word Operation  */
    // Test
    fun wordTest(text: String) {
        val word = Word(0, text)

        viewModelScope.launch {
//            wordRepository.addNewWord(word)
//            Log.d("!!!", "wordTest: ")

            val listWordIndex: List<WordIndex> = listOf(WordIndex(0, 0), WordIndex(1, 3))
            val wordListItem: WordListItem = WordListItem(listWordIndex)
            val list = WordList(0, WordListConverter().itemToString(wordListItem))
            wordListRepository.addWordList(list)

            Log.d("!!!1", list.toString())
            Log.d("!!!2", WordListConverter().itemToString(wordListItem))
            Log.d("!!!3", WordListConverter().itemToString(WordListConverter().stringToItem(list.wordList)))

        }
    }



    /** Navigation */
    fun newsPagePopBack() {

    }
}