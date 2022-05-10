package com.example.english.data.word.wordlist.room

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WordListItem(
    var list: List<WordIndex>
)

@JsonClass(generateAdapter = true)
data class WordIndex(
    var paragraphIndex: Int,
    var wordIndex: Int
)

object EmptyWordList {
    val wordList = WordList(0, 0, "")
    val wordListItem = WordListItem(emptyList())
}

