package jp.dosukoityanko.ui.viewmodel.restaurantList

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.*
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.dosukoityanko.data.entity.common.Amount
import jp.dosukoityanko.data.entity.common.Distance
import jp.dosukoityanko.data.entity.common.Resource
import jp.dosukoityanko.data.entity.restaurantList.Restaurant
import jp.dosukoityanko.data.repository.restaurantList.RestaurantListRepository
import jp.dosukoityanko.ui.viewmodel.util.NoCacheMutableLiveData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RestaurantListViewModel @Inject constructor(
    private val restaurantListRepository: RestaurantListRepository,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val settingsClient: SettingsClient
) : ViewModel() {
    
    private val locationRequest by lazy {
        LocationRequest.create().apply { priority = LocationRequest.PRIORITY_HIGH_ACCURACY }
    }
    private val locationBuilder by lazy {
        LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest).build()
    }

    val onEvent = NoCacheMutableLiveData<ApiEvent>()
    private val _restaurantList = MutableLiveData<List<Restaurant>>()

    val selectedRestaurant = MutableLiveData<Restaurant>()
    val emptyImageState = MutableLiveData(true)
    val bottomSheetState = MutableLiveData(false)
    val isLoading = MutableLiveData(false)
    val currentLocation = MutableLiveData<Location>()
    val isVisibleNotFoundMessage = MutableLiveData(false)

    fun onMenuButtonClick() {
        bottomSheetState.value = true
    }

    fun onSearchButtonClick() {
        getLocation()
    }

    val finalCalledFunction = MutableLiveData<() -> Unit>()

    private val _selectedDistance = MutableLiveData<Distance>()
    private val _selectedAmount = MutableLiveData<Amount>()

    fun onOneKilometerChecked() {
        _selectedDistance.value = Distance.A_KILO_METERS
    }

    fun onThreeKilometerChecked() {
        _selectedDistance.value = Distance.THREE_KILO_METERS
    }

    fun onFiveKilometerChecked() {
        _selectedDistance.value = Distance.FIVE_KILO_METERS
    }

    fun onThreeThousandChecked() {
        _selectedAmount.value = Amount.THREE_THOUSAND
    }

    fun onFiveThousandChecked() {
        _selectedAmount.value = Amount.FIVE_THOUSAND
    }

    fun setEmptyImageState(visibleState: Boolean) {
        emptyImageState.value = visibleState
    }

    private fun getRestaurant() {
        finalCalledFunction.value = ::getRestaurant
        viewModelScope.launch {
            restaurantListRepository.getRestaurant(
                currentLocation.value,
                _selectedDistance.value,
                _selectedAmount.value
            ).collect {
                handleEvent(it)
            }
        }
        bottomSheetState.value = false
    }

    private fun handleEvent(resource: Resource<List<Restaurant>>) {
        when (resource) {
            is Resource.Empty -> {
                isVisibleNotFoundMessage.value = false
                isLoading.value = false
            }
            is Resource.InProgress -> {
                emptyImageState.value = false
                isVisibleNotFoundMessage.value = false
                isLoading.value = true
            }
            is Resource.Success -> {
                isLoading.value = false
                isVisibleNotFoundMessage.value = false
                _restaurantList.value = resource.extractData
                onEvent.setValue(ApiEvent.Success(resource.extractData))
            }
            is Resource.ApiError -> {
                isVisibleNotFoundMessage.value = true
                isLoading.value = false
                onEvent.setValue(ApiEvent.Error(resource.errorBody.errorString()))
            }
            is Resource.NetworkError -> {
                isVisibleNotFoundMessage.value = true
                isLoading.value = false
                onEvent.setValue(ApiEvent.Error(resource.errorMessage))
            }
        }
    }

    fun selectRestaurant(position: Int) {
        selectedRestaurant.value = _restaurantList.value?.get(position)
    }

    fun clickLike(
        callback: () -> Unit,
        fallback: () -> Unit
    ) {
        selectedRestaurant.value?.let {
            viewModelScope.launch {
                restaurantListRepository.addRestaurant(it, callback) {
                    Timber.w("warn: $it")
                    fallback()
                }
            }
        }
    }

    fun onCancelButtonClick() {
        bottomSheetState.value = false
    }

    @SuppressLint("MissingPermission")
    fun getLocation() {
        settingsClient.checkLocationSettings(locationBuilder)
            .addOnSuccessListener {
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult?) {
                            locationResult?.lastLocation?.let {
                                currentLocation.value = it
                                getRestaurant()
                            }
                            fusedLocationProviderClient.removeLocationUpdates(this)
                        }
                    },
                    Looper.getMainLooper()
                )
            }
            .addOnFailureListener {
                onEvent.setValue(ApiEvent.LocationError(it))
            }
    }

    sealed class ApiEvent {
        class Success(val restaurantList: List<Restaurant>?) : ApiEvent()
        class Error(val errorText: String) : ApiEvent()
        class LocationError(val exception: Exception) : ApiEvent()
    }
}
