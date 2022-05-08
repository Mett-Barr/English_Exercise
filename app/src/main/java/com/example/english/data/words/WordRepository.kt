package com.example.english.data.words

import com.example.english.data.words.room.Word
import kotlinx.coroutines.flow.Flow

interface WordRepository {

    fun getWord(): Flow<Word>

    suspend fun addNewWord(word: Word): Long

    suspend fun deleteWord(word: Word)
}