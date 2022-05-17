package com.example.english.data.word

import android.content.Context
import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.english.MainViewModel
import com.example.english.data.word.word.WordRepository
import com.example.english.data.word.word.room.Word
import kotlinx.coroutines.flow.first

fun wordIsExist(word: Word, repository: WordRepository) {

}

suspend fun wordIsExistByEnglish(english: String, wordRepository: WordRepository): Boolean {
    return wordRepository.getWordByName(english).first() != null
}

suspend fun addWordInList(
    english: String,
    wordRepository: WordRepository,
    wordListTable: SnapshotStateList<Int>,
): SnapshotStateList<Int> {
    var wordId = 0

    // 檢查單字是否存在

    Log.d("!!! addWordInList 1", wordListTable.toList().toString())

    if (wordIsExistByEnglish(english, wordRepository)) {
        // word已存在
        wordRepository.getWordId(english).collect {
            // 獲取ID並更新WordList
            wordId = it


            // 檢查是否已存在於WordListTable上
            if (!wordListTable.contains(wordId)) {
                // 若未存在於WordList上

//                Log.d("!!! 若未存在於WordList上", wordListTable.toList().toString())

                // 新增至wordListTable
                wordListTable.add(wordId)

//                Log.d("!!! addWordInList", wordListTable.toList().toString())


                Log.d("!!! 若未存在於wordListTable上", wordListTable.toList().toString())
            }

            Log.d("!!! word已存在於wordListTable上", wordListTable.toList().toString())
        }
    } else {
        // word不存在

        // 新增new word並獲取wordId
        wordId = wordRepository.addNewWord(Word(english = english)).toInt()

        // 新增至wordListTable
        wordListTable.add(wordId)
    }


    Log.d("!!! addWordInList 2", wordListTable.toList().toString())
    return wordListTable
}

suspend fun addWordByVM(
    english: String,
    index: Int,
    viewModel: MainViewModel,
){
    var wordId: Int? = null

    if (wordIsExistByEnglish(english, viewModel.wordRepository)) {

        viewModel.wordRepository.getWordId(english).collect {
            if (!viewModel.currentContentWordList[index].contains(it)) {
                viewModel.currentContentWordList[index].add(it)
            }
        }
    } else {
        wordId = viewModel.wordRepository.addNewWord(Word(english = english)).toInt()
        viewModel.currentContentWordList[index].add(wordId)
    }
}

suspend fun addWordInListTest(
    english: String,
    wordRepository: WordRepository,
    wordListTable: SnapshotStateList<Int>,
): SnapshotStateList<Int> {
//    var wordId = 0
//
//    // 檢查單字是否存在
//
//    Log.d("!!! addWordInList 1", wordListTable.toList().toString())
//
//    if (wordIsExistByEnglish(english, wordRepository)) {
//        // word已存在
//        wordRepository.getWordId(english).collect {
//            // 獲取ID並更新WordList
//            wordId = it
//
//
//            // 檢查是否已存在於WordListTable上
//            if (!wordListTable.contains(wordId)) {
//                // 若未存在於WordList上
//
////                Log.d("!!! 若未存在於WordList上", wordListTable.toList().toString())
//
//                // 新增至wordListTable
//                wordListTable.add(wordId)
//
////                Log.d("!!! addWordInList", wordListTable.toList().toString())
//
//
//                Log.d("!!! 若未存在於wordListTable上", wordListTable.toList().toString())
//            }
//
//            Log.d("!!! word已存在於wordListTable上", wordListTable.toList().toString())
//        }
//    } else {
//        // word不存在
//
//        // 新增new word並獲取wordId
//        wordId = wordRepository.addNewWord(Word(english = english)).toInt()
//
//        // 新增至wordListTable
//        wordListTable.add(wordId)
//    }
//
//
//    Log.d("!!! addWordInList 2", wordListTable.toList().toString())

    wordListTable.add(1)

    Log.d("!!! addWordInListTest", wordListTable.toList().toString())

    return wordListTable
}

suspend fun addWordTest(
    english: String,
    wordRepository: WordRepository,
    wordListTable: SnapshotStateList<Int>,
): Int? {
    var wordId: Int? = null

    // 檢查單字是否存在

    Log.d("!!! addWordInList 1", wordListTable.toList().toString())

    if (wordIsExistByEnglish(english, wordRepository) && !wordListTable.contains(wordId)) {
        wordRepository.getWordId(english).collect {
            wordId = it
        }
    }

    if (!wordIsExistByEnglish(english, wordRepository)) {
        wordId = wordRepository.addNewWord(Word(english = english)).toInt()
    }



    Log.d("!!! addWordInList 2", wordListTable.toList().toString())
    return 0
}
