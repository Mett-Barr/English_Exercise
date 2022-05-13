package com.example.english.data.word.wordlist

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.room.TypeConverter
import com.example.english.data.word.wordlist.room.EmptyWordList
import com.example.english.data.word.wordlist.room.EmptyWordList.emptyWordListItem
import com.example.english.data.word.wordlist.room.WordIndex
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
        return jsonAdapter.fromJson(string) ?: emptyWordListItem
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
//    val string = jsonAdapter.toJson(wordListItem)
    return jsonAdapter.toJson(wordListItem)
//    return string
}

@TypeConverter
fun stringToItem(string: String): WordListItem {
    val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val jsonAdapter: JsonAdapter<WordListItem> = moshi.adapter(WordListItem::class.java)
//    val wordListItem = jsonAdapter.fromJson(string) ?: wordListItem
    return jsonAdapter.fromJson(string) ?: emptyWordListItem

//    val wordListItem:WordListItem = try {
//        jsonAdapter.fromJson(string) ?: emptyWordListItem
//    } catch (e: Exception) {
//        Log.d("!!!", e.toString())
//        emptyWordListItem
//    }
//    return wordListItem
}


fun wordListToPage(wordListItem: WordListItem, size: Int): SnapshotStateList<MutableList<Int>> {

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

fun wordListToStringFile(wordListForPage: List<List<Int>>): String {

    var wordListItem: WordListItem = EmptyWordList.emptyWordListItem
    var paragraphIndex = 0
    wordListForPage.forEach { paragraphList ->
        paragraphList.forEach { wordIndex ->

            wordListItem.list += WordIndex(
                wordIndex = wordIndex,
//                paragraphIndex = paragraphList.indexOf(paragraphList)
                paragraphIndex = paragraphIndex
            )
        }
        paragraphIndex += 1
    }

    return itemToString(wordListItem)
}
