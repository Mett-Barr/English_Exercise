package com.example.english.translation.format

import com.google.gson.annotations.SerializedName

data class PostHeader(
    @SerializedName("X-Android-Package") val packageName: String = "com.example.english",
    @SerializedName("X-Android-Cert") val sha1: String = "17:58:72:D8:5C:35:6A:DB:26:DA:80:E9:1F:C5:28:29:CB:C6:CE:43"
)
