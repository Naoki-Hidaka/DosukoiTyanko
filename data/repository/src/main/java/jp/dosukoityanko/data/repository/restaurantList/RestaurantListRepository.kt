package jp.dosukoityanko.data.repository.restaurantList

import android.location.Location
import jp.dosukoityanko.data.entity.common.Amount
import jp.dosukoityanko.data.entity.common.Distance
import jp.dosukoityanko.data.entity.common.Resource
import jp.dosukoityanko.data.entity.restaurantList.Restaurant
import kotlinx.coroutines.flow.Flow

interface RestaurantListRepository {

    suspend fun getRestaurant(
        location: Location?,
        distance: Distance?,
        amount: Amount?
    ): Flow<Resource<List<Restaurant>>>

    suspend fun addRestaurant(
        restaurant: Restaurant,
        callback: () -> Unit,
        fallback: (Throwable) -> Unit
    )
}
