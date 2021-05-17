package jp.dosukoityanko.data.repository.restaurantList

import android.location.Location
import jp.dosukoityanko.data.entity.common.Distance
import jp.dosukoityanko.data.entity.common.Resource
import jp.dosukoityanko.data.entity.restaurantList.Restaurant
import kotlinx.coroutines.flow.Flow

interface RestaurantListDataStore {

    fun fetchRestaurants(
        location: Location?,
        distance: Distance?,
        operation: (List<Restaurant>) -> List<Restaurant> = { it }
    ): Flow<Resource<List<Restaurant>>>
}
