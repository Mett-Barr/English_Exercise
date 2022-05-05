package com.example.english.translation.test

import com.example.english.translation.format.PostObject
import com.example.english.translation.format.TranslationAPIFormat
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


const val BASE_URL =
    "https://translation.googleapis.com/language/translate/v2/"

const val testUrl =
    "?key=AIzaSyBjmgx_l-kWKBO1L2Bci7bCxvKM83BQLgY&source=en&target=de&q=Hello%20world&q=My%20name%20is%20Jeff"

//class Translation() {
//    private val retrofit = Retrofit.Builder()
//        .addConverterFactory(ScalarsConverterFactory.create())
//        .baseUrl(BASE_URL)
//        .build()
//}

interface TranslationTest {

    @POST("?")
    suspend fun getTranslatedText(@Body text: PostObject): Call<TranslationAPIFormat>

    @POST("?")
    suspend fun getTranslatedText2(@Body text: PostObject): Call<TranslationAPIFormat>

    @POST("?")
    suspend fun getTranslatedText3(@Body text: String = "key=AIzaSyBjmgx_l-kWKBO1L2Bci7bCxvKM83BQLgY&source=en&target=de&q=Hello%20world&q=My%20name%20is%20Jeff"): Call<TranslationAPIFormat>

//    @POST()
//    suspend fun test(@Body text: PostObject): TranslationAPIFormat
}


object TranslationApi {
//    val retrofitService: TranslationTest by lazy { retrofit.create(TranslationTest::class.java) }
}