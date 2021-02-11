package jp.dosukoityanko.domain.repository.restaurantList

import android.location.Location
import jp.dosukoityanko.domain.entity.common.Resource
import jp.dosukoityanko.domain.entity.restaurantList.Restaurant
import kotlinx.coroutines.flow.Flow

interface RestaurantListRepository {

    suspend fun getRestaurant(): Flow<Resource<List<Restaurant>>>

    suspend fun getRestaurantBelowThreeThousand(location: Location?): Flow<Resource<List<Restaurant>>>

    suspend fun getRestaurantBelowFiveThousand(location: Location?): Flow<Resource<List<Restaurant>>>

    suspend fun addRestaurant(
        restaurant: Restaurant,
        callback: () -> Unit,
        fallback: (Throwable) -> Unit
    )
}
