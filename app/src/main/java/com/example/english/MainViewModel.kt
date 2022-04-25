package com.example.english

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.english.data.newslist.Repository
import com.example.english.data.newslist.room.News
import com.example.english.data.newslist.room.NewsObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val list = repository.newsList()

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