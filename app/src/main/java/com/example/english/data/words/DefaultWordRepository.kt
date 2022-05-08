package com.example.english.data.words

import com.example.english.data.words.room.Word
import com.example.english.data.words.room.WordDao
import kotlinx.coroutines.flow.Flow

class DefaultWordRepository(private val dao: WordDao) : WordRepository {
    override fun getWord(): Flow<Word> {
        return dao.getWord()
    }

    override suspend fun addNewWord(word: Word): Long {
        return dao.addNewWord(word)
    }

    override suspend fun deleteWord(word: Word) {
        dao.deleteWord(word)
    }
}