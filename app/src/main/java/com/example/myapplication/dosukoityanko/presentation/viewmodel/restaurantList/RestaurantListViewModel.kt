package com.example.myapplication.dosukoityanko.presentation.viewmodel.restaurantList

import androidx.lifecycle.*
import com.example.myapplication.dosukoityanko.domain.entity.common.Resource
import com.example.myapplication.dosukoityanko.domain.entity.restaurantList.Restaurant
import com.example.myapplication.dosukoityanko.domain.repository.likeList.LikeRestaurantDao
import com.example.myapplication.dosukoityanko.domain.repository.restaurantList.RestaurantListRepository
import com.example.myapplication.dosukoityanko.domain.repository.restaurantList.RestaurantListRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RestaurantListViewModel(
    private val restaurantListRepository: RestaurantListRepository
) : ViewModel() {

    private val _restaurantList = MutableStateFlow<Resource<List<Restaurant>>>(Resource.Empty)
    val restaurantList: StateFlow<Resource<List<Restaurant>>> = _restaurantList

    private val _selectedRestaurant = MutableLiveData<Restaurant>()
    val selectedRestaurant: LiveData<Restaurant> = _selectedRestaurant

    fun getRestaurantList() {
        viewModelScope.launch {
            restaurantListRepository.getRestaurant().collect {
                _restaurantList.value = it
            }
        }
    }

    fun selectRestaurant(position: Int) {
        _selectedRestaurant.value = restaurantList.value.extractData?.get(position)
    }

    fun clickLike(callback: () -> Unit) {
        selectedRestaurant.value?.let {
            viewModelScope.launch {
                restaurantListRepository.addRestaurant(it)
                callback()
            }
        }
    }

    companion object {
        class Factory(
            private val likeRestaurantDao: LikeRestaurantDao,
            private val restaurantListRepository: RestaurantListRepository = RestaurantListRepositoryImpl(
                likeRestaurantDao
            )
        ) : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>) =
                RestaurantListViewModel(restaurantListRepository) as T
        }
    }
}
