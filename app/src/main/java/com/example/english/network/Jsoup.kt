package com.example.english.network

import android.content.Context
import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import com.example.english.MainViewModel
import org.jsoup.Jsoup

const val BBC_TITLE = "main-heading"
const val BBC_PARAGRAPH = ""
const val BBC_COVER = "div.ssrcss-vk3nhx-ComponentWrapper ep2nwvo1"

suspend fun jsoupTest(viewModel: MainViewModel, context: Context) {

    val html = Jsoup.connect("https://www.bbc.com/news/uk-politics-62410234").get()


    val title = html.getElementById("main-heading")?.text() ?: ""
    viewModel.draftTitle = TextFieldValue(title)


    // get article body
    var content = ""
    html.getElementsByClass("ssrcss-pv1rh6-ArticleWrapper e1nh2i2l6")
        .select("[data-component='text-block']").forEach {

            content += it.text() + "\n\n"

//            Log.d("!!!", it.text())
//            text.value += it.text() + "\n\n"
        }

    viewModel.draftContent = TextFieldValue(content)

    viewModel.addNews(context)
//    StringConverter().stringToListNull(viewModel.draftContent.text)
}

class JsoupNews(url: String) {

    private val html = Jsoup.connect(url).get()

//    fun getTitle(): String = html.getElementById("main-heading")?.text() ?: ""
    fun getTitle(): String = html.getElementById(BBC_TITLE)?.text() ?: ""

    fun getContent(): String {
        var content = ""
        html.getElementsByClass("ssrcss-pv1rh6-ArticleWrapper e1nh2i2l6")
            .select("[data-component='text-block']").forEach {
                content += it.text() + "\n\n"
            }
        return content
    }

}

suspend fun jsoupImageTest(): String {

//    val url = "https://www.bbc.com/news/science-environment-62378157"
//
//    val html = Jsoup.connect(url).get()
//
//    return html.select(BBC_COVER).first()!!.attr("src")




    val url = "https://www.bbc.com/news/science-environment-62378157"

    val html = Jsoup.connect(url).get()

    val imageUrl = html.select(BBC_COVER).attr("src") ?: "error"

//    Log.d("!!!  1", imageUrl)


    val testUrl =
        html.getElementsByClass("ssrcss-11kpz0x-Placeholder e16icw910").select("img[src]").first()!!.attr("src")
//            .toString()

//    Log.d("!!!  2", html.getElementsByClass("ssrcss-11kpz0x-Placeholder e16icw910").toString())

    Log.d("!!!  3", testUrl)

    return testUrl
}

suspend fun jsoupImageTest2() {

    val url = "https://www.bbc.com/news/science-environment-62378157"

    val html = Jsoup.connect(url).get()

    val imageUrl = html.select(BBC_COVER).attr("src") ?: "error"

//    Log.d("!!!  1", imageUrl)


    val testUrl =
        html.getElementsByClass("ssrcss-11kpz0x-Placeholder e16icw910").select("img[src]").first()!!.attr("src")
//            .toString()

//    Log.d("!!!  2", html.getElementsByClass("ssrcss-11kpz0x-Placeholder e16icw910").toString())

    Log.d("!!!  3", testUrl)

}