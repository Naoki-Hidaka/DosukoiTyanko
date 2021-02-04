package com.example.myapplication.dosukoityanko.presentation.viewmodel.restaurantList

import android.location.Location
import android.view.View
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

    private val _emptyImageState = MutableLiveData(true)
    val emptyImageState: LiveData<Boolean> = _emptyImageState

    private val _searchButtonState = MutableLiveData(true)
    val searchButtonState: LiveData<Boolean> = _searchButtonState

    private val _handButtonState = MutableLiveData(false)
    val handButtonState: LiveData<Boolean> = _handButtonState

    val onSearchButtonClick = View.OnClickListener {
        if (_searchButtonState.value == true) {
            optionFabOpen()
        } else {
            optionFabClose()
        }
    }

    fun setEmptyImageState(visibleState: Boolean) {
        _emptyImageState.value = visibleState
    }

    private fun optionFabOpen() {
        _searchButtonState.value = false
        _handButtonState.value = true
    }

    private fun optionFabClose() {
        _searchButtonState.value = true
        _handButtonState.value = false
    }

    fun getRestaurantList() {
        optionFabClose()
        viewModelScope.launch {
            restaurantListRepository.getRestaurant().collect {
                _restaurantList.value = it
            }
        }
    }

    fun getRestaurantBelowThreeThousand(location: Location) {
        optionFabClose()
        viewModelScope.launch {
            restaurantListRepository.getRestaurantBelowThreeThousand(location).collect {
                _restaurantList.value = it
            }
        }
    }

    fun getRestaurantBelowFiveThousand(location: Location) {
        optionFabClose()
        viewModelScope.launch {
            restaurantListRepository.getRestaurantBelowFiveThousand(location).collect {
                _restaurantList.value = it
            }
        }
    }

    fun selectRestaurant(position: Int) {
        _selectedRestaurant.value = restaurantList.value.extractData?.get(position)
    }

    fun clickLike(
        callback: () -> Unit,
        fallback: () -> Unit
    ) {
        selectedRestaurant.value?.let {
            viewModelScope.launch {
                restaurantListRepository.addRestaurant(it, callback) {
                    fallback()
                }
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
