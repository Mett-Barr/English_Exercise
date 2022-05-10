package com.example.english.data.word.word

import com.example.english.data.word.word.room.Word
import com.example.english.data.word.word.room.WordDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

class DefaultWordRepository(private val dao: WordDao) : WordRepository {
    override fun getWord(id: Int): Flow<Word> {
        return dao.getWord(id)
    }

    override fun getWordByName(english: String): Flow<Word?> {
        return dao.getWordByEnglish(english)
    }

    override fun getWordId(english: String): Flow<Int> {
        return dao.getWordId(english)
    }

    override suspend fun addNewWord(word: Word): Long {
        return dao.addNewWord(word)
    }

    override suspend fun updateWord(word: Word) {
        dao.updateWord(word)
    }

    override suspend fun deleteWord(word: Word) {
        dao.deleteWord(word)
    }

    override suspend fun isWordExist(english: String): Boolean {
        return getWordByName(english).first() != null
    }
}