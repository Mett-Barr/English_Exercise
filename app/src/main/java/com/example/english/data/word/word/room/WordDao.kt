package com.example.english.data.word.word.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @Query("SELECT * from WordTable WHERE id=:id")
    fun getWord(id: Int): Flow<Word>

    @Query("SELECT * FROM WordTable WHERE english=:english")
    fun getWordByEnglish(english: String): Flow<Word?>

    @Query("SELECT id FROM WordTable WHERE english=:english")
    fun getWordId(english: String): Flow<Int>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewWord(word: Word): Long

    @Update
    suspend fun updateWord(word: Word)

    @Delete
    suspend fun deleteWord(word: Word)
}