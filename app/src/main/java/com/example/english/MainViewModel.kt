package com.example.english

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.english.data.article.*
import com.example.english.data.newslist.Repository
import com.example.english.data.newslist.room.News
import com.example.english.data.word.addWordTest
import com.example.english.data.word.word.WordRepository
import com.example.english.data.word.word.room.Word
import com.example.english.data.word.wordlist.wordListToStringFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    val wordRepository: WordRepository,
//    val wordListRepository: WordListRepository,
) : ViewModel() {


    /** draft news */
//    var currentNews by mutableStateOf(NewsObject.news)
    var draftTitle by mutableStateOf(TextFieldValue(""))
    var draftContent by mutableStateOf(TextFieldValue(""))

    val list = repository.newsList()


    /** current news */
    private var currentNewsId by mutableStateOf("0")
    private var currentNewsSize by mutableStateOf(0)

    var currentTitle by mutableStateOf("Title")

    var currentContent = mutableStateListOf<TextFieldValue>()
    var currentContentCn = mutableStateListOf<TextFieldValue>()
    var currentContentTr = mutableStateListOf<TextFieldValue>()
    var currentContentWordList: SnapshotStateList<SnapshotStateList<Int>> =
        mutableStateListOf(emptyList<Int>().toMutableStateList())

    val testList =
        mutableStateListOf(mutableStateListOf(1), mutableStateListOf(2), mutableStateListOf(3))

    fun currentNews(news: News, context: Context) {
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
//        val fileName = FILE_NAME + newsId.toString()
//        val fileNameCn = FILE_NAME_CN + newsId.toString()

        // add file
        addNewFile(newsId, context)

        // add word list
//        addNewWordList(
//            WordList(newsId = newsId.toInt(), wordListItemString = "")
//        )
    }


    /** File operation */

    private fun addNewFile(fileNum: Long, context: Context) {
        viewModelScope.launch {
            FileOperator.addFile(
                fileNum = fileNum.toString(),
                draftContent = draftContent.text,
                context = context
            )
        }
    }

    private fun getFile(
        context: Context,
    ) {
        viewModelScope.launch {
            currentContent = FileOperator.getFile(FILE_NAME + currentNewsId, context)

            currentNewsSize = currentContent.size

            currentContentCn = FileOperator.getFileCn(FILE_NAME_CN + currentNewsId, context)
            currentContentTr = FileOperator.getFile(FILE_NAME_Tr + currentNewsId, context)
            currentContentWordList = FileOperator.getWordListFile(
                FILE_NAME_WORDLIST + currentNewsId,
                currentNewsSize,
                context)
            currentContentWordList.forEach {
                Log.d("!!!", it.toList().toString())
            }
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
                currentFileName = FILE_NAME + currentNewsId,
                currentFileNameCn = FILE_NAME_CN + currentNewsId,
                currentFileNameTr = FILE_NAME_Tr + currentNewsId,

                currentFileNameWordList = FILE_NAME_WORDLIST + currentNewsId,

                currentContent = currentContent,
                currentContentCn = currentContentCn,
                currentContentTr = currentContentTr,

                currentContentWordList = currentContentWordList,

                context = context
            )
        }
    }


    /** Word Operation  */
    // Query
    fun getWordById(id: Int): Flow<Word> {
        return wordRepository.getWord(id)
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


    // Test
//    fun wordTest(text: String) {
//        val word = Word(0, text)
//
//        viewModelScope.launch {
////            wordRepository.addNewWord(word)
////            Log.d("!!!", "wordTest: ")
//
////            val listWordIndex: List<WordIndex> = listOf(WordIndex(0, 0), WordIndex(1, 3))
////            val wordListItem: WordListItem = WordListItem(listWordIndex)
////            val list = WordList(0, WordListConverter().itemToString(wordListItem))
////            wordListRepository.addWordList(list)
////
////            Log.d("!!!1", list.toString())
////            Log.d("!!!2", WordListConverter().itemToString(wordListItem))
////            Log.d("!!!3", WordListConverter().itemToString(WordListConverter().stringToItem(list.wordList)))
//
//
//            oldaddWordInList(
//                word = Word(0, "dog", "狗"),
//                paragraphIndex = 0,
//                wordList = oldCurrentWordList.value,
//                wordListItem = WordListItem(
//                    listOf(
//                        WordIndex(0, 1),
//                        WordIndex(0, 2),
//                        WordIndex(2, 1)
//                    )
//                ),
//                wordRepository = wordRepository,
//                wordListRepository = wordListRepository
//            )
//
//        }
//    }


    /** WordList Operation */
    //Insert
    fun addWordInList(english: String, paragraphIndex: Int) {

        Log.d("!!!", "addWordInList: $paragraphIndex")

        Log.d("!!!", "addWordInList: $paragraphIndex")



        viewModelScope.launch {
//            Log.d("!!! 1", "addWordInList: ${wordListToStringFile(currentContentWordList)}")
            currentContentWordList[paragraphIndex] = com.example.english.data.word.addWordInListTest(
                english = english,
                wordRepository = wordRepository,
                wordListTable = currentContentWordList[paragraphIndex]
            )

//            currentContentWordList[paragraphIndex] = com.example.english.data.word.addWordInList(
//                english = english,
//                wordRepository = wordRepository,
//                wordListTable = currentContentWordList[paragraphIndex]
//            )

//            currentContentWordList[paragraphIndex] +

//            val wordId =
//                addWordTest(english, wordRepository, currentContentWordList[paragraphIndex])
//
//            if (wordId != null) currentContentWordList[paragraphIndex].add(wordId)
//
//            Log.d("!!!", "addWordInList: $wordId")

//            addWordTest(english,
//                wordRepository,
//                currentContentWordList[paragraphIndex]).also {
//                Log.d("!!!", "addWordInList: addWordTest")
//                if (it != null) currentContentWordList[paragraphIndex].add(it)
//            }

//            Log.d("!!! 2", "addWordInList: ${wordListToStringFile(currentContentWordList)}")
//            currentContentWordList.toList().forEach {
//                Log.d("!!! forEach", "$it：${it.toList()}")
//            }
//
//            val list: List<List<Int>> = currentContentWordList.toList()
//            Log.d("!!! list", list.toString())
        }

    }


//    // Query
//    private fun getWordListByNewsIndex(newsIndex: Int) {
//        viewModelScope.launch {
////            currentWordList = wordListRepository.getWordListByNewsIndex(newsIndex)
//        }
//    }
//
////    fun getWordListToPage() {
////        viewModelScope.launch {
////            currentContentWordList
////        }
////    }
//
//    fun getCurrentWordList(): Flow<WordList> =
//        wordListRepository.getWordListByNewsIndex(currentNewsIndex)
//
////    suspend fun getCurrentWordListToPage() {
////        getCurrentWordList().collect {
////
////        }
////    }
//
//    // Insert
//    private suspend fun addNewWordList(wordList: WordList) {
//        wordListRepository.addWordList(wordList)
//    }
//
//    // Update
//    fun updateWordList(wordList: WordList) {
//        viewModelScope.launch {
//            wordListRepository.updateWordList(wordList)
//        }
//    }
//
//    // Delete
//    fun deleteWordList(wordList: WordList) {
//        viewModelScope.launch {
//            wordListRepository.deleteWordList(wordList)
//        }
//    }


    /** Navigation */
    fun newsPagePopBack() {

    }
}