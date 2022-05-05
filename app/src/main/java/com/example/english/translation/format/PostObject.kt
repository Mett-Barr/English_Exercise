package com.example.english.translation.format

import com.google.gson.annotations.SerializedName

data class PostObject(
    @SerializedName("format") val format: String = "text",
    @SerializedName("q") val q: String,
    @SerializedName("source") val source: String = "en",
    @SerializedName("target") val target: String = "zh-TW",
    @SerializedName("key") val key: String = "AIzaSyBjmgx_l-kWKBO1L2Bci7bCxvKM83BQLgY"
)