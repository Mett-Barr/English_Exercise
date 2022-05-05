package com.example.english.translation.test

import com.example.english.translation.format.PostFormat
import com.example.english.translation.format.Translation
import com.example.english.translation.format.TranslationAPIFormat
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


private const val BASE_URL =
    "https://translation.googleapis.com/language/translate/v2"

/**
 * Build the Moshi object with Kotlin adapter factory that Retrofit will be using.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * The Retrofit object with the Moshi converter.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

//class Translation() {
//    private val retrofit = Retrofit.Builder()
//        .addConverterFactory(ScalarsConverterFactory.create())
//        .baseUrl(BASE_URL)
//        .build()
//}

interface TranslationTest {
    @POST
    suspend fun getTranslatedText(@Body text: PostFormat): TranslationAPIFormat
}

object TranslationApi {
    val retrofitService: TranslationTest by lazy { retrofit.create(TranslationTest::class.java) }
}