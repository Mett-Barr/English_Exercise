package com.example.english.data.word

import android.util.Log
import com.example.english.data.word.word.WordRepository
import com.example.english.data.word.word.room.Word
import com.example.english.data.word.wordlist.WordListConverter
import com.example.english.data.word.wordlist.WordListRepository
import com.example.english.data.word.wordlist.room.WordIndex
import com.example.english.data.word.wordlist.room.WordList
import com.example.english.data.word.wordlist.room.WordListItem
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first

fun wordIsExist(word: Word, repository: WordRepository) {

}

suspend fun wordIsExistByEnglish(english: String, wordRepository: WordRepository): Boolean {
    return wordRepository.getWordByName(english).first() != null
}

fun addNewWord(word: Word, repository: WordRepository) {

}

suspend fun addWordInList(
    word: Word,
    newsIndex: Int,
    paragraphIndex: Int,
    wordListItem: WordListItem,
    wordRepository: WordRepository,
    wordListRepository: WordListRepository,
) {

    wordListRepository.addWordList(WordList(0, ""))

    var wordId: Int = 0

    // 檢查單字是否已存在
    if (!wordIsExistByEnglish(word.english, wordRepository)) {
        // 未存在，新增後獲得單字的index
        wordId = wordRepository.addNewWord(word).toInt()
        Log.d("!!!", "未存在，新增後獲得單字的index")


        val newWord = Word(wordId, word.english, word.chinese)

        val newWordListItem: WordListItem = WordListItem(wordListItem.list + WordIndex(paragraphIndex, wordId))

        // 將已確定的Word資訊，更新到WordList上
        wordListRepository.updateWordList(
            WordList(
                newsIndex,
                WordListConverter().itemToString(newWordListItem)
            )
        )
        Log.d("!!!", "將已確定的Word資訊，更新到WordList上")

    } else {
        // 已存在，從Word查詢單字的Index
        wordRepository.getWordId(word.english).collect {
            wordId = it
            Log.d("!!!", "已存在，從Word查詢單字的Index")


            val newWord = Word(wordId, word.english, word.chinese)

            Log.d("!!!1", wordListItem.toString())
            val newWordListItem: WordListItem = WordListItem(wordListItem.list + WordIndex(paragraphIndex, wordId))
            Log.d("!!!2", newWordListItem.toString())

            // 將已確定的Word資訊，更新到WordList上
            wordListRepository.updateWordList(
                WordList(
                    newsIndex,
                    WordListConverter().itemToString(newWordListItem)
                )
            )
            Log.d("!!!", "將已確定的Word資訊，更新到WordList上")

            wordListRepository.getWordList(1).collect {
                if (it != null) {

                    Log.d("!!!3", it.toString())
                }
            }

        }
    }

//    Log.d("!!!", "if else -> ")
//
//    val newWord = Word(wordId, word.english, word.chinese)
//
//    val newWordListItem: WordListItem = WordListItem(wordListItem.list + WordIndex(paragraphIndex, wordId))
//
//    // 將已確定的Word資訊，更新到WordList上
//    wordListRepository.updateWordList(
//        WordList(
//            newsIndex,
//            WordListConverter().itemToString(newWordListItem)
//        )
//    )
//    Log.d("!!!", "將已確定的Word資訊，更新到WordList上")
}