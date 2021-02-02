package com.example.myapplication.dosukoityanko.presentation.viewmodel.likeList

import androidx.lifecycle.*
import com.example.myapplication.dosukoityanko.domain.entity.restaurantList.Restaurant
import com.example.myapplication.dosukoityanko.domain.repository.likeList.LikeRestaurantDao
import com.example.myapplication.dosukoityanko.domain.repository.likeList.LikeRestaurantRepository
import com.example.myapplication.dosukoityanko.domain.repository.likeList.LikeRestaurantRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LikeListViewModel(
    private val likeRestaurantRepository: LikeRestaurantRepository
) : ViewModel() {

    private val _likeList: MutableStateFlow<List<Restaurant>> = MutableStateFlow(emptyList())
    val likeList: StateFlow<List<Restaurant>> = _likeList

    private val _selectedRestaurant = MutableLiveData<Restaurant>()
    val selectedRestaurant: LiveData<Restaurant> = _selectedRestaurant

    init {
        viewModelScope.launch {
            likeRestaurantRepository.getAllRestaurant().collect {
                _likeList.value = it
            }
        }
    }

    fun selectRestaurant(position: Int) {
        _selectedRestaurant.value = likeList.value[position]
    }

    fun deleteRestaurant(restaurant: Restaurant) {
        viewModelScope.launch {
            likeRestaurantRepository.deleteRestaurant(restaurant)
        }
    }

    companion object {
        class Factory(
            likeRestaurantDao: LikeRestaurantDao,
            private val likeRestaurantRepository: LikeRestaurantRepository = LikeRestaurantRepositoryImpl(
                likeRestaurantDao
            )
        ) : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>) = LikeListViewModel(
                likeRestaurantRepository
            ) as T
        }
    }
}
