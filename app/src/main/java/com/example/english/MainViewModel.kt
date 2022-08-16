package com.example.english

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.english.data.article.*
import com.example.english.data.image.ImageOperatorObject
import com.example.english.data.newslist.Repository
import com.example.english.data.newslist.room.News
import com.example.english.data.word.addInWordListTable
import com.example.english.data.word.word.WordRepository
import com.example.english.data.word.word.room.Word
import com.example.english.network.JsoupNews
import com.example.english.network.imageStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val wordRepository: WordRepository,
) : ViewModel() {

    val isDownloading = mutableStateListOf<Int>()

    val done: (Int) -> Unit = { newId: Int ->
        isDownloading.remove(newId)
    }

    var currentWebsite by mutableStateOf(NewsWebsite.BBC)

    /** draft news */
    var draftTitle by mutableStateOf(TextFieldValue("test"))
    var draftContent by mutableStateOf(TextFieldValue("test"))

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

    var currentImage by mutableStateOf<Bitmap?>(null)
    var currentTitle by mutableStateOf("Title")

    var currentContent = mutableStateListOf<TextFieldValue>()
    var currentContentCn = mutableStateListOf<TextFieldValue>()
    var currentContentTr = mutableStateListOf<TextFieldValue>()

    var wordListTable: SnapshotStateList<SnapshotStateList<Int>> =
        mutableStateListOf(emptyList<Int>().toMutableStateList())

    var currentWord by mutableStateOf("")

    private fun wordExistCheck(word: String, index: Int) {
        viewModelScope.launch {
            currentWord = if (wordListTable[index].contains(wordRepository.getWordIdSuspend(word))) word else ""
//            currentWord = if (wordRepository.wordIsExist(word)) word else ""
        }
    }

    fun noCurrentWord() {
        currentWord = ""
    }

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

    fun changeDoneState(index: Int) {
        currentContent.also {
            if (it[index].text.first() == '^') {
                it[index] = it[index].copy(text = it[index].text.removeRange(0, 1))
            } else {
                it[index] = it[index].copy(text = '^' + it[index].text)
            }
        }
    }


    /** Repository operation */
    // Insert
    fun addNews(context: Context) {
        viewModelScope.launch { suspendAddNews(context) }
    }

    fun addNewsByUrl(context: Context, url: String) {
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


    // update
    fun updateProgress() {
        viewModelScope.launch {
            var done = 0
            currentContent.forEach { if (it.text.first() == '^') done++ }
            Log.d("!!!", "updateProgress: $done")

            val progress = (done * 100 / currentContent.size)
            Log.d("!!!", "updateProgress: $progress")
            repository.updateProgress(currentNews.copy(progress = progress))
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
            ) { done(fileNum.toInt()) }

            Log.d("??? 1", "addNewFile: ")
        }
    }

    private fun addUrlFile(fileNum: Long, content: String, context: Context) {
        viewModelScope.launch {
            FileOperator.addFile(
                fileNum = fileNum.toString(),
                draftContent = content,
                context = context
            ) { done(fileNum.toInt()) }

            Log.d("??? 1", "addNewFile: ")
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
        wordExistCheck(english, index)

        viewModelScope.launch {
            addInWordListTable(english, wordRepository, wordListTable[index])
        }

    }


    /** Navigation */
    fun newsPagePopBack() {

    }


    /**  Jsoup  */
    fun addNewsByJsoup(url: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val jsoupNews = JsoupNews(url)

            draftTitle = TextFieldValue(jsoupNews.getTitle())
            draftContent = TextFieldValue(jsoupNews.getContent())

//            suspendAddNews(context)
        }
    }

    fun addImage(bitmap: Bitmap, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            ImageOperatorObject.addImage("10", bitmap, context)
        }
    }


    /**  BBC  */
    fun addBBCNews(url: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {

            // get Article
            val jsoupNews = JsoupNews(url)

//            draftTitle = TextFieldValue(jsoupNews.getTitle())
//            draftContent = TextFieldValue(jsoupNews.getContent())

            val title = jsoupNews.getTitle()
            val content = jsoupNews.getContent()

            val caption = content.split("\n")[0]
            val newsId = addUrlNews(context, title, content)
//            isDownloading.add(newsId.toInt())

            // get Image
            val imageUrl = jsoupNews.getImageUrl()
            val bitmap = imageStore(imageUrl, context)
            bitmap?.let { ImageOperatorObject.addImage(newsId, it, context) }
        }
    }

    private suspend fun addUrlNews(
        context: Context,
        title: String,
//        caption: String,
        content: String,
    ): String {

//         Room
//        val caption = draftContent.text.split("\n")[0]
        val newsId = repository.addNews(News(title = title))
        isDownloading.add(newsId.toInt())

        // add file
        addUrlFile(newsId, content, context)
        Log.d("!!", "addUrlFile $newsId")


        return newsId.toString()
    }


}

enum class NewsWebsite(val url: String) {
    BBC("https://www.bbc.com/news")
}