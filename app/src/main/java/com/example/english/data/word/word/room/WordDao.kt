package com.example.english.data.word.word.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @Query("SELECT * from WordTable WHERE id=:id")
    fun getWord(id: Int): Flow<Word>

    @Query("SELECT * FROM WordTable WHERE english=:english")
    fun getWordByEnglish(english: String): Flow<Word?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewWord(word: Word): Long

    @Delete
    suspend fun deleteWord(word: Word)
}