package com.example.myapplication.dosukoityanko.presentation.viewmodel.restaurantList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.dosukoityanko.domain.entity.common.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RestaurantListViewModel : ViewModel() {

    private val _restaurantList = MutableLiveData<Resource<List<String>>>()
    val restaurantList: LiveData<Resource<List<String>>> = _restaurantList

    fun getRestaurantList() {
        viewModelScope.launch {
            _restaurantList.value = Resource.InProgress
            delay(1000)
            _restaurantList.value = Resource.Success(
                listOf(
                    "ピカチュウ",
                    "カイリュー",
                    "ヤドラン",
                    "ピジョン",
                    "コダック",
                    "コラッタ",
                    "ズバット",
                    "ギャロップ",
                    "サンダース",
                    "メノクラゲ",
                    "パウワウ",
                    "カラカラ",
                    "タマタマ",
                    "ガラガラ",
                    "フシギダネ"
                )
            )
        }
    }
}
