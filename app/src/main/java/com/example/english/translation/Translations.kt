package com.example.english.translation

import com.squareup.moshi.JsonClass

@JsonClass( generateAdapter = true )
data class Translations(
    val `data`: Data
)