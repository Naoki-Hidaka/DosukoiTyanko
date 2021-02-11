package jp.dosukoityanko.domain.repository.restaurantList

import android.location.Location
import jp.dosukoityanko.domain.entity.common.Resource
import jp.dosukoityanko.domain.entity.restaurantList.Restaurant
import kotlinx.coroutines.flow.Flow

interface RestaurantListDataStore {

    fun fetchRestaurants(
        location: Location?,
        operation: (List<Restaurant>) -> List<Restaurant> = { it }
    ): Flow<Resource<List<Restaurant>>>
}
