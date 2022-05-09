package com.example.english.data.word.word

import com.example.english.data.word.word.room.Word
import kotlinx.coroutines.flow.Flow

interface WordRepository {

    fun getWord(id: Int): Flow<Word>

    fun getWordByName(english: String): Flow<Word?>

    fun getWordId(english: String): Flow<Int>

    suspend fun addNewWord(word: Word): Long

    suspend fun deleteWord(word: Word)

    suspend fun isWordExist(english: String): Boolean
}