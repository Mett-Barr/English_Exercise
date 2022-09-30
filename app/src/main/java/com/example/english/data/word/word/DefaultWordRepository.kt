package com.example.english.data.word.word

import com.example.english.data.word.word.room.Word
import com.example.english.data.word.word.room.WordDao
import kotlinx.coroutines.flow.Flow

class DefaultWordRepository(private val dao: WordDao) : WordRepository {
    override fun getWord(id: Int): Flow<Word> {
        return dao.getWord(id)
    }

    override suspend fun getWordSus(id: Int): Word {
        return dao.getWordSus(id)
    }

    override fun getWordByEnglish(english: String): Flow<Word?> {
        return dao.getWordByEnglish(english)
    }

    override fun getWordByEnglishSus(english: String): Word? {
        return dao.getWordByEnglishSus(english)
    }

    override fun getWordId(english: String): Flow<Int> {
        return dao.getWordId(english)
    }

    override suspend fun getWordIdSuspend(english: String): Int? {
        return dao.getWordIdSuspend(english)
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

    override suspend fun wordIsExist(english: String): Boolean {
        return dao.isWordExist(english)
    }
}