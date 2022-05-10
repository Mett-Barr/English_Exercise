package com.example.english.data.word.wordlist

import com.example.english.data.word.wordlist.room.WordList
import com.example.english.data.word.wordlist.room.WordListDao
import kotlinx.coroutines.flow.Flow

class DefaultWordListRepository(private val dao: WordListDao): WordListRepository {
    override fun getWordListById(id: Int): Flow<WordList> {
        return dao.getWordListById(id)
    }

    override fun getWordListByNewsIndex(newsIndex: Int): Flow<WordList> {
        return dao.getWordListByNewsIndex(newsIndex)
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