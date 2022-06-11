package com.example.english

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.english.data.article.*
import com.example.english.data.newslist.Repository
import com.example.english.data.newslist.room.News
import com.example.english.data.word.addInWordListTable
import com.example.english.data.word.word.WordRepository
import com.example.english.data.word.word.room.Word
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val wordRepository: WordRepository,
) : ViewModel() {


    /** draft news */
    var draftTitle by mutableStateOf(TextFieldValue(""))
    var draftContent by mutableStateOf(TextFieldValue(""))
    //
    fun navToInsertPage() {
        draftTitle = TextFieldValue("新聞標題")
        draftContent = TextFieldValue("文章內容")
    }

    val list = repository.newsList()


    /** current news */
    private var currentNews by mutableStateOf(News())
    private var currentNewsId by mutableStateOf("0")
    private var currentNewsSize by mutableStateOf(0)

    var currentTitle by mutableStateOf("Title")

    var currentContent = mutableStateListOf<TextFieldValue>()
    var currentContentCn = mutableStateListOf<TextFieldValue>()
    var currentContentTr = mutableStateListOf<TextFieldValue>()

    var wordListTable: SnapshotStateList<SnapshotStateList<Int>> =
        mutableStateListOf(emptyList<Int>().toMutableStateList())

    val testList =
        mutableStateListOf(mutableStateListOf(1), mutableStateListOf(2), mutableStateListOf(3))

    fun currentNews(news: News, context: Context) {
        currentNews = news
        currentNewsId = news.id.toString()
        currentTitle = news.title

        // get content for page
        getFile(context)
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
        val newsId = repository.addNews(News(0, draftTitle.text, caption))

        // add file
        addNewFile(newsId, context)

    }

    // Delete
    fun deleteNews(context: Context) {
        viewModelScope.launch {

            // Room
            repository.deleteNews(currentNews)

            // File
            deleteCurrentFile(context)
        }
    }


    /** File operation */

    // Add
    private fun addNewFile(fileNum: Long, context: Context) {
        viewModelScope.launch {
            FileOperator.addFile(
                fileNum = fileNum.toString(),
                draftContent = draftContent.text,
                context = context
            )
        }
    }

    // Get
    private fun getFile(
        context: Context,
    ) {
        viewModelScope.launch {
            currentContent = FileOperator.getFile(FILE_NAME + currentNewsId, context)

            currentNewsSize = currentContent.size

            currentContentCn = FileOperator.getFileCn(FILE_NAME_CN + currentNewsId, context)
            currentContentTr = FileOperator.getFile(FILE_NAME_Tr + currentNewsId, context)

            wordListTable =
                FileOperator.getWordListTableFile(FILE_NAME_WORDLISTTABLE + currentNewsId, context)
        }
    }

    fun saveCurrentFile(context: Context) {
        viewModelScope.launch {
            FileOperator.saveCurrentFile(
                currentFileName = FILE_NAME + currentNewsId,
                currentFileNameCn = FILE_NAME_CN + currentNewsId,
                currentFileNameTr = FILE_NAME_Tr + currentNewsId,
                currentFileNameWordListTable = FILE_NAME_WORDLISTTABLE + currentNewsId,


                currentContent = currentContent,
                currentContentCn = currentContentCn,
                currentContentTr = currentContentTr,
                wordListTable = wordListTable,

                context = context
            )
        }
    }

    // Delete
    private fun deleteCurrentFile(context: Context) {
        viewModelScope.launch {
            FileOperator.deleteCurrentFile(currentNewsId, context)
        }
    }



    /** Word Operation  */
    // Query
    fun getWordById(id: Int): Flow<Word> {
        return wordRepository.getWord(id)
    }

    fun getWordList(list: List<Int>): List<Flow<Word>> {
        val wordList: MutableList<Flow<Word>> = emptyList<Flow<Word>>().toMutableList()

        list.forEach {
            wordList.add(wordRepository.getWord(it))
        }

        return wordList
    }

    // Insert
    fun addNewWord(word: Word) {
        viewModelScope.launch {
            wordRepository.addNewWord(word)
        }
    }

    // update
    fun updateWord(word: Word) {
        viewModelScope.launch {
            wordRepository.updateWord(word)
        }
    }

    // Delete()
    fun deleteWord(word: Word) {
        viewModelScope.launch {
            wordRepository.deleteWord(word)
        }
    }



    /** WordList Operation */
    //Insert
    fun addWordListTable(english: String, index: Int) {
        viewModelScope.launch {
            addInWordListTable(english, wordRepository, wordListTable[index])
        }
    }



    /** Navigation */
    fun newsPagePopBack() {

    }
}