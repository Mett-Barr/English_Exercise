package com.example.english.data.word

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.english.data.word.word.WordRepository
import com.example.english.data.word.word.room.Word
import kotlinx.coroutines.flow.first

suspend fun wordIsExistByEnglish(english: String, wordRepository: WordRepository): Boolean {
    return wordRepository.wordIsExist(english)
//    return wordRepository.getWordByName(english).first() != null
}

suspend fun addInWordListTable(
    english: String,
    wordRepository: WordRepository,
    list: SnapshotStateList<Int>,
    finish: () -> Unit = {}
): SnapshotStateList<Int>  {

    Log.d("!!!", "addInWordListTable: $english")
    if (wordIsExistByEnglish(english, wordRepository)) {
        wordRepository.getWordId(english).collect {
            val wordId = it
            if (!list.contains(wordId)) {
                list.add(wordId)
            }
        }
    } else {
        val wordId = wordRepository.addNewWord(Word(english = english)).toInt()
        list.add(wordId)
    }

    finish()

    return list
}
