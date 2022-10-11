package com.example.english.tool.translation.json

import com.squareup.moshi.JsonClass

@JsonClass( generateAdapter = true )
data class Translations(
    val `data`: Data
)