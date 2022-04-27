package com.example.english

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.english.data.newslist.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    // text input (testing)
    var textFieldValue by mutableStateOf(TextFieldValue("123"))

    var draftTitle by mutableStateOf(TextFieldValue(""))
    var draftContent by mutableStateOf(TextFieldValue(""))

    val list = repository.newsList()


    var animTest by mutableStateOf(false)

    val roomSize: Flow<Int> = repository.getSize()

    var newsListIsEmpty = repository.isEmpty()

    fun roomTest() {
        viewModelScope.launch {
        }
    }


//
//    fun roomTest() {
//        viewModelScope.launch {
//            repeat(3) { repository.addNews(NewsObject.news) }
//            newsListIsEmpty.value  = repository.isEmpty()
//            Log.d("!!!", "roomTest: ${newsListIsEmpty.value}")
//        }
//    }
}