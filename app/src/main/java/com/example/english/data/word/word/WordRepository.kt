package com.example.english.data.word.word

import com.example.english.data.word.word.room.Word
import kotlinx.coroutines.flow.Flow

interface WordRepository {

    fun getWord(id: Int): Flow<Word>

    suspend fun getWordSus(id: Int): Word

    fun getWordByName(english: String): Flow<Word?>

    fun getWordId(english: String): Flow<Int>

    suspend fun getWordIdSuspend(english: String): Int?

    suspend fun addNewWord(word: Word): Long

    suspend fun updateWord(word: Word)

    suspend fun deleteWord(word: Word)

    suspend fun wordIsExist(english: String): Boolean
}