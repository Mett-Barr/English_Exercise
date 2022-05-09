package com.example.english.data.word.wordlist

import com.example.english.data.word.wordlist.room.WordList
import com.example.english.data.word.wordlist.room.WordListItem
import com.example.english.data.word.wordlist.room.WordListDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class DefaultWordListRepository(private val dao: WordListDao): WordListRepository {
    override suspend fun getWordList(id: Int): Flow<WordList> {
        return dao.getWordList(id)
    }

    override suspend fun addWordList(wordList: WordList) {
        dao.addWordList(wordList)
    }

    override suspend fun updateWordList(wordList: WordList) {
        dao.updateWordList(wordList)
    }

    override suspend fun deleteWordList(wordList: WordList) {
        dao.deleteWordList(wordList)
    }
}