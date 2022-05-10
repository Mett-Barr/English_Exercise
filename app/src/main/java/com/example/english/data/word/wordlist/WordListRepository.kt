package com.example.english.data.word.wordlist

import com.example.english.data.word.wordlist.room.WordList
import com.example.english.data.word.wordlist.room.WordListItem
import kotlinx.coroutines.flow.Flow

interface WordListRepository {

    fun getWordListById(id: Int): Flow<WordList>

    fun getWordListByNewsIndex(newsIndex: Int): Flow<WordList>

    suspend fun addWordList(wordList: WordList)

    suspend fun updateWordList(wordList: WordList)

    suspend fun deleteWordList(wordList: WordList)
}