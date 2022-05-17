package com.example.english.data.word.wordlist

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WordListTable(
    var wordListTable: List<List<Int>>
)

object EmptyWordListTable {
    val emptyWordListTable: WordListTable = WordListTable(listOf(listOf()))
}
