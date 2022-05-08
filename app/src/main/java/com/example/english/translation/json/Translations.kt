package com.example.english.translation.json

import com.squareup.moshi.JsonClass

@JsonClass( generateAdapter = true )
data class Translations(
    val `data`: Data
)