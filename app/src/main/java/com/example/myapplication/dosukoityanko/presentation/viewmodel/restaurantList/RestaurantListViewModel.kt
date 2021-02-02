package com.example.myapplication.dosukoityanko.presentation.viewmodel.restaurantList

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
import timber.log.Timber

class RestaurantListViewModel(
    private val restaurantListRepository: RestaurantListRepository
) : ViewModel() {

    private val _restaurantList = MutableStateFlow<Resource<List<Restaurant>>>(Resource.Empty)
    val restaurantList: StateFlow<Resource<List<Restaurant>>> = _restaurantList

    private val _selectedRestaurant = MutableLiveData<Restaurant>()
    val selectedRestaurant: LiveData<Restaurant> = _selectedRestaurant

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

    val onRedHandButtonClick = View.OnClickListener {
        optionFabClose()
        getRestaurantBelowThousand()
    }

    val onBlackHandButtonClick = View.OnClickListener {
        optionFabClose()
        getRestaurantBelowThreeThousand()
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
        viewModelScope.launch {
            restaurantListRepository.getRestaurant().collect {
                _restaurantList.value = it
            }
        }
    }

    private fun getRestaurantBelowThousand() {
        viewModelScope.launch {
            restaurantListRepository.getRestaurantBelowThousand().collect {
                _restaurantList.value = it
            }
        }
    }

    private fun getRestaurantBelowThreeThousand() {
        viewModelScope.launch {
            restaurantListRepository.getRestaurantBelowThreeThousand().collect {
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
                    Timber.d("debug: error occurred")
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
