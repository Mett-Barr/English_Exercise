package com.example.english.data.words.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @Query("SELECT * from WordTable ORDER BY id")
    fun getWord(): Flow<Word>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewWord(word: Word): Long

    @Delete
    suspend fun deleteWord(word: Word)
}