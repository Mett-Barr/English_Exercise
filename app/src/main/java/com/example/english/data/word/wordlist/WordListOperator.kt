package com.example.english.data.word.wordlist

import androidx.room.TypeConverter
import com.example.english.data.word.wordlist.EmptyWordListTable.emptyWordListTable
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@TypeConverter
fun wordListTableToString(wordListTable: WordListTable): String {
    val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val jsonAdapter: JsonAdapter<WordListTable> = moshi.adapter(WordListTable::class.java)
    return jsonAdapter.toJson(wordListTable)
}

@TypeConverter
fun stringToWordListTable(string: String): WordListTable {
    val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val jsonAdapter: JsonAdapter<WordListTable> = moshi.adapter(WordListTable::class.java)
    return jsonAdapter.fromJson(string) ?: emptyWordListTable
}