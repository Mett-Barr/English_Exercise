package com.example.english.data.word.wordlist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.room.TypeConverter
import com.example.english.data.word.wordlist.room.EmptyWordList.wordListItem
import com.example.english.data.word.wordlist.room.WordListItem
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

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
        return jsonAdapter.fromJson(string) ?: wordListItem
    }


//    @TypeConverter
//    fun toString(wordListItem: WordListItem): Flow {
//        val moshi: Moshi = Moshi.Builder().build()
//        val jsonAdapter: JsonAdapter<WordListItem> = moshi.adapter(WordListItem::class.java)
//        return jsonAdapter.toJson(wordListItem)
//    }


}

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
    return jsonAdapter.fromJson(string) ?: wordListItem
}


fun wordListToNewsPage(wordListItem: WordListItem, size: Int): SnapshotStateList<MutableList<Int>> {

    val list = mutableStateListOf<MutableList<Int>>()
    val emptyList = mutableListOf<Int>()

    repeat(size) {
        list.add(emptyList)
    }

    wordListItem.list.forEach {
        list[it.paragraphIndex] += it.wordIndex
    }

    return list
}
