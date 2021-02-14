package jp.dosukoityanko.presentation.viewmodel.restaurantList

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.dosukoityanko.domain.entity.common.Resource
import jp.dosukoityanko.domain.entity.restaurantList.Restaurant
import jp.dosukoityanko.domain.repository.restaurantList.RestaurantListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantListViewModel @Inject constructor(
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

    val _bottomSheetState = MutableLiveData(false)
    val bottomSheetState: LiveData<Boolean> = _bottomSheetState

    fun onSearchButtonClick() {
        if (_searchButtonState.value == true) {
            optionFabOpen()
        } else {
            optionFabClose()
        }
    }

    val finalCalledFunction = MutableLiveData<() -> Unit>()

    private val _location = MutableLiveData<Location>()

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

    fun getRestaurantBelowThreeThousand() {
        finalCalledFunction.value = ::getRestaurantBelowThreeThousand
        optionFabClose()
        viewModelScope.launch {
            _location.value?.let {
                restaurantListRepository.getRestaurantBelowThreeThousand(it).collect {
                    _restaurantList.value = it
                }
            }
        }
    }

    fun getRestaurantBelowFiveThousand() {
        finalCalledFunction.value = ::getRestaurantBelowFiveThousand
        optionFabClose()
        viewModelScope.launch {
            _location.value?.let {
                restaurantListRepository.getRestaurantBelowFiveThousand(it).collect {
                    _restaurantList.value = it
                }
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

    fun setLocation(location: Location) {
        _location.value = location
    }
}
