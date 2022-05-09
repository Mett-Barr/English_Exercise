package com.example.english.data.word.wordlist

import androidx.room.TypeConverter
import com.example.english.data.word.wordlist.room.EmptyWordListItem.emptyList
import com.example.english.data.word.wordlist.room.WordListItem
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow

class WordListConverter {
    @TypeConverter
    fun itemToString(wordListItem: WordListItem): String {
        val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter: JsonAdapter<WordListItem> = moshi.adapter(WordListItem::class.java)
        return jsonAdapter.toJson(wordListItem)
    }

    @TypeConverter
    fun stringToItem(string: String): WordListItem {
        val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter: JsonAdapter<WordListItem> = moshi.adapter(WordListItem::class.java)
        return jsonAdapter.fromJson(string) ?: emptyList
    }

//    @TypeConverter
//    fun toString(wordListItem: WordListItem): Flow {
//        val moshi: Moshi = Moshi.Builder().build()
//        val jsonAdapter: JsonAdapter<WordListItem> = moshi.adapter(WordListItem::class.java)
//        return jsonAdapter.toJson(wordListItem)
//    }


}

