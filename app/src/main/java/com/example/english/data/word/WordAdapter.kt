package com.example.english.data.word

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.english.data.word.word.WordRepository
import com.example.english.data.word.word.room.Word
import kotlinx.coroutines.flow.first

suspend fun wordIsExistByEnglish(english: String, wordRepository: WordRepository): Boolean {
    return wordRepository.getWordByName(english).first() != null
}

suspend fun addInWordListTable(
    english: String,
    wordRepository: WordRepository,
    list: SnapshotStateList<Int>,
): SnapshotStateList<Int>  {

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

    return list
}
