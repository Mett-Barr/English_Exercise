package com.example.english.data.word.wordlist.room

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WordListItem(
    var list: List<WordIndex>
)

@JsonClass(generateAdapter = true)
data class WordIndex(
    var newsIndex: Int,
    var wordIndex: Int
)

object EmptyWordListItem {
    val emptyList = WordListItem(emptyList())
}

