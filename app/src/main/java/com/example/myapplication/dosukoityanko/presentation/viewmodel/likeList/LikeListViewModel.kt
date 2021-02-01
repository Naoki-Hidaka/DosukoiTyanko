package com.example.myapplication.dosukoityanko.presentation.viewmodel.likeList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LikeListViewModel : ViewModel() {

    private val _likeList: MutableLiveData<List<String>> = MutableLiveData()
    val likeList: LiveData<List<String>> = _likeList

    init {
        viewModelScope.launch {
            delay(1000)
            _likeList.value =
                listOf(
                    "ピカチュウ",
                    "カイリュー",
                    "ヤドラン",
                    "ピジョン",
                )
        }
    }
}
