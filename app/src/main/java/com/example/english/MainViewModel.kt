package com.example.english

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val list = mutableStateOf(listOf(""))
}