package jp.dosukoityanko.presentation.viewmodel.likeList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.dosukoityanko.domain.entity.restaurantList.Restaurant
import jp.dosukoityanko.domain.repository.likeList.LikeRestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikeListViewModel @Inject constructor(
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
}
