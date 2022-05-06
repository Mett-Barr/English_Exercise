package com.example.english.translation.test

import com.example.english.translation.format.PostHeader
import com.example.english.translation.format.PostObject
import com.example.english.translation.format.TranslationAPIFormat
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST


const val BASE_URL =
    "https://translation.googleapis.com/language/translate/v2/detect/"

const val testUrl =
    "?key=AIzaSyBjmgx_l-kWKBO1L2Bci7bCxvKM83BQLgY&source=en&target=de&q=Hello%20world&q=My%20name%20is%20Jeff"

//class Translation() {
//    private val retrofit = Retrofit.Builder()
//        .addConverterFactory(ScalarsConverterFactory.create())
//        .baseUrl(BASE_URL)
//        .build()
//}

interface TranslationTest {

    @Headers(
        "X-Android-Package: com.example.english",
        "X-Android-Cert: 17:58:72:D8:5C:35:6A:DB:26:DA:80:E9:1F:C5:28:29:CB:C6:CE:43"
    )
    @POST("?")
    suspend fun getTranslatedText(@Body text: PostObject): Call<TranslationAPIFormat>

    @Headers(
        "X-Android-Package: com.example.english",
        "X-Android-Cert: 17:58:72:D8:5C:35:6A:DB:26:DA:80:E9:1F:C5:28:29:CB:C6:CE:43"
    )
    @POST("?")
    suspend fun getTranslatedText2(@Body text: PostObject): Call<TranslationAPIFormat>

    @Headers(
        "X-Android-Package: com.example.english",
        "X-Android-Cert: 17:58:72:D8:5C:35:6A:DB:26:DA:80:E9:1F:C5:28:29:CB:C6:CE:43"
    )
    @POST("?")
    suspend fun getTranslatedText3(@Body text: String = "key=AIzaSyBjmgx_l-kWKBO1L2Bci7bCxvKM83BQLgY&source=en&target=de&q=Hello%20world&q=My%20name%20is%20Jeff"): Call<TranslationAPIFormat>

//    @POST()
//    suspend fun test(@Body text: PostObject): TranslationAPIFormat
}


object TranslationApi {
//    val retrofitService: TranslationTest by lazy { retrofit.create(TranslationTest::class.java) }
}