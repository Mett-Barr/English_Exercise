package com.example.english.data.word

import android.util.Log
import com.example.english.data.word.word.WordRepository
import com.example.english.data.word.word.room.Word
import com.example.english.data.word.wordlist.WordListRepository
import com.example.english.data.word.wordlist.itemToString
import com.example.english.data.word.wordlist.room.WordIndex
import com.example.english.data.word.wordlist.room.WordList
import com.example.english.data.word.wordlist.room.WordListItem
import kotlinx.coroutines.flow.first

fun wordIsExist(word: Word, repository: WordRepository) {

}

suspend fun wordIsExistByEnglish(english: String, wordRepository: WordRepository): Boolean {
    return wordRepository.getWordByName(english).first() != null
}

fun addNewWord(word: Word, repository: WordRepository) {

}

suspend fun addWordInList(
    english: String,
    paragraphIndex: Int,
//    wordList: WordList,
//    wordListItem: WordListItem,
    wordRepository: WordRepository,
    wordListForPage: MutableList<MutableList<Int>>
): MutableList<MutableList<Int>> {
    var wordId = 0

    // 檢查單字是否存在

    if (wordIsExistByEnglish(english, wordRepository)) {
        // word已存在
        wordRepository.getWordId(english).collect {
            // 獲取ID並更新WordList
            wordId = it

            // 更新wordListForPage
            wordListForPage[paragraphIndex] += wordId
        }
    } else {
        // word不存在

        // 新增new word並獲取wordId
        wordId = wordRepository.addNewWord(Word(english = english)).toInt()

        // 更新wordListForPage
        wordListForPage[paragraphIndex] += wordId
    }

    return wordListForPage
}


suspend fun oldaddWordInList(
    word: Word,
    paragraphIndex: Int,
    wordList: WordList,
    wordListItem: WordListItem,
    wordRepository: WordRepository,
    wordListRepository: WordListRepository,
) {

//    wordListRepository.addWordList(WordList(0, newsIndex,""))

    var wordId: Int = 0

    // 檢查單字是否已存在
    if (!wordIsExistByEnglish(word.english, wordRepository)) {
        // 未存在，新增後獲得單字的ID
        wordId = wordRepository.addNewWord(word).toInt()
        Log.d("!!!", "未存在，新增後獲得單字的index")

//        val newWord = Word(wordId, word.english, word.chinese)

//        val newWord = Word(wordId, word.english, word.chinese)
//
//        val newWordListItem: WordListItem = WordListItem(wordListItem.list + WordIndex(paragraphIndex, wordId))
//
//        // 將已確定的Word資訊，更新到WordList上
//        wordListRepository.updateWordList(
//            WordList(
//                newsIndex,
//                WordListConverter().itemToString(newWordListItem)
//            )
//        )
//        Log.d("!!!", "將已確定的Word資訊，更新到WordList上")

        oldupdateWordList(
            wordId,
//            newWord,
            paragraphIndex,
            wordListItem,
            wordList,
            wordListRepository,
        )

    } else {
        // 已存在，從Word查詢單字的ID
        wordRepository.getWordId(word.english).collect {
            wordId = it
            Log.d("!!!", "已存在，從Word查詢單字的Index")


            oldupdateWordList(
                wordId,
//                word,
                paragraphIndex,
                wordListItem,
                wordList,
                wordListRepository,
            )
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


private fun updateWordListForPage(
    wordId: Int,
    paragraphIndex: Int,
    wordListForPage: MutableList<MutableList<Int>>
) {
    wordListForPage[paragraphIndex] += wordId
}


private suspend fun oldupdateWordList(
    wordId: Int,
//    word: Word,
    paragraphIndex: Int,
    wordListItem: WordListItem,
    wordList: WordList,
    wordListRepository: WordListRepository
) {
//    val newWord = Word(wordId, word.english, word.chinese)

    Log.d("!!!1", wordListItem.toString())
    val newWordListItem =
        WordListItem(wordListItem.list + WordIndex(paragraphIndex, wordId))
    Log.d("!!!2", newWordListItem.toString())

    // 將已確定的Word資訊，更新到WordList上
    wordListRepository.updateWordList(
        WordList(
            wordList.id,
            wordList.newsId,
            itemToString(newWordListItem)
        )
    )
    Log.d("!!!", "將已確定的Word資訊，更新到WordList上")
}