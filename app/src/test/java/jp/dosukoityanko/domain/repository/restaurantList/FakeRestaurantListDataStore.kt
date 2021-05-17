package jp.dosukoityanko.domain.repository.restaurantList

import android.location.Location
import jp.dosukoityanko.data.entity.common.Distance
import jp.dosukoityanko.data.entity.common.Resource
import jp.dosukoityanko.data.entity.restaurantList.Restaurant
import jp.dosukoityanko.data.repository.restaurantList.RestaurantListDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRestaurantListDataStore(
    private val restaurantList: List<Restaurant>
) : RestaurantListDataStore {

    override fun fetchRestaurants(
        location: Location?,
        distance: Distance?,
        operation: (List<Restaurant>) -> List<Restaurant>
    ): Flow<Resource<List<Restaurant>>> = flow {
        emit(Resource.InProgress)
        delay(1000)
        emit(
            Resource.Success(
                operation(restaurantList)
            )
        )
    }
}
