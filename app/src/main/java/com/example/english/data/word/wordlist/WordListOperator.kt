package com.example.english.data.word.wordlist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.room.TypeConverter
import com.example.english.data.word.wordlist.room.EmptyWordList.emptyWordListItem
import com.example.english.data.word.wordlist.room.EmptyWordListTable.emptyWordListTable
import com.example.english.data.word.wordlist.room.WordIndex
import com.example.english.data.word.wordlist.room.WordListItem
import com.example.english.data.word.wordlist.room.WordListTable
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
    return jsonAdapter.toJson(wordListItem)
}

@TypeConverter
fun stringToItem(string: String): WordListItem {
    val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val jsonAdapter: JsonAdapter<WordListItem> = moshi.adapter(WordListItem::class.java)
    return jsonAdapter.fromJson(string) ?: emptyWordListItem
}

fun wordListToTable(wordListItem: WordListItem, size: Int): SnapshotStateList<SnapshotStateList<Int>> {

    val list = mutableStateListOf<SnapshotStateList<Int>>()
    val emptyList = mutableStateListOf<Int>()

    repeat(size) {
        list.add(emptyList)
    }

    wordListItem.list.forEach {
        list[it.paragraphIndex] += it.wordIndex
    }

    wordListItem.list.forEachIndexed { index, wordIndex ->

    }
//    {
//        list[it.paragraphIndex] += it.wordIndex
//    }

    return list
}

fun wordListToStringFile(list: List<List<Int>>): String {

    val wordListItem: WordListItem = emptyWordListItem
//    var paragraphIndex = 0
//    val wordListTable = list.toList()
    list.forEachIndexed { index, paragraphList ->
        paragraphList.forEach { wordIndex ->

            wordListItem.list += WordIndex(
                wordIndex = wordIndex,
//                paragraphIndex = paragraphList.indexOf(paragraphList)
                paragraphIndex = index
            )
        }
//    wordListForPage.forEach { paragraphList ->
//        paragraphList.forEach { wordIndex ->
//
//            wordListItem.list += WordIndex(
//                wordIndex = wordIndex,
////                paragraphIndex = paragraphList.indexOf(paragraphList)
//                paragraphIndex = paragraphIndex
//            )
//        }
//        Log.d("!!! wordListToStringFile", "$paragraphIndex：${paragraphList.toList()}")
//        paragraphIndex += 1
    }

    return itemToString(wordListItem)
}










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

//fun wordListTableToStringFile(wordListTable: WordListTable): String {
//    var string = ""
//
//    string = wordListTableToString(wordListTable)
//
//    return string
//}