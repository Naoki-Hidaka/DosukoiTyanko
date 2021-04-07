package jp.dosukoityanko.presentation.viewmodel.restaurantList

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import android.os.Looper
import android.widget.RadioGroup
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.dosukoityanko.R
import jp.dosukoityanko.domain.entity.common.Amount
import jp.dosukoityanko.domain.entity.common.Distance
import jp.dosukoityanko.domain.entity.common.Resource
import jp.dosukoityanko.domain.entity.restaurantList.Restaurant
import jp.dosukoityanko.domain.repository.restaurantList.RestaurantListRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantListViewModel @Inject constructor(
    application: Application,
    private val restaurantListRepository: RestaurantListRepository,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val settingsClient: SettingsClient
) : AndroidViewModel(application) {

    private val locationRequest by lazy {
        LocationRequest.create().apply { priority = LocationRequest.PRIORITY_HIGH_ACCURACY }
    }

    private val locationBuilder by lazy {
        LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest).build()
    }

    val onEvent = MutableLiveData<Event>()

    val isLoading = MutableLiveData(false)

    val isNotFoundTextVisible = MutableLiveData(false)

    val selectedRestaurant = MutableLiveData<Restaurant>()

    val emptyImageState = MutableLiveData(true)

    val bottomSheetState = MutableLiveData(false)
    
    private val selectedDistance = MutableLiveData<Distance>()

    private val selectedAmount = MutableLiveData<Amount>()

    val distanceRadioButtonCheckedListener = RadioGroup.OnCheckedChangeListener { _, id ->
        when (id) {
            R.id.a_kilo_meters -> selectedDistance.value = Distance.A_KILO_METERS
            R.id.three_kilo_meters -> selectedDistance.value = Distance.THREE_KILO_METERS
            R.id.five_kilo_meters -> selectedDistance.value = Distance.FIVE_KILO_METERS
        }
    }

    val amountRadioButtonCheckedListener = RadioGroup.OnCheckedChangeListener { _, id ->
        when (id) {
            R.id.three_thousand_yen -> selectedAmount.value = Amount.THREE_THOUSAND
            R.id.five_thousand_yen -> selectedAmount.value = Amount.FIVE_THOUSAND
        }
    }

    fun onSearchButtonClick() {
        onEvent.value = Event.ClickedSearchButton
    }

    fun onSearchFloatingButtonClick() {
        bottomSheetState.value = !(bottomSheetState.value ?: false)
    }

    @SuppressLint("MissingPermission")
    fun getRestaurant() {
        settingsClient.checkLocationSettings(locationBuilder)
            .addOnSuccessListener {
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult?) {
                            locationResult?.lastLocation?.let {
                                fetchRestaurant(it)
                            }
                            fusedLocationProviderClient.removeLocationUpdates(this)
                        }
                    },
                    Looper.getMainLooper()
                )
            }
            .addOnFailureListener {
                if (it is ResolvableApiException) {
                    it.startResolutionForResult(getApplication(), 1)
                }
            }
    }

    fun fetchRestaurant(location: Location) {
        isNotFoundTextVisible.value = false
        emptyImageState.value = false
        isLoading.value = true
        viewModelScope.launch {
            restaurantListRepository.getRestaurant(
                location,
                selectedDistance.value,
                selectedAmount.value
            ).collect {
                when (it) {
                    is Resource.Success -> {
                        onEvent.value = Event.FetchSuccess(it.extractData)
                    }
                    is Resource.ApiError -> {
                        onEvent.value = Event.ApiError
                    }
                    is Resource.NetworkError -> {
                        onEvent.value = Event.NetworkError
                    }
                    else -> {
                    }
                }
            }
            isLoading.value = false
        }
        bottomSheetState.value = false
    }

    fun onSelectRestaurant(restaurant: Restaurant) {
        selectedRestaurant.value = restaurant
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

    fun onCancelButtonClick() {
        bottomSheetState.value = false
    }

    sealed class Event {
        object ClickedSearchButton : Event()
        class FetchSuccess(val restaurantList: List<Restaurant>?) : Event()
        object ApiError : Event()
        object NetworkError : Event()
    }
}
