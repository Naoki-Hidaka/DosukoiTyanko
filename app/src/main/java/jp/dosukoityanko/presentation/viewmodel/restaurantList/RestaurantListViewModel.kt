package jp.dosukoityanko.presentation.viewmodel.restaurantList

import android.location.Location
import android.widget.RadioGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.dosukoityanko.R
import jp.dosukoityanko.domain.entity.common.Amount
import jp.dosukoityanko.domain.entity.common.Distance
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

    val isLoading = MutableLiveData(true)

    private val _restaurantList = MutableStateFlow<Resource<List<Restaurant>>>(Resource.Empty)
    val restaurantList: StateFlow<Resource<List<Restaurant>>> = _restaurantList

    private val _selectedRestaurant = MutableLiveData<Restaurant>()
    val selectedRestaurant: LiveData<Restaurant> = _selectedRestaurant

    private val _emptyImageState = MutableLiveData(true)
    val emptyImageState: LiveData<Boolean> = _emptyImageState

    private val _bottomSheetState = MutableLiveData(false)
    val bottomSheetState: LiveData<Boolean> = _bottomSheetState

    fun onSearchButtonClick() {
        _bottomSheetState.value = !(_bottomSheetState.value ?: false)
    }

    val finalCalledFunction = MutableLiveData<() -> Unit>()

    private val _location = MutableLiveData<Location>()

    private val _selectedDistance = MutableLiveData<Distance>()
    val selectedDistance: LiveData<Distance> = _selectedDistance

    private val _selectedAmount = MutableLiveData<Amount>()
    val selectedAmount: LiveData<Amount> = _selectedAmount

    val distanceRadioButtonCheckedListener = RadioGroup.OnCheckedChangeListener { _, id ->
        when (id) {
            R.id.a_kilo_meters -> _selectedDistance.value = Distance.A_KILO_METERS
            R.id.three_kilo_meters -> _selectedDistance.value = Distance.THREE_KILO_METERS
            R.id.five_kilo_meters -> _selectedDistance.value = Distance.FIVE_KILO_METERS
        }
    }

    val amountRadioButtonCheckedListener = RadioGroup.OnCheckedChangeListener { _, id ->
        when (id) {
            R.id.three_thousand_yen -> _selectedAmount.value = Amount.THREE_THOUSAND
            R.id.five_thousand_yen -> _selectedAmount.value = Amount.FIVE_THOUSAND
        }
    }

    fun setEmptyImageState(visibleState: Boolean) {
        _emptyImageState.value = visibleState
    }

    fun getRestaurant() {
        finalCalledFunction.value = ::getRestaurant
        viewModelScope.launch {
            restaurantListRepository.getRestaurant(
                _location.value,
                _selectedDistance.value,
                _selectedAmount.value
            ).collect {
                _restaurantList.value = it
            }
        }
        _bottomSheetState.value = false
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

    fun onCancelButtonClick() {
        _bottomSheetState.value = false
    }
}
