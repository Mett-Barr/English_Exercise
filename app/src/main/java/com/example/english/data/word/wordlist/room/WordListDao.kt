package com.example.english.data.word.wordlist.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WordListDao {

    @Query("SELECT * from wordlist WHERE id=:id")
    fun getWordListById(id: Int): Flow<WordList>

    @Query("SELECT * from wordlist WHERE newsId=:newsIndex")
    fun getWordListByNewsIndex(newsIndex: Int): Flow<WordList>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWordList(wordList: WordList)

    @Update
    suspend fun updateWordList(wordList: WordList)

    @Delete
    suspend fun deleteWordList(wordList: WordList)
}